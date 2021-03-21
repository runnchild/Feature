package com.rongc.feature.binding

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.databinding.BindingAdapter


class EditTextLinesLimiter(private val editText: EditText, private val maxLines: Int) :
    TextWatcher {
    private var lastValue = ""
    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
        lastValue = charSequence.toString()
    }

    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

    override fun afterTextChanged(editable: Editable) {
        if (editText.lineCount > maxLines) {
            var selectionStart = editText.selectionStart - 1
            editText.setText(lastValue)
            if (selectionStart >= editText.length()) {
                selectionStart = editText.length()
            }
            editText.setSelection(selectionStart)
        }
    }
}

@BindingAdapter("linesLimit")
fun EditText.linesLimit(lines: Int) {
    addTextChangedListener(EditTextLinesLimiter(this, lines))
}