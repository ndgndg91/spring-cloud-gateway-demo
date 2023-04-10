package com.ndgndg91.orderservice

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.math.BigInteger

@Service
class OrderService (
    private val repository: OrderRepository
){

    fun placeOrder(title: String): Mono<BigInteger> {
        val order = Order(title = title)
        return repository
            .save(order)
            .mapNotNull { it.id }
    }

    fun findAll(): Flux<Order> {
        return repository.findAll()
    }
}