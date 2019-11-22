package com.ctyeung.dictation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.ctyeung.dictation.databinding.FragmentShareBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ShareFragment(val listener:OnDialogListener) : DialogFragment() {
    lateinit var binding: FragmentShareBinding
    lateinit var _context: Context

    interface OnDialogListener {
        fun onShareDlgClick()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShareBinding.inflate(inflater, container, false)
        _context = this.context ?: listener as Context
        initValues(_context)
        initClickHandler()
        return binding.root
    }

    fun initValues(context: Context) {
        val view: View = binding.root

    }

    fun initClickHandler() {
        val view: View = binding.root

        val btnShare = view.findViewById<FloatingActionButton>(R.id.btnShare)
        btnShare.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                onClickShare()
            }
        })
    }

    fun onClickShare() {
        listener.onShareDlgClick()
        dismiss()
    }
}