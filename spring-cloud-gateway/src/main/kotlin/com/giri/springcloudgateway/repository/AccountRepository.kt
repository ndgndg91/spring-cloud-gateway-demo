package com.giri.springcloudgateway.repository

import org.springframework.stereotype.Repository
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Repository
class AccountRepository(
    private val webClient: WebClient
) {

    fun verify(): Mono<Boolean> {
        return Mono.just(false)
    }


}