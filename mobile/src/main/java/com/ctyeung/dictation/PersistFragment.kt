package com.ctyeung.dictation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.databinding.Bindable
import androidx.fragment.app.DialogFragment


import androidx.databinding.DataBindingUtil
import com.ctyeung.dictation.databinding.FragmentPersistBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

/*
 * Refactor to use viewModel !!
 */

class PersistFragment(val listener:OnDialogListener) : DialogFragment()
{
    val KEY_SAVE:String = "keySave"
    val KEY_SHARE:String = "keyShare"
    lateinit var binding:FragmentPersistBinding
    lateinit var txtDirectory:EditText
    lateinit var txtFilename:EditText
    lateinit var _context:Context

    interface OnDialogListener
    {
        fun onPersistDlgClick(key: String)
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentPersistBinding.inflate(inflater, container, false)
        _context = this.context?: listener as Context
        initValues(_context)
        initClickHandler()
        return binding.root
    }

    fun initValues(context:Context)
    {
        val view:View = binding.root

        val directory = SharedPrefUtility.getDirectory(context)
        txtDirectory = view.findViewById<EditText>(R.id.txtDirectory)
        txtDirectory?.setText(directory)

        val filename = SharedPrefUtility.getFilePath(context)
        txtFilename = view.findViewById<EditText>(R.id.txtFilename)
        txtFilename?.setText(filename)
    }

    fun initClickHandler()
    {
        val view:View = binding.root

        val btnSave = view.findViewById<FloatingActionButton>(R.id.btnSave)
        btnSave.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                onClickSave()
            }
        })

        val btnShare = view.findViewById<FloatingActionButton>(R.id.btnShare)
        btnShare.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                onClickShare()
            }
        })
    }

    fun onClickSave()
    {
        SharedPrefUtility.setDirectory(_context, txtDirectory.text.toString())
        SharedPrefUtility.setFilePath(_context, txtFilename.text.toString())
        listener.onPersistDlgClick(KEY_SAVE)
        dismiss()
    }

    fun onClickShare()
    {
        listener.onPersistDlgClick(KEY_SHARE)
        dismiss()
    }
}