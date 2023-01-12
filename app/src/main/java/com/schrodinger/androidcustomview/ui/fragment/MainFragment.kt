package com.schrodinger.androidcustomview.ui.fragment

import androidx.fragment.app.activityViewModels
import com.androiddev.common.ui.BaseViewModelFragment
import com.schrodinger.androidcustomview.R
import com.schrodinger.androidcustomview.databinding.ActivityMainBinding

class MainFragment : BaseViewModelFragment<MainViewModel,ActivityMainBinding>() {

    override fun getLayoutId() = R.layout.activity_main

    override val viewModel: MainViewModel by activityViewModels()
}