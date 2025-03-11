package com.giri.springcloudgateway.filter.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import org.springframework.http.HttpHeaders
import java.time.LocalDateTime

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
@JsonIgnoreProperties(ignoreUnknown = true)
data class AccessLog(
    val timestamp: String = LocalDateTime.now().toString(),
    val requestId: String,
    val ip: String,
    val method: String,
    val url: String,
    val requestHeaders: HttpHeaders,
    val body: String?,
    val statusCode: String,
    val responseHeaders: HttpHeaders,
    val response: String?,
    val errorMessage: List<ExceptionLog>? = null
) {
    companion object {
        fun ok(requestId: String,
               request: RequestLog,
               response: ResponseLog): AccessLog {
            return AccessLog(
                requestId = requestId,
                ip = request.ip,
                method = request.method,
                url = request.url,
                requestHeaders = request.headers,
                body = request.body,
                statusCode = response.statusCode,
                responseHeaders = response.headers,
                response = response.body
            )
        }
    }
}