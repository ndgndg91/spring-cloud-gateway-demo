package com.ndgndg91.apiuser

import com.ndgndg91.apiuser.domain.Order
import com.ndgndg91.apiuser.helper.SignatureHelper
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.math.BigInteger

@Component
class OrderApiClient(private val client: WebClient) {
    companion object {
        private const val secret = "7d28969f-fbd4-4117-810d-9bbf17f1c4f1"
    }

    fun findAll(nonce: BigInteger): Mono<List<Order>> {
        return client.get().uri("http://localhost:8080/apis/orders")
            .header(HttpHeaders.AUTHORIZATION, "Bearer 7ed2adbe-5c81-4278-99df-675555157d2b")
            .header("nonce", nonce.toString())
            .header("signature", SignatureHelper.createSignature("GET:/apis/orders:$nonce", secret))
            .retrieve()
            .onStatus({it.is4xxClientError || it.is5xxServerError}, { Mono.error(RuntimeException())})
            .bodyToMono(object : ParameterizedTypeReference<List<Order>>(){})
    }
}