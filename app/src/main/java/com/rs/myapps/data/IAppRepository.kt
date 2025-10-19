package com.rs.myapps.data

interface IAppRepository {
    suspend fun getAppList(): List<AppInfDao>
    suspend fun calculateCheckSum(
        path: String,
        checkSumAlgorithm: String
    ): Result<ByteArray>
}
