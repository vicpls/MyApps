package com.rs.myapps.domain

import androidx.compose.runtime.Immutable

@Immutable
data class AppInf(
    val name: String,
    val version: String,
    val pack: String,
    val checksum: String? = null,
    val sourceDir: String? = null,
)