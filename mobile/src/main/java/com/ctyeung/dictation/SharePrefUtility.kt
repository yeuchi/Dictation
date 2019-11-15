package com.ctyeung.dictation

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color

object SharedPrefUtility
{
    val mypreference = "mypref"
    var keyfilepath = "filepath"


    open fun getFilePath(context: Context):String
    {
        val defaultValue:String = ""
        val sharedPreferences = getSharedPref(context)
        return sharedPreferences.getString(keyfilepath, defaultValue)
    }

    open fun setFilePath(context: Context, filePath:String)
    {
        val sharedPreferences = getSharedPref(context)
        val editor = sharedPreferences.edit()
        editor.putString(keyfilepath, filePath)
        editor.commit()
    }

    fun getSharedPref(context: Context): SharedPreferences
    {
        return context.getSharedPreferences(mypreference, Context.MODE_PRIVATE)
    }
}