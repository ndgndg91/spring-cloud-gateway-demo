package com.ndgndg91.account.controller

import java.math.BigInteger

data class RootAccount(
    val id: BigInteger,
    val email: String,
    val accessTokens: List<String>,
    val secretKey: String
)