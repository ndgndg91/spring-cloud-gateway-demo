package com.ndgndg91.bff.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Clock
import java.time.LocalDateTime

@Service
class TimeBasedSaleService2(
    private val clock: Clock
){
    @Value("#{T(java.time.LocalDateTime).parse('\${time.kst.sale.start}', T(java.time.format.DateTimeFormatter).ofPattern('yyyy-MM-dd HH:mm:ss z'))}")
    private lateinit var saleStartTime: LocalDateTime
    @Value("#{T(java.time.LocalDateTime).parse('\${time.kst.sale.end}', T(java.time.format.DateTimeFormatter).ofPattern('yyyy-MM-dd HH:mm:ss z'))}")
    private lateinit var saleEndTime: LocalDateTime

    fun isNowOnSale(): Boolean {
        val now = LocalDateTime.now(clock)
        return now.isAfter(saleStartTime) && now.isBefore(saleEndTime)
    }
}