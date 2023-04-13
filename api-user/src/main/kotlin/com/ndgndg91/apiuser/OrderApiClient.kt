package com.ndgndg91.apiuser

import com.ndgndg91.apiuser.domain.Order
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.math.BigInteger

@Component
class OrderApiClient(private val client: WebClient) {

    fun findAll(nonce: BigInteger): Mono<List<Order>> {
        return client.get().uri("http://localhost:8080/apis/orders")
            .header(HttpHeaders.AUTHORIZATION, "Bearer 7ed2adbe-5c81-4278-99df-675555157d2b")
            .header("nonce", nonce.toString())
            .retrieve()
            .onStatus({it.is4xxClientError || it.is5xxServerError}, { Mono.error(RuntimeException())})
            .bodyToMono(object : ParameterizedTypeReference<List<Order>>(){})
    }
}