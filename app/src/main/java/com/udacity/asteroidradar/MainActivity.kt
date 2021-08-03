package com.udacity.asteroidradar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

//import com.udacity.asteroidradar.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    //    private lateinit var bind: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        bind = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }
}
