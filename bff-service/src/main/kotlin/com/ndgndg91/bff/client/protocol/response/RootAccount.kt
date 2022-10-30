package com.ndgndg91.bff.client.protocol.response

import java.math.BigInteger

data class RootAccount(
    val id: BigInteger,
    val email: String
)