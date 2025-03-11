package com.giri.springcloudgateway.global.handler

import com.giri.springcloudgateway.filter.LoggingFilter.Companion.AG_TRACE_ID
import com.giri.springcloudgateway.filter.dto.AccessLog
import com.giri.springcloudgateway.filter.dto.ExceptionLog
import com.giri.springcloudgateway.global.dto.ErrorResponse
import com.giri.springcloudgateway.global.extension.ip
import com.giri.springcloudgateway.global.helper.JsonHelper.toJson
import com.giri.springcloudgateway.global.helper.JsonHelper.toJsonByteArray
import org.slf4j.LoggerFactory
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@Component
class GlobalExceptionHandler: ErrorWebExceptionHandler {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun handle(exchange: ServerWebExchange, ex: Throwable): Mono<Void> {
        val errorResponse = ErrorResponse()

        val logs = toExceptionLogs(ex)
        logger.info(AccessLog(
            requestId = exchange.attributes[AG_TRACE_ID] as? String ?: UUID.randomUUID().toString(),
            ip =  exchange.request.ip(),
            method = exchange.request.method.toString(),
            url = exchange.request.uri.toString(),
            requestHeaders = exchange.request.headers,
            body = "on error",
            statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value().toString(),
            responseHeaders = exchange.response.headers,
            response = "",
            errorMessage = logs
        ).toJson())

        val bytes = errorResponse.toJsonByteArray()
        val buffer = exchange.response.bufferFactory().wrap(bytes)
        exchange.response.headers[HttpHeaders.CONTENT_TYPE] = MediaType.APPLICATION_JSON_VALUE
        return exchange.response.writeWith(Flux.just(buffer))
    }

    private fun toExceptionLogs(ex: Throwable): MutableList<ExceptionLog> {
        val logs = mutableListOf(formatStackTraceToExceptionLog(ex))
        logs.addAll(ex.suppressedExceptions.map { formatStackTraceToExceptionLog(it) })
        return logs
    }

    private fun formatStackTraceToExceptionLog(ex: Throwable): ExceptionLog {
        return ExceptionLog(
            exClassName = ex.javaClass.name,
            exMessage = ex.message?.split("\n\t")?.toList() ?: emptyList(),
            stackTrace = Arrays.stream(ex.stackTrace)
                .map(StackTraceElement::toString)
                .toList()
        )

    }
}