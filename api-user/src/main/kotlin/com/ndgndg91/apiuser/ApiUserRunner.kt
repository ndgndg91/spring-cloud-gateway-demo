package com.ndgndg91.apiuser

import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.TimeUnit


@Component
class ApiUserRunner(
    private val client: OrderApiClient
) : ApplicationRunner, ApplicationContextAware {
    private val log = LoggerFactory.getLogger(ApiUserRunner::class.java)
    private lateinit var context: ApplicationContext

    /**
     * 60 초 동안 호출 후 자동 종료
     */
    override fun run(args: ApplicationArguments?) {
        val stop = System.currentTimeMillis() + TimeUnit.MILLISECONDS.toMillis(1000 * 60)
        while (stop > System.currentTimeMillis()) {
            Thread.sleep(500)
            client.findAll(Random().nextLong().toBigInteger()).subscribe {
                log.info("{}", it)
            }
        }

        log.info("Time to terminate application")
        Thread.sleep(1000)
        (context as ConfigurableApplicationContext).close()
    }

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.context = applicationContext
    }
}