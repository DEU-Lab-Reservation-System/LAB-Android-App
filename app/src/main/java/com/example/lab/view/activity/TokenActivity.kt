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
    private var token: CharArray = CharArray(6) {' '}
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

    private val textWatcher = object : TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if(p0!!.isEmpty()) return

            token[focusIdx] = p0!![0]

            if(focusIdx == tokenList.size-1){
                Toast.makeText(applicationContext, "${token[0]}${token[1]}${token[2]}${token[3]}${token[4]}${token[5]}", Toast.LENGTH_SHORT).show()
                return
            }

            focusToNext()
        }

        override fun afterTextChanged(p0: Editable?) {

        }

    }

    /** 키 다운 이벤트 */
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == KeyEvent.KEYCODE_DEL) focusToPrevious()

        return true
    }

    /** EditText 이벤트 등록 */
    private fun addEditTextEvent() {
        for (i in 0 until tokenList.size){
            tokenList[i].addTextChangedListener(textWatcher)
        }
    }

    /** 이전 Edittext로 포커스 이동 */
    private fun focusToPrevious(){
        if(focusIdx == 0) return

        focusIdx--

        tokenList[focusIdx].requestFocus()
        tokenList[focusIdx].text = null
    }

    /** 다음 Edittext로 포커스 이동 */
    private fun focusToNext(){
        if(focusIdx == tokenList.size-1) return

        tokenList[++focusIdx].requestFocus()
    }
}