package com.giri.springcloudgateway.filter

import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange

@Component
class CachingBodyFilter: GatewayFilterFactory<CachingBodyFilter.Config> {

    override fun getConfigClass(): Class<Config> {
        return Config::class.java
    }

    override fun newConfig(): Config {
        return Config("CachingBodyFilter")
    }

    data class Config(val name: String?)

    override fun apply(config: Config?): GatewayFilter {
        return GatewayFilter { exchange: ServerWebExchange, chain: GatewayFilterChain ->
            ServerWebExchangeUtils.cacheRequestBody(exchange) { serverHttpRequest: ServerHttpRequest ->
                chain.filter(exchange.mutate().request(serverHttpRequest).build())

            }
        }
    }
}