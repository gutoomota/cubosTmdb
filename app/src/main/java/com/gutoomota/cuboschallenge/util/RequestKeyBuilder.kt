package com.gutoomota.cuboschallenge.util

import java.security.SecureRandom

class RequestKeyBuilder {

    fun generateRandomKey(length: Int): String {
        val chars = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray()
        val sb = StringBuilder(length)
        val random = SecureRandom()
        for (i in 0 until length) {
            val c = chars[random.nextInt(chars.size)]
            sb.append(c)
        }
        return sb.toString()
    }
}
