package com.giri.springcloudgateway.repository

import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.time.Duration

@Repository
class NonceRepository(private val template: ReactiveStringRedisTemplate) {

    companion object {
        private const val PREFIX = "NONCE"
    }

    fun isExists(accessToken: String, nonce: String): Mono<Boolean> {
        return template.hasKey("$PREFIX:$accessToken:$nonce")
    }

    fun setNonce(accessToken: String, nonce: String):  Mono<Boolean> {
        return template.opsForValue().set("$PREFIX:$accessToken:$nonce", "1", Duration.ofSeconds(10))
    }
}