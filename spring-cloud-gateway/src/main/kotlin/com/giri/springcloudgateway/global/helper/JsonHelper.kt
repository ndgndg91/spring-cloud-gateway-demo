package com.giri.springcloudgateway.global.helper

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

object JsonHelper {
    private val objectMapper = jacksonObjectMapper()

    fun Any.toJson(): String = objectMapper.writeValueAsString(this)

    fun Any.toJsonByteArray(): ByteArray = objectMapper.writeValueAsBytes(this)
}