package com.giri.springcloudgateway.filter.dto

data class ExceptionLog(
    val exClassName: String,
    val exMessage: List<String>,
    val stackTrace: List<String>,
)