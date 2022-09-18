package com.example.lab.view.activity

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.lab.R
import com.example.lab.databinding.ActivityMainBinding
import com.example.lab.view.fragment.*

class MainActivity : AppCompatActivity() {
    private lateinit var bind:ActivityMainBinding

    private var homeFragment: HomeFragment? = null
    private var timeTableFragment:TimeTableFragment? = null
    private var reservFragment:ReservFragment? = null
    private var profileFragment: ProfileFragment? = null

    private var fragmentMap: HashMap<Int, Fragment?>? = hashMapOf(
        R.id.menu_home to homeFragment,
        R.id.menu_timetable to timeTableFragment,
        R.id.menu_reserv to reservFragment,
        R.id.menu_profile to profileFragment
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)

        initBottomNavibar()
        eventAdder()
    }

    private fun eventAdder(){
        bind.reservResultBtn.setOnClickListener(View.OnClickListener {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.frameLayout, ReservResultFragment())
                .addToBackStack(null)
                .commit()
        })
    }

    private fun initBottomNavibar() {
        bind.bottomNavbar.run {
            setOnNavigationItemSelectedListener {
                var fragment = when (it.itemId) {
                    R.id.menu_home -> changetFragment(R.id.menu_home)
                    R.id.menu_timetable -> changetFragment(R.id.menu_timetable)
                    R.id.menu_reserv -> changetFragment(R.id.menu_reserv)
                    R.id.menu_profile -> changetFragment(R.id.menu_profile)
                    else -> changetFragment(R.id.menu_home)
                }

                // OnNavigationItemSelectedListner의 반환 값 (람다 함수 형식이라 마지막 라인이 반환 값이 됨)
                true
            }
            selectedItemId = R.id.menu_home
        }
    }

    private fun changetFragment(fragmentId: Int){
        fragmentMap?.forEach { (key, value) ->
            if(key == fragmentId){
                if(fragmentMap!![key] == null){
                    fragmentMap!![key] = fragmentFactory(fragmentId)
                    addFragment(fragmentMap!![key])
                }
                showFragment(fragmentMap!![key])
            }
            else hideFragment(fragmentMap!![key])
        }
    }

    private fun fragmentFactory(fragmentId: Int): Fragment {
        return when (fragmentId) {
            R.id.menu_home -> HomeFragment()
            R.id.menu_timetable -> TimeTableFragment()
            R.id.menu_reserv -> ReservFragment()
            R.id.menu_profile -> ProfileFragment()
            else -> HomeFragment()
        }
    }

    // Fragment 변경
    private fun<T: Fragment> addFragment(fragment: T?) {
        // 이전 버전까지 호환 가능하도록 getSupportFragmentManager() 사용
        fragment?.let {
            supportFragmentManager
                .beginTransaction() // 프래그먼트 변경을 위한 트랜잭션을 시작
                .add(R.id.frameLayout, it) // FrameLayout에 전달 받은 프래그먼트로 교체
                .commit() // 변경 사항 적용
        }
    }

    private fun<T: Fragment> showFragment(fragment: T?){
        fragment?.let {
            supportFragmentManager
                .beginTransaction()
                .show(it)
                .commit()
        }
    }

    private fun<T: Fragment> hideFragment(fragment: T?){
        fragment?.let {
            supportFragmentManager
                .beginTransaction()
                .hide(it)
                .commit()
        }
    }
}