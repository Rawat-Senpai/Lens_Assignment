package com.example.lens_assignment.utils

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.lens_assignment.R
class LightTheme: MyAppTheme{
    override fun backgroundColor(context: Context): Int {
        return ContextCompat.getColor(context, R.color.lightBackground)
    }

    override fun mainTextColor(context: Context): Int {
        return ContextCompat.getColor(context, R.color.lightMainTextColor)
    }

    override fun changeIconColor(context: Context): Int {
        return ContextCompat.getColor(context, R.color.lightWhiteIcons)
    }

    override fun changeButtonColor(context: Context): Int {
        return ContextCompat.getColor(context, R.color.black)
    }

    override fun changeTextHintColor(context: Context): Int {
        return ContextCompat.getColor(context, R.color.lightHintColor)
    }

    override fun mainEditTextColor(context: Context): Int {
        return ContextCompat.getColor(context, R.color.lightMainTextColor)
    }

    override fun id(): Int {
        return  0
    }

}


class DarkTheme: MyAppTheme{
    override fun backgroundColor(context: Context): Int {
        return ContextCompat.getColor(context, R.color.darkBackground)
    }

    override fun mainTextColor(context: Context): Int {
        return ContextCompat.getColor(context, R.color.darkMainTextColor)
    }

    override fun changeIconColor(context: Context): Int {
        return ContextCompat.getColor(context, R.color.darkWhiteIcons)
    }


    override fun changeButtonColor(context: Context): Int {
        return ContextCompat.getColor(context, R.color.black)
    }


    override fun changeTextHintColor(context: Context): Int {
        return ContextCompat.getColor(context, R.color.darkHintColor)
    }

    override fun mainEditTextColor(context: Context): Int {
        return ContextCompat.getColor(context, R.color.lightMainTextColor)
    }


    override fun id(): Int {
        return  1
    }

}
