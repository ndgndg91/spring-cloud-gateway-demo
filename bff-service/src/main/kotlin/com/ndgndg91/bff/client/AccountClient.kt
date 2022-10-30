package com.ndgndg91.bff.client

import com.ndgndg91.bff.client.protocol.response.RootAccount
import feign.Param
import feign.RequestLine
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import java.math.BigInteger

interface AccountClient {

    @RequestLine("GET /apis/root-accounts")
    fun findAll(): List<RootAccount>

    @RequestLine("GET /apis/root-accounts/{id}")
    fun findById(@Param id: BigInteger): RootAccount
}