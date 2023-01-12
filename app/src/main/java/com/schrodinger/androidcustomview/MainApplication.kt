package com.schrodinger.androidcustomview

import android.content.Context
import android.util.Log
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.startup.AppInitializer
import com.androiddev.common.CommonApplication
import com.blankj.utilcode.util.LogUtils
import com.etonedu.wechatvoip.startup.StartupInitializer

class MainApplication : CommonApplication() {

    private val tag = MainApplication::class.java.simpleName

    companion object {
        lateinit var Instance: MainApplication
    }

    override fun onCreate() {
        super.onCreate()
        Instance = this
        val timeStart = System.currentTimeMillis()
        val result =
            AppInitializer.getInstance(this).initializeComponent(StartupInitializer::class.java)
        LogUtils.d(
            tag,
            "initializeComponent result:" + result + "\ttime:${System.currentTimeMillis() - timeStart}"
        )
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

    override fun onTerminate() {
        super.onTerminate()
    }
}