package com.rs.myapps.data

import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.security.MessageDigest

const val ALG_MD5 = "MD5"
const val LTAG = com.rs.myapps.LTAG + ".CheckSumUS"

class CheckSumUseCase(
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    suspend operator fun invoke(path: String, algorithm: String = ALG_MD5): Result<ByteArray> =

        withContext(dispatcher) {
            runCatching {

                val digest = MessageDigest.getInstance(algorithm)
                val apkFile = File(path)

                Log.d(LTAG, "apk size = ${apkFile.length()}")

                val byteArray = ByteArray(1024)
                var bytesCount: Int

                FileInputStream(apkFile).use { fis ->
                    while ((fis.read(byteArray).also { bytesCount = it }) != -1) {
                        digest.update(byteArray, 0, bytesCount)
                    }
                }

                return@runCatching digest.digest()
            }
        }
}

