package com.giri.springcloudgateway.filter

import com.giri.springcloudgateway.repository.AccountRepository
import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory
import org.springframework.stereotype.Component

@Component
class AuthenticationFilter(
    private val repository: AccountRepository
): GatewayFilterFactory<AuthenticationFilter.Config> {

    private val log = LoggerFactory.getLogger(AuthenticationFilter::class.java)

    data class Config(val name: String?)

    override fun getConfigClass(): Class<Config> {
        return Config::class.java
    }

    override fun newConfig(): Config {
        return Config("AuthenticationFilter")
    }

    override fun apply(config: Config?): GatewayFilter {
        return GatewayFilter { exchange, chain ->
            chain.filter(exchange)
        }
    }
}