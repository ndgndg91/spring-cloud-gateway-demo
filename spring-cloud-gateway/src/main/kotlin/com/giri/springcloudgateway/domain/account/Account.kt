package com.giri.springcloudgateway.domain.account

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.math.BigInteger

@JsonIgnoreProperties(ignoreUnknown = true)
data class Account(
    val id: BigInteger,
    val email: String,
    val secretKey: String
)