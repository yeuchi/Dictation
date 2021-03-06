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
import com.ctyeung.dictatekotlin.databinding.FragmentShareBinding
import com.ctyeung.dictatekotlin.viewModel.SharedPrefUtility
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ShareFragment(val listener:OnDialogListener) : DialogFragment() {
    lateinit var binding: FragmentShareBinding
    lateinit var _context: Context
    lateinit var txtTitle:EditText

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
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding = FragmentShareBinding.inflate(inflater, container, false)
        _context = this.context ?: listener as Context
        initValues(_context)
        initClickHandler()
        return binding.root
    }

    fun initValues(context: Context) {
        val view: View = binding.root
        val title = SharedPrefUtility.getShareTitle(context)
        txtTitle = view.findViewById<EditText>(R.id.txtTitle)
        txtTitle.setText(title)
    }

    fun initClickHandler() {
        val view: View = binding.root

        val btnShare = view.findViewById<FloatingActionButton>(R.id.btnShare)
        btnShare.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                onClickShare()
            }
        })

        val btnCancel = view.findViewById<FloatingActionButton>(R.id.btnCancel)
        btnCancel.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                dismiss()
            }
        })
    }

    fun onClickShare() {
        SharedPrefUtility.setShareTitle(_context, txtTitle.text.toString())
        listener.onShareDlgClick()
        dismiss()
    }
}