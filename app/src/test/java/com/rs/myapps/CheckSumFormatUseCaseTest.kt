package com.rs.myapps

import com.rs.myapps.data.CheckSumFormatUseCase
import org.junit.Assert.assertEquals
import org.junit.Test


class CheckSumFormatUseCaseTest {

    val bytes = intArrayOf(0xFF, 0x12, 0xAB, 0x57)
        .map{it.toByte()}.toByteArray()

    val rightAnswer = "ff12ab57"

    @Test
    fun formatting_isCorrect() {
        assertEquals(rightAnswer, CheckSumFormatUseCase()(bytes))
    }
}