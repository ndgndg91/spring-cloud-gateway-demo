package com.giri.springcloudgateway.filter.dto

import org.springframework.http.HttpHeaders

data class RequestLog(
    val ip: String,
    val method: String,
    val url: String,
    val headers: HttpHeaders,
    val body: String?,
) {
    companion object {
        val EMPTY = RequestLog("", "", "", HttpHeaders.EMPTY, "")
    }
}