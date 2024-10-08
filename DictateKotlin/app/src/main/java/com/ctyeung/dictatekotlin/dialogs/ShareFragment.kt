package com.ctyeung.dictatekotlin.dialogs

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.ctyeung.dictatekotlin.R
import com.ctyeung.dictatekotlin.databinding.FragmentShareBinding
import com.ctyeung.dictatekotlin.viewModel.SharedPrefUtility

class ShareFragment(private val listener: OnDialogListener) : DialogFragment() {
    private lateinit var binding: FragmentShareBinding
    private lateinit var _context: Context
    private lateinit var txtTitle: EditText

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

    private fun initValues(context: Context) {
        val view: View = binding.root
        val title = SharedPrefUtility.getShareTitle(context)
        txtTitle = view.findViewById<EditText>(R.id.txtTitle)
        txtTitle.setText(title)
    }

    private fun initClickHandler() {
        val view: View = binding.root
        binding.apply {
            btnShare.setOnClickListener {
                onClickShare()
            }

            btnCancel.setOnClickListener {
                dismiss()
            }
        }
    }

    private fun onClickShare() {
        SharedPrefUtility.setShareTitle(_context, txtTitle.text.toString())
        listener.onShareDlgClick()
        dismiss()
    }
}