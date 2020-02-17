package br.edu.infnet.instagrom

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.graphics.get
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val REQUEST_IMAGE_CAPTURE_CODE=71
    var originalBitmap:Bitmap? = null
    var currentBitmap:Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpListeners()
    }

    private fun setUpListeners() {
        btnPhoto.setOnClickListener {
            capturePhoto()
        }

        mirror_cardbutton.setOnClickListener {
            currentBitmap?.let {
                val flipped = it.flip()
                photo_imageview.setImageBitmap(flipped)
                currentBitmap = flipped
            }

        }

    }

    fun capturePhoto() {
        val photoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (photoIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(photoIntent, REQUEST_IMAGE_CAPTURE_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode== Activity.RESULT_OK){
            if(requestCode==REQUEST_IMAGE_CAPTURE_CODE){
                data?.let {
                    val thumbnail:Bitmap=it.getParcelableExtra("data")
                    currentBitmap = thumbnail
                    originalBitmap = thumbnail
                    photo_imageview.setImageBitmap(thumbnail)
                }
            }
        }

    }
}

// extensão para Bitmap
fun Bitmap.flip(): Bitmap {
    val conf = Bitmap.Config.ARGB_8888 // see other conf types
    val flipped = Bitmap.createBitmap(width, height, conf) //

    for (x in 0..width/2){
      for (y in 0 until height){
          val pixel = getPixel(width-1-x, y)
          flipped.setPixel(width-1-x, y, getPixel(x, y))
          flipped.setPixel(x, y, pixel)
      }
    }
    return flipped
}

