package com.giri.springcloudgateway.helper

import org.slf4j.LoggerFactory
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object SignatureHelper {
    private val log = LoggerFactory.getLogger(SignatureHelper::class.java)
    fun isInvalidSignature(hashedMessage: String, message: String, secretKey: String): Boolean {
        log.info("서버 컨캣 메세지 원본 : {}", message)
        val encodeMessage = Base64.getEncoder().encode(message.toByteArray())
        val secret = SecretKeySpec(secretKey.toByteArray(), "HmacSHA512")
        val mac = Mac.getInstance("HmacSHA512")
        mac.init(secret)
        val hexString = toHexString(mac.doFinal(encodeMessage))
        log.info("{} , {}", hashedMessage, hexString)
        return hashedMessage != hexString
    }

    private fun toHexString(bytes: ByteArray): String {
        val formatter = Formatter()
        for (b in bytes) {
            formatter.format("%02x", b)
        }
        return formatter.toString()
    }
}


