package com.ctyeung.dictatekotlin

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.lifecycle.Observer
import androidx.databinding.DataBindingUtil
import com.ctyeung.dictatekotlin.databinding.ActivityMainBinding
import android.widget.Toast
import android.os.Environment
import android.widget.TextView
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import android.speech.RecognizerIntent
import android.os.Build
import android.os.StrictMode
import android.util.Log
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ctyeung.dictatekotlin.room.Verse
import com.ctyeung.dictatekotlin.utilities.SharedPrefUtility
import com.ctyeung.dictatekotlin.utilities.SpeechRecognitionHelper
import com.ctyeung.dictatekotlin.viewModel.VerseViewModel
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
class MainActivity : AppCompatActivity(),
    ListAdapter.ListItemClickListener,
    ShareFragment.OnDialogListener,
    LocalSaveFragment.OnDialogListener,
    DeleteFragment.OnDialogListener {

    val MAX_ITEMS:Int = 200
    val REQ_CODE_SPEECH_INPUT = 100
    var isSavePermitted:Boolean = true
    var countSelected:Int = 0

    lateinit var verseViewModel: VerseViewModel
    lateinit var textInfo:TextView
    lateinit var binding: ActivityMainBinding
    lateinit var activity: MainActivity
    lateinit var speechRecognizer:SpeechRecognitionHelper
    lateinit var recycleView:RecyclerView

    /*
     * Refactor all variables into model
     *
     * 1. Serialization
     * 2. Marshalling of data
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.mainLayout = this
        activity = this
        speechRecognizer = SpeechRecognitionHelper()

        // recycler view
        recycleView = activity.findViewById(R.id.rv_stanza)
        val adapter = ListAdapter(this)
        recycleView.layoutManager = LinearLayoutManager(this)
        recycleView.adapter = adapter

        // data
        verseViewModel = ViewModelProvider(this).get(VerseViewModel::class.java)
        verseViewModel.stanza.observe(this, Observer { stanza ->
            // Update the cached copy of the words in the adapter.
            stanza?.let { adapter.setVerses(it) }
        })

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
            dlg.show(supportFragmentManager, resources.getString(R.string.default_title))
        }
    }

    override fun onShareDlgClick() {
        try {
            val builder = StrictMode.VmPolicy.Builder()
            StrictMode.setVmPolicy(builder.build())

            val uri = SharedPrefUtility.getFileUri(activity)
            val emailIntent = Intent(Intent.ACTION_SEND_MULTIPLE)
            emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            emailIntent.type = resources.getString(R.string.default_directory)+"/*"

            // Subject
            val title = resources.getString(R.string.default_directory)
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, title)
            val msg = SharedPrefUtility.getShareTitle(activity)
            emailIntent.putExtra(Intent.EXTRA_TEXT, msg)
            emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uri)

            // user select share application
            if (emailIntent.resolveActivity(activity.baseContext.packageManager) != null) {
                val send_title = resources.getString(R.string.default_title)
                activity.startActivity(Intent.createChooser(emailIntent, send_title))
            }

        } catch (e: Exception) {
            val msg = resources.getString(R.string.share_error)

            Toast.makeText(this,
                msg,
                Toast.LENGTH_SHORT
            ).show()

            Log.e(msg, e.message, e)
        }
    }

    /*
     * Delete dictation verse(s) on screen and database
     */
    fun onClickDelete()
    {
        countSelected = selectedCount()
        val dlg = DeleteFragment(this, countSelected, verseCount())
        dlg.show(supportFragmentManager, "Delete")
    }

    /*
     * User chooses to delete
     */
    override fun onDeleteDlgClick() {
        if(0==countSelected ||
            countSelected==verseCount()) {
            verseViewModel.clear()
        }
        else
            verseViewModel.deleteSelected()
    }

    /*
     * Recycleview item clicked (Dictation verse)
     */
    override fun onListItemClick(verse: Verse)
    {
        // a recycler item is clicked -- selection changed
        verseViewModel.update(verse)
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

        if(0 == verseViewModel.stanza.value?.size)
        {
            Toast.makeText(this, activity.resources.getString(R.string.no_text), Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    /*
     * Persist fragment button (save) clicked
     */
    override fun onSaveDlgClick()
    {
        // something to save
        var builder: StringBuilder = StringBuilder()

        // loop through list and write to file
        val stanza = verseViewModel.stanza.value?:emptyList<Verse>()
        for (v in stanza) {
            builder.append(v.verse+ "\r\n")
        }

        write2file(builder.toString())
    }

    /*
     * Capture dictation result
     */
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
                val verse = Verse(System.currentTimeMillis(), str)
                verseViewModel.insert(verse)
            }
        }
        updateInfo()
    }

    /*
     * Display appropriate text information
     */
    fun updateInfo()
    {
        val count = verseCount()
        var str:String
        if(count > 0)
        {
            str = activity.resources.getString(R.string.info_count) + " " + count
            popUpSaveReminder()
        }
        else {
            str = activity.resources.getString(R.string.info)
        }
        textInfo.setText(str)
    }

    /*
     * number of verses in db
     */
    fun verseCount():Int
    {
        return verseViewModel.stanza.value?.size?:0
    }

    /*
     * number of selected count
     */
    fun selectedCount():Int
    {
        return verseViewModel.repository.getCountSelected()
    }

    /*
     * if verse count > 200, popup for user to save !
     */
    private fun popUpSaveReminder()
    {
        if(verseCount() > MAX_ITEMS)
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
        val myFile = SharedPrefUtility.getFile(activity)

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

