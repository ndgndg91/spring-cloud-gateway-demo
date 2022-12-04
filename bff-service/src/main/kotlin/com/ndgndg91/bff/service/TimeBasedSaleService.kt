package com.ndgndg91.bff.service

import com.ndgndg91.bff.time.ASIA_SEOUL
import com.ndgndg91.bff.time.now
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class TimeBasedSaleService {

    @Value("#{T(java.time.LocalDateTime).parse('\${time.kst.sale.start}', T(java.time.format.DateTimeFormatter).ofPattern('yyyy-MM-dd HH:mm:ss z'))}")
    private lateinit var saleStartTime: LocalDateTime
    @Value("#{T(java.time.LocalDateTime).parse('\${time.kst.sale.end}', T(java.time.format.DateTimeFormatter).ofPattern('yyyy-MM-dd HH:mm:ss z'))}")
    private lateinit var saleEndTime: LocalDateTime

    fun isNowOnSale(): Boolean {
        val now = now(ASIA_SEOUL)
        return now.isAfter(saleStartTime) && now.isBefore(saleEndTime)
    }
}