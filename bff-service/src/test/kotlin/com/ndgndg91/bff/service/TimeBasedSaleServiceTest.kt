package com.ndgndg91.bff.service

import com.ndgndg91.bff.time.ASIA_SEOUL
import com.ndgndg91.bff.time.CHANGEABLE_CLOCK
import com.ndgndg91.bff.time.now
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.test.util.ReflectionTestUtils
import java.time.Clock
import java.time.LocalDateTime
import java.time.Month
import java.time.ZoneOffset

@ExtendWith(MockitoExtension::class)
internal class TimeBasedSaleServiceTest {
    @InjectMocks
    private lateinit var service: TimeBasedSaleService


    @BeforeEach
    fun setup() {
        CHANGEABLE_CLOCK = null
        ReflectionTestUtils.setField(service, "saleStartTime", LocalDateTime.of(2023, 1, 1, 0, 0, 0))
        ReflectionTestUtils.setField(service, "saleEndTime", LocalDateTime.of(2023, 2, 1, 0, 0, 0))
    }

    @Test
    fun isNowOnSale_true() {
        // given
        CHANGEABLE_CLOCK = Clock.fixed(
            LocalDateTime.of(2023, 1, 10, 0, 0, 0).toInstant(ZoneOffset.ofHours(9)),
            ASIA_SEOUL
        )

        // when
        val now = now(ASIA_SEOUL)
        val result = kotlin.runCatching { service.isNowOnSale() }
            .onFailure { it.printStackTrace() }

        println(now)

        // then
        assertThat(now.year).isEqualTo(2023)
        assertThat(now.month).isEqualTo(Month.JANUARY)
        assertThat(now.dayOfMonth).isEqualTo(10)
        assertThat(now.hour).isEqualTo(0)
        assertThat(now.minute).isEqualTo(0)
        assertThat(now.second).isEqualTo(0)
        assertThat(result.isSuccess).isTrue
        assertThat(result.getOrNull()).isTrue
    }

    @Test
    fun isNowOnSale_false() {
        // given
        CHANGEABLE_CLOCK = Clock.fixed(
            LocalDateTime.of(2022, 12, 1, 0, 0, 0).toInstant(ZoneOffset.ofHours(9)),
            ASIA_SEOUL
        )

        // when
        val now = now(ASIA_SEOUL)
        val result = kotlin.runCatching { service.isNowOnSale() }
            .onFailure { it.printStackTrace() }

        println(now)

        // then
        assertThat(now.year).isEqualTo(2022)
        assertThat(now.month).isEqualTo(Month.DECEMBER)
        assertThat(now.dayOfMonth).isEqualTo(1)
        assertThat(now.hour).isEqualTo(0)
        assertThat(now.minute).isEqualTo(0)
        assertThat(now.second).isEqualTo(0)
        assertThat(result.isSuccess).isTrue
        assertThat(result.getOrNull()).isFalse
    }
}