package com.rs.myapps.data

class CheckSumFormatUseCase {

    operator fun invoke(checkSum: ByteArray) : String {
        val sb = StringBuilder()

        checkSum.forEach{
            sb.append(String.format("%02x",it))
        }

        return sb.toString()
    }
}