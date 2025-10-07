package com.rs.myapps

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

const val LTAG = "myApps"

@HiltAndroidApp
class MyApps : Application()