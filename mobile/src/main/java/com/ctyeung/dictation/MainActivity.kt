package com.ctyeung.dictation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.databinding.DataBindingUtil
import com.ctyeung.dictation.databinding.ActivityMainBinding
import android.widget.Toast
import android.os.Environment
import android.widget.TextView
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import android.speech.RecognizerIntent
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.lang.StringBuilder


/*
 * Description:	Dictation exercise from Intel's literature
 *
 * Reference:
 *
 *  Permission
 *  https://android--code.blogspot.com/2018/03/android-kotlin-request-permissions-at.html
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
class MainActivity : AppCompatActivity(), ListAdapter.ListItemClickListener, ShareFragment.OnDialogListener, LocalSaveFragment.OnDialogListener {
    val MAX_ITEMS:Int = 200
    val REQ_CODE_SPEECH_INPUT = 100
    var list:ArrayList<String> = ArrayList<String>()
    var verseCount:Long = 0
    var isSavePermitted:Boolean = true

    lateinit var textInfo:TextView
    lateinit var binding: ActivityMainBinding
    lateinit var activity: MainActivity
    lateinit var speechRecognizer:SpeechRecognitionHelper
    lateinit var recycleView:RecyclerView

    /*
     * Recycleview item clicked (Dictation verse)
     */
    override fun onListItemClick(clickItemIndex: Int)
    {
        // a recycler item is clicked
    }

    /*
     * Refactor to use viewModel !!
     */

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

        textInfo = activity.findViewById(R.id.txt_info)

        if (shouldAskPermissions())
            askPermissions()
    }

    /*
     * API 23 and up requires permission for READ/WRITE to storage
     */
    protected fun shouldAskPermissions(): Boolean {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1
    }

    /*
     * Popup to (explicit) request User for permission
     */
    protected fun askPermissions() {

        val permissions = arrayOf(
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"
        )
        val requestCode = 200
        requestPermissions(permissions, requestCode)
    }

    /*
     * User permission request -> result
     */
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray)
    {
        for(permission:String in permissions)
        {
            // result:0 is ok
            val result = ContextCompat.checkSelfPermission(activity, permission)
            if (0!=result)
            {
                // not permitted to save or read -- !!! data-binding refactor
                val btnSave = activity.findViewById<FloatingActionButton>(R.id.btnSave)
                btnSave.hide()
                isSavePermitted = false
            }
        }
    }

    /*
     * Start dictation
     */
    fun onClickStart()
    {
        askSpeechInput()
    }

    /*
     * Save dictation to file
     */
    fun onClickSave()
    {
        if(isPersistAllowed()) {
            val dlg = LocalSaveFragment(this)
            dlg.show(supportFragmentManager, "Local Save")
        }
    }

    /*
     * Share dictation (drive, facebook, email, etc)
     */
    fun onClickShare()
    {
        if(isPersistAllowed()) {
            val dlg = ShareFragment(this)
            dlg.show(supportFragmentManager, "Share")
        }
    }

    /*
     * 1. User permits save ?
     * 2. External storage is available
     * 3. there is dictation to save ?
     */
    fun isPersistAllowed():Boolean
    {
        if(false == isSavePermitted)
        {
            Toast.makeText(this, activity.resources.getString(R.string.not_permitted), Toast.LENGTH_LONG).show()
            return false
        }

        if( false == verifyExternalStorage())
            return false

        if(0 == list.size)
        {
            Toast.makeText(this, activity.resources.getString(R.string.no_text), Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    /*
     * Clear text on screen
     */
    fun onClickClear()
    {
        list.clear()
        recycleView?.invalidate()

        textInfo?.setText(activity.resources.getString(R.string.info))
        verseCount = 0
    }

    /*
     * Persist fragment button (save) clicked
     */
    override fun onSaveDlgClick()
    {
        // something to save
        var builder: StringBuilder = StringBuilder()

        // loop through list and write to file
        for (str in list) {
            builder.append(str+ "\r\n")
        }

        write2file(builder.toString())
    }

    override fun onShareDlgClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
                val str = matches?.get(0).toString()
                list.add(str)
                recycleView.adapter = ListAdapter(this, list)
                //recycleView?.invalidate()
                verseCount ++
                textInfo?.setText(activity.resources.getString(R.string.info_count) + " " + verseCount)

                popUpSaveReminder()
            }
        }
    }

    /*
     * if verse count > 200, popup for user to save !
     */
    private fun popUpSaveReminder()
    {
        if(verseCount > MAX_ITEMS)
        {
            // pop up to ask user to save or select auto-save !!
        }
    }

    private fun askSpeechInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, activity.resources.getString(R.string.say_some));
        startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
    }

    private fun getDateTime():String
    {
        val hasDateTime = SharedPrefUtility.getHasDateTime(activity)
        if(hasDateTime)
        {
            val df = SimpleDateFormat("d MMM yyyy HH:mm:ss")
            val date = df.format(Calendar.getInstance().getTime())
            return date
        }
        return " " // spacer
    }

    private fun write2file(str: String)
    {
        val path = Environment.getExternalStorageDirectory().toString()

        val directory = File(path, SharedPrefUtility.getDirectory(activity))
        directory.mkdirs()

        val filename = SharedPrefUtility.getFilePath(activity)
        val myFile = File(directory, filename)

        try
        {
            if (!myFile.exists())
                myFile.createNewFile()

            val fos: FileOutputStream
            val dateTime = getDateTime()
            val buf = dateTime + "\r\n" + str + "\r\n"
            val data = buf.toByteArray()
            fos = FileOutputStream(myFile, true)
            fos.write(data)
            fos.flush()
            fos.close()
            Toast.makeText(this,  dateTime + activity.resources.getString(R.string.file_saved), Toast.LENGTH_LONG).show()
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    private val isExternalStorageReadOnly: Boolean get() {
        val extStorageState = Environment.getExternalStorageState()
        return if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            true
        } else {
            false
        }
    }
    private val isExternalStorageAvailable: Boolean get() {
        val extStorageState = Environment.getExternalStorageState()
        return if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            true
        } else{
            false
        }
    }

    fun verifyExternalStorage():Boolean
    {
        if(false == isExternalStorageAvailable)
        {
            Toast.makeText(this, activity.resources.getString(R.string.no_storage), Toast.LENGTH_LONG).show()
            return false
        }

        if(true == isExternalStorageReadOnly)
        {
            Toast.makeText(this, activity.resources.getString(R.string.read_only_storage), Toast.LENGTH_LONG).show()
            return false;
        }
        return true;
    }
}

