package com.rs.myapps.data

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import com.rs.myapps.LTAG
import com.rs.myapps.domain.IoDispatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.security.MessageDigest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepositoryImpl @Inject constructor(
    @param:ApplicationContext val context: Context,
    @param:IoDispatcher val corDispatcher: CoroutineDispatcher,
): IAppRepository {

    override suspend fun getAppList(): List<AppInfDao> {

        val pm: PackageManager = context.packageManager

        return withContext(corDispatcher){
            pm.getInstalledPackages(0).map {
                AppInfDao(
                    name = it.applicationInfo?.loadLabel(pm)?.toString(),
                    version = it.versionName,
                    pack = it.packageName,
                    sourceDir = it.applicationInfo?.sourceDir
                )
            }
        }
    }

    override suspend fun calculateCheckSum(
        path: String,
        checkSumAlgorithm: String
    ): Result<ByteArray> {
        return withContext(corDispatcher) {
            runCatching {

                val digest = MessageDigest.getInstance(checkSumAlgorithm)
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
}