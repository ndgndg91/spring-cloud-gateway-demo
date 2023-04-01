package com.ndgndg91.account.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.math.BigInteger
import java.util.NoSuchElementException
import java.util.concurrent.ConcurrentHashMap

@RestController
class AccountController {
    companion object {
        private val repository = ConcurrentHashMap(
            mutableMapOf(
                BigInteger.ONE to RootAccount(BigInteger.ONE, "ndgndg91@gmail.com", listOf(
                    "ad02afd8-e642-4224-91b9-62c56dd3bcca",
                    "7ed2adbe-5c81-4278-99df-675555157d2b"
                )),
                BigInteger.TWO to RootAccount(BigInteger.TWO, "giri.nam@gmail.com", listOf(
                    "9bbba799-459e-4ef0-b6bf-a451d20737ac"
                )),
                BigInteger.TEN to RootAccount(BigInteger.TEN, "my@gmail.com", listOf(
                    "390a934c-e372-4bde-921c-174e1a685424"
                ))
            )
        )
    }

    @GetMapping("/apis/root-accounts")
    fun findAll(
        @RequestParam(required = false) accessToken: String?
    ): ResponseEntity<List<RootAccount>> {
        val response = if (accessToken != null) {
            try {
                repository.elements().toList().first { rootAccount ->
                    val token = rootAccount.accessTokens.find { at ->
                        at == accessToken.lowercase()
                    }
                    token != null
                }.let { listOf(it) }
            } catch (e: NoSuchElementException) {
                emptyList()
            }
        } else {
            repository.elements().toList().sortedBy { it.id }
        }

        return ResponseEntity.ok(response)
    }

    @GetMapping("/apis/root-accounts/{id}")
    fun findById(@PathVariable id: BigInteger): ResponseEntity<RootAccount> {
        return ResponseEntity.ok(repository[id])
    }
}

