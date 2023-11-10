package com.ophi.storyapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import com.ophi.storyapp.R
import com.ophi.storyapp.model.SplashScreenViewModel
import com.ophi.storyapp.model.ViewModelFactory

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private val viewModel by viewModels<SplashScreenViewModel> {
        ViewModelFactory.getInstance(this, true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()

        val splashTime = 1000L

        Handler(Looper.getMainLooper()).postDelayed({
            var intent = Intent(this, MainActivity::class.java)
            viewModel.getSession().observe(this) {user ->
                if (!user.isLogin) {
                    intent = Intent(this,LoginActivity::class.java)
                }
            }

            startActivity(intent)
            finish()
        }, splashTime)
    }
}