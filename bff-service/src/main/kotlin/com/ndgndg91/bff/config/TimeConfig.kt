package com.ndgndg91.bff.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Clock

@Configuration
class TimeConfig {

    @Bean
    fun clock(): Clock {
        return Clock.systemUTC()
    }
}