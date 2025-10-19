package com.rs.myapps.domain

import com.rs.myapps.data.IAppRepository
import javax.inject.Inject

const val ALG_MD5 = "MD5"

class CheckSumUseCase @Inject constructor(
    private val repo: IAppRepository
) {
    suspend operator fun invoke(path: String, algorithm: String = ALG_MD5): Result<ByteArray> =
        repo.calculateCheckSum(path, algorithm)
}

