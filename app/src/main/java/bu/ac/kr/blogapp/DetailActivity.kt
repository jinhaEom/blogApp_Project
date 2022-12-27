package bu.ac.kr.blogapp

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import bu.ac.kr.blogapp.data.BlogViewModel
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*

class DetailActivity : AppCompatActivity() {
    private val OPEN_GALLERY = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        findViewById<ImageView>(R.id.coverImageView).setOnClickListener { openGallery() }

    }
    private fun openGallery() {
        val intent : Intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")
        startActivityForResult(intent, OPEN_GALLERY)
    }

    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == Activity.RESULT_OK){
            if(requestCode == OPEN_GALLERY){
                var currentImageUrl : Uri? = data?.data

                try{
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,currentImageUrl)
                    findViewById<ImageView>(R.id.coverImageView).setImageBitmap(bitmap)

                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }else {
            Log.d("ActivityResult", "에러발생")
        }
    }
}


