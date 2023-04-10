package com.ndgndg91.orderservice

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import java.math.BigInteger

interface OrderRepository: ReactiveCrudRepository<Order, BigInteger> {
}