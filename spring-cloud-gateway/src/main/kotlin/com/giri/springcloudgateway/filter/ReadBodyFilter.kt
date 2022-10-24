package com.giri.springcloudgateway.filter

import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils.CACHED_REQUEST_BODY_ATTR
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets


@Component
class ReadBodyFilter: GatewayFilterFactory<ReadBodyFilter.Config> {
    companion object {
        private val log = LoggerFactory.getLogger(ReadBodyFilter::class.java)
    }

    data class Config(val name: String?)

    override fun getConfigClass(): Class<Config> {
        return Config::class.java
    }

    override fun newConfig(): Config {
        return Config("ReadBodyFilter")
    }

    override fun apply(config: Config?): GatewayFilter {
        return GatewayFilter { exchange, chain ->
            val cachedBody = StringBuilder()
            val cachedBodyAttribute = exchange.getAttribute<Any>(CACHED_REQUEST_BODY_ATTR)
            if (cachedBodyAttribute !is DataBuffer) {
                log.info("not cached")
                // caching gone wrong error handling
            }
            val dataBuffer: DataBuffer = cachedBodyAttribute as DataBuffer
            cachedBody.append(StandardCharsets.UTF_8.decode(dataBuffer.asByteBuffer()).toString())
            val bodyAsJson = cachedBody.toString()
            log.info("{}", bodyAsJson)
            chain.filter(exchange)
        }
    }
}