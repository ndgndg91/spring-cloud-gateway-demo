package com.ndgndg91.account.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.math.BigInteger
import java.util.concurrent.ConcurrentHashMap

@RestController
class AccountController {
    companion object {
        private val repository = ConcurrentHashMap(
            mutableMapOf(
                BigInteger.ONE to RootAccount(BigInteger.ONE, "ndgndg91@gmail.com"),
                BigInteger.TWO to RootAccount(BigInteger.TWO, "giri.nam@gmail.com"),
                BigInteger.TEN to RootAccount(BigInteger.TEN, "my@gmail.com")
            )
        )
    }

    @GetMapping("/apis/root-accounts")
    fun findAll(): ResponseEntity<List<RootAccount>> {
        val response = repository.elements().toList().sortedBy { it.id }
        return ResponseEntity.ok(response)
    }

    @GetMapping("/apis/root-accounts/{id}")
    fun findById(@PathVariable id: BigInteger): ResponseEntity<RootAccount> {
        return ResponseEntity.ok(repository[id])
    }
}

