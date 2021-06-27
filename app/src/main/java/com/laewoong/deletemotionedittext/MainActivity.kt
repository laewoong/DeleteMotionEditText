package com.laewoong.deletemotionedittext

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.laewoong.deletemotionedittext.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.edittextDeleteMotion.setOnEndDeleteMotionListener {
            Log.i(MainActivity::class.simpleName, "Called End Motion Callback")
        }
    }
}