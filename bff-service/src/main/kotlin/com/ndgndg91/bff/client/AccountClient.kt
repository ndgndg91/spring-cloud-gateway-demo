package com.ndgndg91.bff.client

import com.ndgndg91.bff.client.dto.response.RootAccount
import feign.Param
import feign.RequestLine
import java.math.BigInteger

interface AccountClient {

    @RequestLine("GET /apis/root-accounts")
    fun findAll(): List<RootAccount>

    @RequestLine("GET /apis/root-accounts/{id}")
    fun findById(@Param id: BigInteger): RootAccount
}