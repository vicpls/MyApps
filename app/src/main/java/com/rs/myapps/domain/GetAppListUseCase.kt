package com.rs.myapps.domain

import com.rs.myapps.data.AppInfDao
import com.rs.myapps.data.IAppRepository
import com.rs.myapps.domain.model.AppInf
import javax.inject.Inject


class GetAppListUseCase @Inject constructor(
    private val repo: IAppRepository
) {
    suspend operator fun invoke(): List<AppInf> =
        repo.getAppList().map{ it.toAppInf() }
}


fun AppInfDao.toAppInf() =
    AppInf(
        name = name ?: "?",
        version = version ?: "?",
        pack = pack,
        sourceDir = sourceDir
    )