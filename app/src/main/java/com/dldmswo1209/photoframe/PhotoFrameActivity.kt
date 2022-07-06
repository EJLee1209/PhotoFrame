package com.dldmswo1209.photoframe

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.core.net.toUri
import com.dldmswo1209.photoframe.databinding.ActivityPhotoFrameBinding
import java.util.*
import kotlin.concurrent.timer

class PhotoFrameActivity : AppCompatActivity() {
    var mBinding: ActivityPhotoFrameBinding? = null
    val binding get() = mBinding!!
    private val photoList = mutableListOf<Uri>()
    private var currentPosition = 0
    private var timer : Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityPhotoFrameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getPhotoUriFromIntent()

        startTimer()
    }
    private fun getPhotoUriFromIntent(){
        // intent로 넘겨받은 이미지 uri와 리스트 사이즈를 가져옴
        val size = intent.getIntExtra("photoListSize", 0)
        for(i in 0..size){
            intent.getStringExtra("photo$i")?.let {
                photoList.add(it.toUri())
            }
        }
    }

    private fun startTimer(){
        timer = timer(period = 5000){ // 5초에 한번 실행
            runOnUiThread {

                Log.d("PhotoFrame", "5초 지나감")
                val current = currentPosition
                val next = if(photoList.size <= currentPosition + 1) 0 else currentPosition + 1

                binding.backgroundPhotoImageView.setImageURI(photoList[current])

                binding.photoImageView.alpha = 0f // 투명도를 0f로 주면 안보임
                binding.photoImageView.setImageURI(photoList[next]) // 다음 이미지를 가져와서 이미지뷰에 설정
                binding.photoImageView.animate() // 애니메이션 기능
                    .alpha(1.0f) // 투명도 0~1까지
                    .setDuration(1000) // 1초동안
                    .start()

                currentPosition = next
            }
        }
    }
    // 액티비티의 생명주기에 따라 timer를 cancel/start
    override fun onStop() {
        super.onStop()

        Log.d("PhotoFrame", "onStop!! timer cancel")

        timer?.cancel()
    }

    override fun onStart() {
        super.onStart()

        Log.d("PhotoFrame", "onStart!! timer start")

        startTimer()
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d("PhotoFrame", "onDestroy!! timer cancel")

        timer?.cancel()
    }
}