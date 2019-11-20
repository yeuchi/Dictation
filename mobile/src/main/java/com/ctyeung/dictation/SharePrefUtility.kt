package com.ctyeung.dictation

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color

object SharedPrefUtility
{
    val mypreference = "mypref"
    val keyfilepath = "filepath"
    val keydirectory = "directory"
    val keydatetime = "datetime"

    open fun getHasDateTime(context: Context):Boolean
    {
        val defaultValue:Boolean = false
        val sharedPreferences = getSharedPref(context)
        return sharedPreferences.getBoolean(keydatetime, defaultValue)
    }

    open fun setHasDateTime(context: Context, onOff:Boolean)
    {
        val sharedPreferences = getSharedPref(context)
        val editor = sharedPreferences.edit()
        editor.putBoolean(keydatetime, onOff)
        editor.commit()
    }

    open fun getDirectory(context: Context):String
    {
        return getString(context, keydirectory, "DICTATION")
    }

    open fun setDirectory(context: Context, directory:String)
    {
        setString(context, keydirectory, directory)
    }

    open fun getFilePath(context: Context):String
    {
        return getString(context, keyfilepath, "dictate.txt")
    }

    open fun setFilePath(context: Context, filePath:String)
    {
        setString(context, keyfilepath, filePath)
    }

    fun getString(context: Context, key:String, defaultValue:String):String
    {
        val sharedPreferences = getSharedPref(context)
        return sharedPreferences.getString(key, defaultValue)
    }

    fun setString(context: Context, key:String, str:String)
    {
        val sharedPreferences = getSharedPref(context)
        val editor = sharedPreferences.edit()
        editor.putString(key, str)
        editor.commit()
    }

    fun getSharedPref(context: Context): SharedPreferences
    {
        return context.getSharedPreferences(mypreference, Context.MODE_PRIVATE)
    }
}