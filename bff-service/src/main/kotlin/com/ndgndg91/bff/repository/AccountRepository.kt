package com.ndgndg91.bff.repository

import com.ndgndg91.bff.client.AccountClient
import com.ndgndg91.bff.client.dto.response.RootAccount
import feign.Contract
import feign.Feign
import feign.Logger
import feign.RequestInterceptor
import feign.codec.Decoder
import feign.codec.Encoder
import feign.codec.ErrorDecoder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.math.BigInteger

@Repository
class AccountRepository {

    private lateinit var client: AccountClient


    @Autowired
    fun setAccountClient(
        contract: Contract,
        encoder: Encoder,
        decoder: Decoder,
        errorDecoder: ErrorDecoder,
        interceptor: RequestInterceptor
    ){
        this.client =  Feign.builder()
            .contract(contract)
            .encoder(encoder)
            .decoder(decoder)
            .errorDecoder(errorDecoder)
            .requestInterceptor(interceptor)
            .logLevel(Logger.Level.FULL)
            .target(AccountClient::class.java, "http://localhost:1010")
    }

    fun findAll(): List<RootAccount> {
        return client.findAll()
    }

    fun findById(id: BigInteger): RootAccount {
        return client.findById(id)
    }



}