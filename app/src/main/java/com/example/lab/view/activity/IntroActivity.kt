package com.example.lab.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.ViewFlipper
import com.airbnb.lottie.LottieAnimationView
import com.example.lab.R

class IntroActivity : AppCompatActivity(), View.OnTouchListener{

    private lateinit var learnMoreBtn:LottieAnimationView
    private lateinit var vFlipper:ViewFlipper
    private lateinit var indexes:List<LinearLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        vFlipper = findViewById(R.id.viewFlipper)
        learnMoreBtn = findViewById(R.id.learnMoreBtn)

        vFlipper.setOnTouchListener(this)
        learnMoreBtn.setOnClickListener {
            val intent = Intent(applicationContext, LoginActivity::class.java)

            startActivity(intent) //intent 에 명시된 액티비티로 이동
            finish() //현재 액티비티 종료
        }
    }

    private var downX: Float = 0f
    private var upX: Float = 0f
    private var page:Int = 1

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if(v != vFlipper) return false
        if(event?.action == MotionEvent.ACTION_DOWN){
            downX = event.x
        }
        else if(event?.action == MotionEvent.ACTION_UP){
            upX = event.x

            if(page >= 5) return false

            if(upX < downX){ // 오른쪽로 슬라이드
                vFlipper.inAnimation = AnimationUtils.loadAnimation(this, R.anim.flipper_right_in)
                vFlipper.outAnimation = AnimationUtils.loadAnimation(this, R.anim.flipper_left_out)
                vFlipper.showNext()
                page++
            }
        }
        return true
    }
}