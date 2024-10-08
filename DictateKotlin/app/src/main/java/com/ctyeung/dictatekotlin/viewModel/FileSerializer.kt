package com.ctyeung.dictatekotlin.viewModel

import android.content.Context
import android.content.Intent
import android.os.StrictMode
import android.util.Log
import com.ctyeung.dictatekotlin.R
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar

/**
 * model -> serialization
 * singleton because it has only 1 use case here
 */
object FileSerializer {

    fun write2email(context: Context, msg: String): Pair<Boolean, String> {
        try {
            val builder = StrictMode.VmPolicy.Builder()
            StrictMode.setVmPolicy(builder.build())

//            val uri =
//                SharedPrefUtility.getFileUri(
//                    context
//                )
            val emailIntent = Intent(Intent.ACTION_SEND_MULTIPLE)
            emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            emailIntent.type = context.resources.getString(R.string.default_directory) + "/*"

            // Subject
            val title = context.resources.getString(R.string.default_directory)
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, title)

            emailIntent.putExtra(Intent.EXTRA_TEXT, msg)
            //emailIntent.putExtra(Intent.EXTRA_TEXT, msg)
//            emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uri)

            // user select share application
            if (emailIntent.resolveActivity(context.packageManager) != null) {
                val send_title = context.resources.getString(R.string.default_title)
                context.startActivity(Intent.createChooser(emailIntent, send_title))
            }
            return Pair<Boolean, String>(true, context.resources.getString(R.string.share_success))

        } catch (e: Exception) {
            val error = context.resources.getString(R.string.share_error) + e.message
            Log.e(error, e.message, e)
            return Pair<Boolean, String>(false, error)
        }
    }

    fun write2file(context: Context, str: String): Pair<Boolean, String> {
        val myFile =
            SharedPrefUtility.getFile(context)

        try {
            if (!myFile.exists())
                myFile.createNewFile()

            val fos: FileOutputStream
            val dateTime =
                getDateTime(
                    context
                )
            val buf = dateTime + "\r\n" + str + "\r\n"
            val data = buf.toByteArray()
            fos = FileOutputStream(myFile, true)
            fos.write(data)
            fos.flush()
            fos.close()
            return Pair<Boolean, String>(true, context.resources.getString(R.string.file_saved))
        } catch (e: Exception) {
            e.printStackTrace()
            return Pair<Boolean, String>(
                false,
                context.resources.getString(R.string.write_file_error) + e
            )
        }
    }

    private fun getDateTime(context: Context): String {
        val hasDateTime =
            SharedPrefUtility.getHasDateTime(
                context
            )
        if (hasDateTime) {
            val df = SimpleDateFormat("d MMM yyyy HH:mm:ss")
            val date = df.format(Calendar.getInstance().getTime())
            return date
        }
        return " " // spacer
    }
}