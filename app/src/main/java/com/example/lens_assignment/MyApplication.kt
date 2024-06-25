package com.example.lens_assignment

import android.app.Application
import com.google.android.libraries.places.api.Places
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltAndroidApp
class MyApplication :Application() {

    override fun onCreate() {
        super.onCreate()

        Places.initialize(applicationContext,"AIzaSyAXzDd0QhaE7aF1l75w_zoE3mtjamQCmV0")

    }
}