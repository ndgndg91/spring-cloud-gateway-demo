package com.ndgndg91.bff.config

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.ndgndg91.bff.client.exception.AccountApiBadRequestException
import com.ndgndg91.bff.client.exception.AccountApiGenericException
import com.ndgndg91.bff.client.exception.AccountApiNotFoundException
import feign.Contract
import feign.RequestInterceptor
import feign.RequestTemplate
import feign.Response
import feign.codec.Decoder
import feign.codec.Encoder
import feign.codec.ErrorDecoder
import org.springframework.beans.factory.ObjectFactory
import org.springframework.beans.factory.ObjectProvider
import org.springframework.boot.autoconfigure.http.HttpMessageConverters
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.cloud.openfeign.support.HttpMessageConverterCustomizer
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder
import org.springframework.cloud.openfeign.support.SpringDecoder
import org.springframework.cloud.openfeign.support.SpringEncoder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import java.util.*


@EnableFeignClients
@Configuration
class FeignConfig {

    @Bean
    fun feignEncoder(): Encoder {
        return SpringEncoder { HttpMessageConverters() }
    }

    @Bean
    fun feignDecoder(customizers: ObjectProvider<HttpMessageConverterCustomizer>): Decoder {
        val objectMapper = jacksonObjectMapper().registerKotlinModule()
        objectMapper.registerModule(JavaTimeModule())
        val jacksonConverter: HttpMessageConverter<*> = MappingJackson2HttpMessageConverter(objectMapper)
        val objectFactory: ObjectFactory<HttpMessageConverters> = ObjectFactory<HttpMessageConverters> {
            HttpMessageConverters(
                jacksonConverter
            )
        }

        return ResponseEntityDecoder(SpringDecoder(objectFactory, customizers))
    }

    @Bean
    fun feignErrorDecoder(): ErrorDecoder {
        return ErrorDecoder { _: String, response: Response ->
            when(response.status()) {
                HttpStatus.BAD_REQUEST.value() -> throw AccountApiBadRequestException()
                HttpStatus.NOT_FOUND.value() -> throw AccountApiNotFoundException()
                else -> throw AccountApiGenericException()
            }
        }
    }

    @Bean
    fun feignRequestInterceptor(): RequestInterceptor {
        return RequestInterceptor { template: RequestTemplate ->
            template.header("USER_AGENT", "BFF_SERVICE")
            template.header("REQUEST_ID", UUID.randomUUID().toString())
        }
    }

    @Bean
    fun feignContract(): Contract {
        return Contract.Default()
    }
}

