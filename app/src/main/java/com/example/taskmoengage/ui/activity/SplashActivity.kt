package com.example.taskmoengage.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.taskmoengage.databinding.ActivitySplashBinding

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivitySplashBinding
    private lateinit var requestLauncher: ActivityResultLauncher<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        getPermission()
        askForPermission()
    }

    private fun moveToNextScreen() {
        mBinding.ivLogo.scaleX = 1f
        mBinding.ivLogo.scaleY = 1f
        performZoomInAndOutAnimation()
        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()

        }, 2000)
    }

    private fun performZoomInAndOutAnimation() {
        val zoomInAnimation = ScaleAnimation(
            1f, 2f,
            1f, 2f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        zoomInAnimation.duration = 2500

        mBinding.ivLogo.startAnimation(zoomInAnimation)
    }

    private fun getPermission() {
        requestLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                moveToNextScreen()
            } else {
                askForPermission()
            }
        }
    }

    private fun askForPermission() {
        requestLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
    }
}