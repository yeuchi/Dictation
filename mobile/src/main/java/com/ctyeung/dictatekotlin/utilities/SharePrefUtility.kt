package com.ctyeung.dictatekotlin.utilities

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.ctyeung.dictatekotlin.R
import java.io.File

object SharedPrefUtility
{
    val mypreference = "mypref"
    val keyfilepath = "filepath"
    val keydatetime = "datetime"
    val keyShareTitle = "shareTitle"

    /*
     * uri to last random dot images
     */
    fun getFile(context: Context):File {
        val path = Environment.getExternalStorageDirectory().toString()

        val directory = File(path, context.resources.getString(R.string.default_directory))
        if(!directory.exists())
            directory.mkdirs()

        val filename = getFilePath(context)
        val file = File(directory, filename)
        return file
    }

    fun getFileUri(context: Context): ArrayList<Uri> {

        val file:File = getFile(context)
        val uri = FileProvider.getUriForFile(
            context,
            "com.ctyeung.dictatekotlin.fileprovider",
            file
        )
        val list = java.util.ArrayList<Uri>()
        list.add(uri)
        return list
    }

    fun getHasDateTime(context: Context):Boolean
    {
        val defaultValue:Boolean = true
        val sharedPreferences = getSharedPref(context)
        return sharedPreferences.getBoolean(keydatetime, defaultValue)
    }

    fun setHasDateTime(context: Context, onOff:Boolean)
    {
        val sharedPreferences = getSharedPref(context)
        val editor = sharedPreferences.edit()
        editor.putBoolean(keydatetime, onOff)
        editor.commit()
    }

    fun getShareTitle(context: Context):String
    {
        return getString(context, keyShareTitle, context.resources.getString(R.string.default_title))
    }

    fun setShareTitle(context: Context, title:String)
    {
        setString(context, keyShareTitle, title)
    }

    fun getFilePath(context: Context):String
    {
        return getString(context, keyfilepath, context.resources.getString(R.string.default_filename))
    }

    fun setFilePath(context: Context, filePath:String)
    {
        setString(context, keyfilepath, filePath)
    }

    fun getString(context: Context, key:String, defaultValue:String):String
    {
        val sharedPreferences = getSharedPref(context)
        return sharedPreferences.getString(key, defaultValue)?:defaultValue
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