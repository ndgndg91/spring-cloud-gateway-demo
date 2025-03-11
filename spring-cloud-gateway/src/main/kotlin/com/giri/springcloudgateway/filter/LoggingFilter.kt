package com.giri.springcloudgateway.filter

import com.giri.springcloudgateway.filter.dto.AccessLog
import com.giri.springcloudgateway.filter.dto.RequestLog
import com.giri.springcloudgateway.filter.dto.ResponseLog
import com.giri.springcloudgateway.global.extension.ip
import org.reactivestreams.Publisher
import org.slf4j.LoggerFactory
import org.springframework.core.Ordered
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DataBufferFactory
import org.springframework.core.io.buffer.DefaultDataBufferFactory
import org.springframework.http.HttpMethod
import org.springframework.http.server.reactive.ServerHttpRequestDecorator
import org.springframework.http.server.reactive.ServerHttpResponseDecorator
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.function.Supplier

@Component
class LoggingFilter : WebFilter, Ordered {
    companion object {
        private val logger = LoggerFactory.getLogger(LoggingFilter::class.java)

        const val AG_TRACE_ID = "ag-trace-id"
        private const val REQUEST_LOG = "cag-request-log"
        private const val RESPONSE_LOG = "cag-response-log"
        private const val K8S_PROBE_PREFIX = "/actuator"
    }

    private fun shouldLog(exchange: ServerWebExchange): Boolean {
        val request = exchange.request
        return request.method != HttpMethod.OPTIONS &&
                request.method != HttpMethod.GET &&
                !request.uri.path.startsWith(K8S_PROBE_PREFIX)
    }

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void?> {
        val requestId = UUID.randomUUID().toString()
        exchange.attributes[AG_TRACE_ID] = requestId

        val decoratedRequest = LoggingRequestDecorator(exchange).mutate().header(AG_TRACE_ID, requestId).build()
        val decoratedResponse = LoggingResponseDecorator(exchange)

        return chain.filter(
            exchange.mutate()
                .request(decoratedRequest)
                .response(decoratedResponse)
                .build()
        ).doOnSuccess { if (shouldLog(exchange)) logCombined(exchange, requestId) }
    }

    override fun getOrder(): Int = Ordered.HIGHEST_PRECEDENCE

    private fun logCombined(exchange: ServerWebExchange, requestId: String) {
        val requestData = exchange.attributes[REQUEST_LOG] as? RequestLog ?: RequestLog.EMPTY
        val responseData = exchange.attributes[RESPONSE_LOG] as? ResponseLog ?: ResponseLog.EMPTY
        logger.info("{}",
            AccessLog.ok(
                requestId = requestId,
                request = requestData,
                response = responseData,
            )
        )
    }

    inner class LoggingRequestDecorator(exchange: ServerWebExchange) : ServerHttpRequestDecorator(exchange.request) {
        private val cachedBody: Flux<DataBuffer>

        init {
            cachedBody = storeRequest(exchange)
        }

        override fun getBody(): Flux<DataBuffer> = cachedBody

        private fun storeRequest(exchange: ServerWebExchange): Flux<DataBuffer> {
            val request = exchange.request
            val requestId = exchange.attributes[AG_TRACE_ID] as String

            val defaultRequestLog = RequestLog(
                headers = request.headers,
                url = request.uri.toString(),
                method = request.method.toString(),
                ip = request.ip(),
                body = ""
            )
            exchange.attributes[REQUEST_LOG] = defaultRequestLog

            return request.body
                .switchIfEmpty(Flux.just(DefaultDataBufferFactory().wrap(ByteArray(0)))) // 빈 본문 처리
                .map { buffer ->
                    val bodyString = buffer.toString(StandardCharsets.UTF_8)
                    if (bodyString.isNotEmpty()) { // 본문이 있으면 업데이트
                        exchange.attributes[REQUEST_LOG] = defaultRequestLog.copy(
                            body = bodyString
                        )
                    }
                    buffer // 원래 버퍼 반환
                }.cache() // 캐싱
                .doOnError { error ->
                    logger.error("[$requestId] Error reading request body", error)
                    exchange.attributes[REQUEST_LOG] = defaultRequestLog.copy(
                        body = "Error occurred when reading request body"
                    )
                }
        }
    }

    inner class LoggingResponseDecorator(private val exchange: ServerWebExchange) : ServerHttpResponseDecorator(exchange.response) {
        private val dataBufferFactory: DataBufferFactory = exchange.response.bufferFactory()

        /**
         * response body 가 없는 경우
         */
        override fun beforeCommit(action: Supplier<out Mono<Void>>) {
            if (exchange.attributes[RESPONSE_LOG] == null) {
                exchange.attributes[RESPONSE_LOG] = ResponseLog(
                    headers = exchange.response.headers,
                    statusCode = statusCode?.value().toString(),
                    body = ""
                )
            }

            super.beforeCommit(action)
        }

        /**
         * response body 가 있는 경우 실행
         * writerWith -> beforeCommit 순서
         */
        override fun writeWith(body: Publisher<out DataBuffer>): Mono<Void> {
            return super.writeWith(
                Flux.from(body)
                    .buffer()
                    .map { dataBuffers ->
                        val joinedBuffers = DefaultDataBufferFactory().join(dataBuffers as MutableList<out DataBuffer>)
                        val content = ByteArray(joinedBuffers.readableByteCount())
                        joinedBuffers.read(content)
                        val responseBody = String(content, StandardCharsets.UTF_8)

                        exchange.attributes[RESPONSE_LOG] = ResponseLog(
                            headers = headers,
                            statusCode = statusCode?.value().toString(),
                            body = responseBody
                        )
                        dataBufferFactory.wrap(content)
                    }
                    .singleOrEmpty()
            )
        }
    }
}