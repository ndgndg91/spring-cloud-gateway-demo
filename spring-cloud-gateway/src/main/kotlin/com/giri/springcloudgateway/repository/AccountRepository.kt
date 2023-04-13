package com.giri.springcloudgateway.repository

import com.giri.springcloudgateway.domain.account.Account
import com.giri.springcloudgateway.exception.account.AccountServerException
import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Repository
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.time.Duration

@Repository
class AccountRepository(
    private val webClient: WebClient
) {
    private val cache: Cache<String, List<Account>> = Caffeine.newBuilder()
        .expireAfterWrite(Duration.ofSeconds(10))
        .maximumSize(5000)
        .build()
    fun findAccountByAccessToken(accessToken: String): Mono<List<Account>> {
        return cache.getIfPresent(accessToken)?.let { Mono.just(it) }?: webClient.get()
            .uri("http://localhost:1010/apis/root-accounts?accessToken=$accessToken")
            .retrieve()
            .onStatus({it.is4xxClientError || it.is5xxServerError}, { Mono.error(AccountServerException())})
            .bodyToMono(object : ParameterizedTypeReference<List<Account>>() {})
            .doOnNext { cache.put(accessToken, it) }
    }
}