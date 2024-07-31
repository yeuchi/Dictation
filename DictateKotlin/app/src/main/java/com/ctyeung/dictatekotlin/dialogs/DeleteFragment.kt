package com.ctyeung.dictatekotlin.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.ctyeung.dictatekotlin.R
import com.ctyeung.dictatekotlin.databinding.FragmentDeleteBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DeleteFragment(private val listener: OnDialogListener) : DialogFragment() {
    private lateinit var binding: FragmentDeleteBinding

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
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding = FragmentDeleteBinding.inflate(inflater, container, false)
        initValues()
        initClickHandler()
        return binding.root
    }

    private fun getMessage():String
    {
            return binding.root.resources.getString(R.string.msg_delete_all)
    }

    private fun initValues() {
        val view: View = binding.root
        val txt = view.findViewById<TextView>(R.id.txt_delete_info)
        txt.text = getMessage()
    }

    private fun initClickHandler() {
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