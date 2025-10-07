package com.rs.myapps.data

data class AppInf(
    val name: String,
    val version: String,
    val pack: String,
    val checksum: String? = null,
    val sourceDir: String? = null,
)
