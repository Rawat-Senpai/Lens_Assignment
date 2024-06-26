package com.example.lens_assignment.utils

import android.content.Context
import com.example.lens_assignment.utils.Constants.PREFS_TOKEN_FILE
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AppInfo @Inject constructor(@ApplicationContext context:Context) {

    private var prefs = context.getSharedPreferences(PREFS_TOKEN_FILE,Context.MODE_PRIVATE)


    fun setDarkMode(theme:Boolean){
        val editor = prefs.edit()
        editor.putBoolean(Constants.THEME,theme)
        editor.apply()
    }

    fun getDarkMode():Boolean{
        return prefs.getBoolean(Constants.THEME,false)
    }



}