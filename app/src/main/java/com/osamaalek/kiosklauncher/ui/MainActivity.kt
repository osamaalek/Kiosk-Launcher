package com.osamaalek.kiosklauncher.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.osamaalek.kiosklauncher.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.findFragmentById(R.id.fragmentContainerView) is AppsListFragment)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, HomeFragment()).commit()
    }
}