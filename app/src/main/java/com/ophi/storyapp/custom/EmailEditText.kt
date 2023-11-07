package com.ophi.storyapp.custom

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.ophi.storyapp.R

class EmailEditText : AppCompatEditText, View.OnTouchListener {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!isValidEmail(s.toString())) {
                    isValid = false
                    error = context.getString(R.string.email_error)
                } else {
                    isValid = true
                    error = null
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        return false
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex =
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.(com|org|net|edu|gov|co.id|[a-zA-Z]{2})$"

        return email.matches(emailRegex.toRegex())
    }

    companion object {
        var isValid: Boolean = false
    }
}