package com.ndgndg91.apiuser.helper

import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object SignatureHelper {
    fun createSignature(message: String, secretKey: String): String {
        val encodeMessage = Base64.getEncoder().encode(message.toByteArray())
        val secret = SecretKeySpec(secretKey.toByteArray(), "HmacSHA512")
        val mac = Mac.getInstance("HmacSHA512")
        mac.init(secret)
        return toHexString(mac.doFinal(encodeMessage))
    }

    private fun toHexString(bytes: ByteArray): String {
        val formatter = Formatter()
        for (b in bytes) {
            formatter.format("%02x", b)
        }
        return formatter.toString()
    }
}


