package com.example.lab.view.viewinitializer

import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

interface ViewInitializer {
    fun init(fragment: Fragment, bind:ViewDataBinding)
}