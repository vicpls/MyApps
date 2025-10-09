package com.rs.myapps.domain

class CheckSumFormatUseCase {

    operator fun invoke(checkSum: ByteArray) : String {
        val sb = StringBuilder()

        checkSum.forEach{
            sb.append(String.format("%02x",it))
        }

        return sb.toString()
    }
}