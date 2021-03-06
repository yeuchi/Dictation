package com.ctyeung.dictatekotlin

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment


import com.ctyeung.dictatekotlin.databinding.FragmentLocalSaveBinding
import com.ctyeung.dictatekotlin.viewModel.SharedPrefUtility
import com.google.android.material.floatingactionbutton.FloatingActionButton

/*
 * Refactor to use viewModel !!
 */

class LocalSaveFragment(val listener:OnDialogListener) : DialogFragment()
{
    lateinit var binding:FragmentLocalSaveBinding
    lateinit var txtDirectory:EditText
    lateinit var txtFilename:EditText
    lateinit var _context:Context

    interface OnDialogListener
    {
        fun onSaveDlgClick()
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding = FragmentLocalSaveBinding.inflate(inflater, container, false)
        _context = this.context?: listener as Context
        initValues(_context)
        initClickHandler()
        return binding.root
    }

    fun initValues(context:Context)
    {
        val view:View = binding.root

        val filename = SharedPrefUtility.getFilePath(context)
        txtFilename = view.findViewById<EditText>(R.id.txtFilename)
        txtFilename.setText(filename)
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

        val btnCancel = view.findViewById<FloatingActionButton>(R.id.btnCancel)
        btnCancel.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                dismiss()
            }
        })
    }

    fun onClickSave()
    {
        /*
         * refactor this to call viewmodel here !!
         * - invoke repository LiveData directly
         */
        SharedPrefUtility.setFilePath(_context, txtFilename.text.toString())
        listener.onSaveDlgClick()
        dismiss()
    }
}