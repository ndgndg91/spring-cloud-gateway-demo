package com.giri.springcloudgateway.global.logging

import ch.qos.logback.classic.spi.ILoggingEvent
import com.giri.springcloudgateway.filter.dto.AccessLog
import org.springframework.boot.json.JsonWriter
import org.springframework.boot.json.JsonWriter.Members
import org.springframework.boot.logging.structured.StructuredLogFormatter


class JsonLogger: StructuredLogFormatter<ILoggingEvent> {
    private val extractor =  { event: ILoggingEvent ->
        if (event.argumentArray != null && event.argumentArray.filterIsInstance<AccessLog>().isNotEmpty()) {
            val accessLog = event.argumentArray.first() as AccessLog
            accessLog.toMap()
        } else {
            mapOf("message" to event.formattedMessage)
        }
    }
    private val writer: JsonWriter<ILoggingEvent> = JsonWriter.of { members: Members<ILoggingEvent> ->
        members.add("time") { obj: ILoggingEvent -> obj.instant }
        members.add("level") { obj: ILoggingEvent -> obj.level }
        members.add("thread") { obj: ILoggingEvent -> obj.threadName }
        members.add("logger") { obj: ILoggingEvent -> obj.loggerName }
        members.addMapEntries(extractor)
    }.withNewLineAtEnd()
    override fun format(event: ILoggingEvent): String {
        return writer.writeToString(event)
    }
}