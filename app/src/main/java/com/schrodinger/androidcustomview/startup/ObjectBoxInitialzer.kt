package com.etonedu.wechatvoip.startup

import android.content.Context
import androidx.startup.Initializer
import com.androiddev.common.startup.initializerTag
import com.blankj.utilcode.util.LogUtils
import com.schrodinger.androidcustomview.BuildConfig
import io.objectbox.BoxStore
import io.objectbox.android.Admin
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class TestObjectBoxEntity(@Id var id: Long = 0, var name: String? = null)

var boxStore: BoxStore? = null

class ObjectBoxInitialzer : Initializer<Unit> {

    override fun create(context: Context) {
        LogUtils.d(initializerTag, "ObjectBoxInitialzer create")
//        try {
//            boxStore = MyObjectBox.builder()
//                .androidContext(context.applicationContext)
//                .build()
//            if (BuildConfig.DEBUG) {
//                val started = Admin(boxStore).start(context)
//                LogUtils.d(initializerTag, "Admin started :$started")
//            }
//        } catch (e: Exception) {
//            LogUtils.e(initializerTag, e.toString())
//        }
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}