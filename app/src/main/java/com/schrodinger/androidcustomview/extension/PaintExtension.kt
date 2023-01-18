package com.schrodinger.androidcustomview.extension

import android.graphics.Paint

fun Paint.getBaseLine(centerY:Float):Float {
    val fontMetrics = fontMetrics
    val dy = (fontMetrics.bottom - fontMetrics.top)/2.0f - fontMetrics.bottom
    return centerY + dy
}