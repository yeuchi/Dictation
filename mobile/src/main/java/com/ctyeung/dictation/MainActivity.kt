package com.ctyeung.dictation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.databinding.DataBindingUtil
import com.ctyeung.dictation.databinding.ActivityMainBinding
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.widget.Toast
import java.nio.file.Files.exists
import android.os.Environment.getExternalStorageDirectory
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Environment
import android.widget.TextView
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import android.speech.RecognizerIntent
import android.R.attr.data
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray


/*
 * Description:	Dictation exercise from Intel's literature
 *
 * Reference:
 *
 *  Kotlin
 *  https://medium.com/@mxcsyounes/discover-the-core-android-api-for-speech-recognition-4591e87fd55b
 *
 *  More detail example
 *  http://www.coderzheaven.com/2017/06/01/android-speech-to-text-tutorial/
 *
 *  Original Java:
 *  https://software.intel.com/en-us/articles/developing-android-applications-with-voice-recognition-features
 */
class MainActivity : AppCompatActivity(), ListAdapter.ListItemClickListener {
    lateinit var binding: ActivityMainBinding
    lateinit var activity: MainActivity
    lateinit var speechRecognizer:SpeechRecognitionHelper
    val REQ_CODE_SPEECH_INPUT = 100
    lateinit var textView:TextView
    lateinit var recycleView:RecyclerView
    var list:ArrayList<String> = ArrayList<String>()

    override fun onListItemClick(clickItemIndex: Int)
    {
        // a recycler item is clicked
    }

    //protected var speechHelper: SpeechRecognitionHelper? = null
    //protected var textView: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding?.mainLayout = this
        activity = this
        speechRecognizer = SpeechRecognitionHelper()

        recycleView = activity.findViewById(R.id.rv_stanza)
        recycleView?.layoutManager = LinearLayoutManager(this)
        recycleView?.adapter = ListAdapter(this, list)
    }

    /*
     * Start dictation
     */
    fun onClickStart()
    {
        askSpeechInput()
    }

    /*
     * Stop dictation
     */
    fun onClickPause()
    {

    }

    /*
     * Save dictation to file
     */
    fun onClickSave()
    {
        // loop through list and write to file
        //write2file()
    }

    /*
     * Clear text on screen
     */
    fun onClickClear()
    {
        list.clear()
        recycleView?.invalidate()
    }

    override fun onActivityResult(requestCode:Int, resultCode:Int, data:Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_SPEECH_INPUT && resultCode == RESULT_OK)
        {
            val matches = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val size:Int = matches?.size?:0
            if(size > 0)
            {
                list.add(matches.toString())
                recycleView.adapter = ListAdapter(this, list)
                //recycleView?.invalidate()
            }
        }
    }

    private fun askSpeechInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi speak something");
        startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
    }

    private fun write2file(str: String) {
        val df = SimpleDateFormat("d MMM yyyy HH:mm:ss")
        val date = df.format(Calendar.getInstance().getTime())

        val filename = "dictate.txt"
        val myFile = File(
            Environment
                .getExternalStorageDirectory(), filename
        )
        try {
            if (!myFile.exists())
                myFile.createNewFile()

            val fos: FileOutputStream
            val buf = date + " " + str + "\r\n"
            val data = buf.toByteArray()
            fos = FileOutputStream(myFile, true)
            fos.write(data)
            fos.flush()
            fos.close()
            Toast.makeText(this, date + " file saved", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}

