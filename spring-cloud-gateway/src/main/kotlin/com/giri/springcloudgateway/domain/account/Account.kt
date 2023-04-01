package com.giri.springcloudgateway.domain.account

import java.math.BigInteger

data class Account(
    val id: BigInteger,
    val email: String
)