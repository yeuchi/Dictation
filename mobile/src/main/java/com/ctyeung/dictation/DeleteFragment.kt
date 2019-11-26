package com.ctyeung.dictation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.ctyeung.dictation.databinding.FragmentDeleteBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DeleteFragment(val listener:OnDialogListener,
                     val countSelected:Int,
                     val countTotal:Int) : DialogFragment() {
    lateinit var binding: FragmentDeleteBinding

    interface OnDialogListener {
        fun onDeleteDlgClick()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDeleteBinding.inflate(inflater, container, false)
        initValues()
        initClickHandler()
        return binding.root
    }

    fun getMessage():String
    {
        /*
         * delete all
         */
        if(0==countSelected || countSelected==countTotal)
        {
            return binding.root.resources.getString(R.string.msg_delete_all)
        }
        // delete selected few
        else
        {
            return binding.root.resources.getString(R.string.msg_delete_selected)
        }
    }

    fun initValues() {
        val view: View = binding.root
        val txt = view.findViewById<TextView>(R.id.txt_delete_info)
        txt.setText(getMessage())
    }

    fun initClickHandler() {
        val view: View = binding.root

        val btnDelete = view.findViewById<FloatingActionButton>(R.id.btnDelete)
        btnDelete.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                onClickDelete()
            }
        })

        val btnCancel = view.findViewById<FloatingActionButton>(R.id.btnCancel)
        btnCancel.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                dismiss()
            }
        })
    }

    fun onClickDelete() {
        listener.onDeleteDlgClick()
        dismiss()
    }
}