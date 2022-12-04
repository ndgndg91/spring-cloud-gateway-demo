package com.ndgndg91.bff.time

import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneId

var CHANGEABLE_CLOCK: Clock? = null
const val KST = "Asia/Seoul"
val ASIA_SEOUL: ZoneId = ZoneId.of("Asia/Seoul")
val UTC: ZoneId = ZoneId.of("UTC")

fun now(zoneId: ZoneId = UTC): LocalDateTime {
    return if (CHANGEABLE_CLOCK != null) LocalDateTime.now(CHANGEABLE_CLOCK)
    else LocalDateTime.now(zoneId)
}
