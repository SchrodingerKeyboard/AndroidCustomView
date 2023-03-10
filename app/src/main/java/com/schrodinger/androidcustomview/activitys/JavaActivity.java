package com.schrodinger.androidcustomview.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.schrodinger.androidcustomview.R;

public class JavaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_java);
        WindowManager wm1 = getWindow().getWindowManager();
        Log.d("WINDOW_SERVICE", "window.windowManager:"+wm1);
        WindowManager wm2 = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        Log.d("WINDOW_SERVICE", "getSystemService(Context.WINDOW_SERVICE):"+wm2);
        WindowManager wm3 = (WindowManager)getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Log.d("WINDOW_SERVICE", "applicationContext.getSystemService(Context.WINDOW_SERVICE):"+wm3);
        WindowManager wm4 = (WindowManager)getBaseContext().getSystemService(Context.WINDOW_SERVICE);
        Log.d("WINDOW_SERVICE", "baseContext.getSystemService(Context.WINDOW_SERVICE):"+wm4);


    }
}