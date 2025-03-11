package com.giri.springcloudgateway.global.extension

import org.springframework.http.server.reactive.ServerHttpRequest

fun ServerHttpRequest.ip(): String {
    return (this.headers["True-Client-IP"]?.toString()
        ?: this.headers["CF-Connecting-IP"]?.toString()
        ?: this.remoteAddress?.address?.hostAddress ?: "unknown")
}