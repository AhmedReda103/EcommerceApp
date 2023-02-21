package com.example.ecommerceapp.dialog

import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.example.ecommerceapp.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

fun Fragment.setupBottomSheetDialog(
    onSendClick:(String)->Unit
){
    val dialog = BottomSheetDialog(requireContext())
    val view = layoutInflater.inflate(R.layout.reset_password_dialog , null )
    dialog.setContentView(view)
    dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
    dialog.show()

    val btnCancel = view.findViewById<Button>(R.id.btn_cancel_dialog)
    val btnSend = view.findViewById<Button>(R.id.btn_send_dialog)
    val etEmail = view.findViewById<EditText>(R.id.et_email_dialog)

    btnSend.setOnClickListener {
        val email = etEmail.text.toString()
        onSendClick(email)
        dialog.dismiss()
    }
    btnCancel.setOnClickListener {
        dialog.dismiss()
    }
    
}