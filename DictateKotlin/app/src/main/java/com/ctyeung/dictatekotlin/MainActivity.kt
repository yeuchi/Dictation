package com.ctyeung.dictatekotlin

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ctyeung.dictatekotlin.databinding.ActivityMainBinding
import com.ctyeung.dictatekotlin.dialogs.DeleteFragment
import com.ctyeung.dictatekotlin.dialogs.ShareFragment
import com.ctyeung.dictatekotlin.room.Verse
import com.ctyeung.dictatekotlin.utilities.SpeechRecognitionHelper
import com.ctyeung.dictatekotlin.viewModel.FileSerializer
import com.ctyeung.dictatekotlin.viewModel.VerseViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
    ListAdapter.ListItemClickListener,
    ShareFragment.OnDialogListener,
    DeleteFragment.OnDialogListener {

    private val REQ_CODE_SPEECH_INPUT = 100

    private val model: VerseViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var speechRecognizer: SpeechRecognitionHelper
    private lateinit var adapter: ListAdapter
    private lateinit var layoutManager: LinearLayoutManager

    /*
     * TODO Refactor all variables into model
     * 1. Serialization
     * 2. Marshalling of data
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.mainLayout = this
        speechRecognizer = SpeechRecognitionHelper()

        adapter = ListAdapter(this@MainActivity)
        layoutManager = LinearLayoutManager(this@MainActivity)
    }

    override fun onResume() {
        super.onResume()
        model.updateInfo()

        binding.apply {
            txtInfo.text = model.txtInfo

            btnStart.setOnClickListener {
                onClickStart()
            }

            btnShare.setOnClickListener {
                onClickShare()
            }

            btnClear.setOnClickListener {
                onClickDelete()
            }

            // recycler view
            rvStanza.layoutManager = layoutManager
            rvStanza.adapter = adapter
        }

        // data
        model.stanza.observe(this, Observer { stanza ->
            // Update the cached copy of the words in the adapter.
            stanza?.let {
                adapter.setVerses(it)
                model.updateInfo()
                binding.txtInfo.text = model.txtInfo
                binding.invalidateAll()
            }
        })
    }

    /**
     * Start dictation
     */
    private fun onClickStart() {
        askSpeechInput()
    }

    /**
     * Share dictation (drive, facebook, email, etc)
     */
    private fun onClickShare() {
        if (isPersistAllowed()) {
            val dlg = ShareFragment(this)
            dlg.show(supportFragmentManager, resources.getString(R.string.default_title))
        }
    }

    override fun onShareDlgClick() {
        val msg = model.serialize()
        val result = FileSerializer.write2email(this@MainActivity, msg)
        Toast.makeText(this, result.second, Toast.LENGTH_SHORT).show()
    }

    /**
     * Delete dictation verse(s) on screen and database
     */
    private fun onClickDelete() {
        val dlg = DeleteFragment(this)
        dlg.show(supportFragmentManager, resources.getString(R.string.msg_delete_all))
    }

    /**
     * User chooses to delete
     */
    override fun onDeleteDlgClick() {
        //if(model.allSelected())
        model.clear()
    }

    /**
     * RecycleView item clicked (Dictation verse)
     */
    override fun onListItemClick(verse: Verse) {
        // a recycler item is clicked -- selection changed
        model.update(verse)
    }

    /*
     * 1. User permits save ?
     * 3. there is dictation to save ?
     */
    private fun isPersistAllowed(): Boolean {
        if (0 == model.stanza.value?.size) {
            Toast.makeText(this, resources.getString(R.string.no_text), Toast.LENGTH_LONG)
                .show()
            return false
        }
        return true
    }

    /**
     * Description: Capture dictation result
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_CODE_SPEECH_INPUT && resultCode == RESULT_OK) {
            val matches = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val size: Int = matches?.size ?: 0
            if (size > 0) {
                val str = matches?.get(0).toString()
                val verse = Verse(System.currentTimeMillis(), str)
                model.insert(verse)
            }
        }
    }

    private fun askSpeechInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(
            RecognizerIntent.EXTRA_PROMPT,
            resources.getString(R.string.say_some)
        )
        startActivityForResult(intent, REQ_CODE_SPEECH_INPUT)
    }
}

