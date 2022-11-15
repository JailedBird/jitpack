package com.jailedbird.jitpack

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
//import com.jailedbird.jitpack.lib1.Lib1

class MainActivity : AppCompatActivity() {
    companion object{
        private const val TAG = "MainActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Log.d(TAG, "onCreate: ${Lib1.info}")

    }
}