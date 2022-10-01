package com.example.lab.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import com.example.lab.R
import com.example.lab.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    // VARIABLE
    private lateinit var bind: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(bind.root)

        bind.nextBtn.setOnClickListener{
            bind.viewFlipper.inAnimation = AnimationUtils.loadAnimation(this, R.anim.flipper_right_in)
            bind.viewFlipper.outAnimation = AnimationUtils.loadAnimation(this, R.anim.flipper_left_out)
            bind.viewFlipper.showNext()
        }
    }
}