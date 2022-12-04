package com.ndgndg91.bff.time

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

internal class TimeHelperKtTest {

    @BeforeEach
    fun setup() {
        CHANGEABLE_CLOCK = null
    }

    @Test
    fun test_now() {
        // given - when
        val utc = now()
        val kst = now(ZoneId.of(KST))

        // then
        assertThat(ZoneId.systemDefault()).isEqualTo(ZoneId.of(KST))
        assertThat(utc.year).isEqualTo(kst.year)
        assertThat(utc.dayOfMonth).isEqualTo(kst.dayOfMonth)
        assertThat(utc.hour).isEqualTo(kst.minusHours(9).hour)
        assertThat(utc.minute).isEqualTo(kst.minute)
        assertThat(utc.second).isEqualTo(kst.second)
    }

    @Test
    fun test_now_with_clock() {
        // given
        CHANGEABLE_CLOCK = Clock.fixed(
            LocalDateTime.of(2025, 1, 1, 0, 0, 0)
                .toInstant(ZoneOffset.ofHours(9)),
            ASIA_SEOUL
        )


        // when
        val kst = now()


        // then
        assertThat(ZoneId.systemDefault()).isEqualTo(ZoneId.of(KST))
        assertThat(kst.year).isEqualTo(2025)
        assertThat(kst.dayOfMonth).isEqualTo(1)
        assertThat(kst.hour).isEqualTo(0)
        assertThat(kst.minute).isEqualTo(0)
        assertThat(kst.second).isEqualTo(0)
    }
}