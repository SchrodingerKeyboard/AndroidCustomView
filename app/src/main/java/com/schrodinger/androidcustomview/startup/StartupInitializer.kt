package com.etonedu.wechatvoip.startup

import android.content.Context
import android.util.Log
import androidx.startup.Initializer
import com.androiddev.common.startup.CommonInitialzer
import com.androiddev.common.startup.initializerTag

class StartupInitializer : Initializer<String> {
    override fun create(context: Context): String {
        Log.d(initializerTag, "StartupInitializer create")
        return "test"
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        Log.d(initializerTag, "dependencies")
        //从上到下按顺序执行初始化。
        return mutableListOf(
            CommonInitialzer::class.java,
            ObjectBoxInitialzer::class.java,
        )
    }
}