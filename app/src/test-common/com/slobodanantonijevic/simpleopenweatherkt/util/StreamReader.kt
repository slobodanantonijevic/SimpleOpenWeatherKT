package com.slobodanantonijevic.simpleopenweatherkt.util

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

object StreamReader {

    fun readFromStream(location: String): String? {

        val stream = StreamReader::class.java.getResourceAsStream(location)
        val builder = StringBuilder()
        val reader = BufferedReader(InputStreamReader(stream))
        try {

            var line: String? = reader.readLine()
            while (line != null) {

                if (builder.length > 0) {
                    builder.append("\n")
                }
                builder.append(line)
                line = reader.readLine()
            }
        } catch (ioe: IOException) {

            return null
        }

        return builder.toString()
    }
}