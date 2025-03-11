package com.giri.springcloudgateway.filter.dto

import org.springframework.http.HttpHeaders

data class ResponseLog(
    val statusCode: String,
    val headers: HttpHeaders,
    val body: String?
) {
    companion object {
        val EMPTY = ResponseLog("", HttpHeaders.EMPTY, "")
    }
}