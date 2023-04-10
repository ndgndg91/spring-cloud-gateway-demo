package com.ndgndg91.orderservice

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.math.BigInteger

@RestController
class OrderController(
    private val orderService: OrderService
) {

    @PostMapping("/apis/orders")
    fun placeOrder(@RequestBody request: OrderPlacementRequest): Mono<BigInteger> {
        return orderService.placeOrder(request.title)
    }

    @GetMapping("/apis/orders")
    fun findAll(): Flux<Order> {
        return orderService.findAll()
    }
}