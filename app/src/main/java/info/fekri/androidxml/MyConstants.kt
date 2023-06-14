package info.fekri.androidxml

import android.Manifest.permission.CAMERA
import android.Manifest.permission.RECORD_AUDIO
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.os.Build

typealias LumaListener = (luma: Double) -> Unit

const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
val REQUIRED_PERMISSIONS =
    mutableListOf(
        CAMERA,
        RECORD_AUDIO
    ).apply {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            add(WRITE_EXTERNAL_STORAGE)
        }
    }.toTypedArray()
