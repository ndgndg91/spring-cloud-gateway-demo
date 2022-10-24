package com.some.someservice

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class SomeServiceApplication

fun main(args: Array<String>) {
    runApplication<SomeServiceApplication>(*args)
}


@RestController
class Some {

    companion object {
        private val log = LoggerFactory.getLogger(Some::class.java)
    }

    @PostMapping("/some")
    fun some(@RequestBody body: Body): ResponseEntity<Response>{
        log.info("{}", body)
        return ResponseEntity.ok(Response(true))
    }
}

data class Body(val accessToken: String)

data class Response(val result: Boolean)