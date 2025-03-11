package com.giri.springcloudgateway.filter

import com.giri.springcloudgateway.global.exception.*
import com.giri.springcloudgateway.global.helper.SignatureHelper
import com.giri.springcloudgateway.repository.AccountRepository
import com.giri.springcloudgateway.repository.NonceRepository
import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class AuthenticationFilter(
    private val accountRepository: AccountRepository,
    private val nonceRepository: NonceRepository
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
            val signature = exchange.request.headers["signature"]?.firstOrNull() ?: throw SignatureNotFoundException()
            val nonce = exchange.request.headers["nonce"]?.firstOrNull() ?: throw NonceNotFoundException()
            val auth = exchange.request.headers[HttpHeaders.AUTHORIZATION]?.firstOrNull() ?: throw UnauthorizedException()
            val accessToken = try {
                auth.split(" ")[1]
            } catch (e: IndexOutOfBoundsException) {
                throw UnauthorizedException()
            }
            accountRepository.findAccountByAccessToken(accessToken).flatMap {
                val account = it.firstOrNull()?: return@flatMap Mono.error(UnauthorizedException())
                log.info("{}", account)
                if (SignatureHelper.isInvalidSignature(signature, "${exchange.request.method}:${exchange.request.path}:$nonce", account.secretKey)) {
                    return@flatMap Mono.error(SignatureInvalidException())
                }
                nonceRepository.isExists(accessToken, nonce).flatMap { exists ->
                    if (exists) {
                        Mono.error(DuplicatedNonceException())
                    } else {
                        nonceRepository.setNonce(accessToken, nonce)
                    }
                }
            }.then(chain.filter(exchange))
        }
    }
}