package com.ndgndg91.orderservice

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.math.BigInteger


@Table("`order`")
data class Order(
    @Id var id: BigInteger? = null,
    val title: String
)