package com.ndgndg91.bff.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.test.util.ReflectionTestUtils
import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

@ExtendWith(MockitoExtension::class)
internal class TimeBasedSaleService2Test {

    @InjectMocks
    private lateinit var service: TimeBasedSaleService2

    @Mock
    private lateinit var clock: Clock


    @BeforeEach
    fun setup() {
        ReflectionTestUtils.setField(service, "saleStartTime", LocalDateTime.of(2022, 12, 1, 0, 0, 0))
        ReflectionTestUtils.setField(service, "saleEndTime", LocalDateTime.of(2022, 12, 31, 0, 0, 0))
    }

    @Test
    fun isNowOnSale_true() {
        // given
        val now = ZonedDateTime.of(
            2022, 12, 20, 0, 0, 0, 0,
            ZoneId.of("Asia/Seoul")
        )
        given(clock.zone).willReturn(now.zone)
        given(clock.instant()).willReturn(now.toInstant())

        // when
        val result = kotlin.runCatching { service.isNowOnSale() }
            .onFailure { it.printStackTrace()}

        // then
        assertThat(result.isSuccess).isTrue
        assertThat(result.getOrNull()).isTrue
    }

    @Test
    fun isNowOnSale_false() {
        // given
        val now = ZonedDateTime.of(
            2023, 1, 20, 0, 0, 0, 0,
            ZoneId.of("Asia/Seoul")
        )
        given(clock.zone).willReturn(now.zone)
        given(clock.instant()).willReturn(now.toInstant())

        // when
        val result = kotlin.runCatching { service.isNowOnSale() }
            .onFailure { it.printStackTrace()}

        // then
        assertThat(result.isSuccess).isTrue
        assertThat(result.getOrNull()).isFalse
    }
}