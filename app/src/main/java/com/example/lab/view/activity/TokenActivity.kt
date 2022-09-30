package com.example.lab.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import com.example.lab.R
import com.example.lab.databinding.ActivityMainBinding
import com.example.lab.databinding.ActivityTokenBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.security.Key
import kotlin.text.StringBuilder

class TokenActivity : AppCompatActivity() {

    // VARIABLE
    private lateinit var bind: ActivityTokenBinding
    private val tokenList: ArrayList<TextInputEditText> = arrayListOf()
    private var token: StringBuilder = StringBuilder()
    private var focusIdx:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityTokenBinding.inflate(layoutInflater)
        setContentView(bind.root)

        tokenList.add(bind.tokenEditText1)
        tokenList.add(bind.tokenEditText2)
        tokenList.add(bind.tokenEditText3)
        tokenList.add(bind.tokenEditText4)
        tokenList.add(bind.tokenEditText5)
        tokenList.add(bind.tokenEditText6)

        addEditTextEvent()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == KeyEvent.KEYCODE_DEL) focusToPrevious()

        return true
    }

    private val textWatcher = object : TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if(p0!!.isEmpty()) focusToPrevious()
            else {
                focusToNext()
                return
            }
        }

        override fun afterTextChanged(p0: Editable?) {
        }

    }

    private fun addEditTextEvent() {
        for (i in 0 until tokenList.size){
            Log.i("이벤트 등록", tokenList[i].toString())

            tokenList[i].addTextChangedListener(textWatcher)
        }
    }

    private fun focusToPrevious(){
        if(focusIdx == 0) return

        tokenList[--focusIdx].requestFocus()
    }

    private fun focusToNext(){
        if(focusIdx == tokenList.size-1) {
            Toast.makeText(applicationContext, "토큰 입력 완료", Toast.LENGTH_SHORT).show()
            return
        }

        tokenList[++focusIdx].requestFocus()
    }
}