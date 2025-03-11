package com.giri.springcloudgateway.global.dto

import com.giri.springcloudgateway.global.exception.ErrorCode

data class MetaResponse(
    val code: Int = ErrorCode.OK.code,
    val message: String = ErrorCode.OK.name,
    val timestamp: Long = System.currentTimeMillis(),
)