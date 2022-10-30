package com.ndgndg91.bff.controller

import com.ndgndg91.bff.client.protocol.response.RootAccount
import com.ndgndg91.bff.repository.AccountRepository
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.math.BigInteger

@RestController
class AccountController(
    private val repository: AccountRepository
){

    companion object {
        private val log = LoggerFactory.getLogger(AccountController::class.java)
    }

    @GetMapping("/apis/root-accounts")
    fun findAll(): ResponseEntity<Response<List<RootAccount>>> {
        return ResponseEntity.ok(Response(repository.findAll()))
    }

    @GetMapping("/apis/root-accounts/{id}")
    fun findById(@PathVariable id: BigInteger): ResponseEntity<Response<RootAccount>> {
        return ResponseEntity.ok(Response(repository.findById(id)))
    }
}