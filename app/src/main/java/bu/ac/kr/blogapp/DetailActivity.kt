package bu.ac.kr.blogapp

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProviders
import bu.ac.kr.blogapp.data.BlogModel
import bu.ac.kr.blogapp.data.BlogViewModel
import bu.ac.kr.blogapp.data.DBKey.Companion.DB_BLOG
import com.google.android.gms.common.internal.Objects.ToStringHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.StringReader
import java.io.StringWriter
import java.lang.ref.ReferenceQueue

class DetailActivity : AppCompatActivity() {
    private var selectedUri: Uri? = null
    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }
    private lateinit var blogModel : BlogModel
    private val storage: FirebaseStorage by lazy {
        Firebase.storage
    }
    private val blogDB: DatabaseReference by lazy {
        Firebase.database.reference.child(DB_BLOG)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_detail)


        findViewById<ImageView>(R.id.coverImageView).setOnClickListener {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    startContentProvider()
                }
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                    showPermissionContextPopup()
                }
                else -> {
                    requestPermissions(
                        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                        1010
                    )
                }
            }
        }
        findViewById<Button>(R.id.saveButton).setOnClickListener {
            val title = findViewById<EditText>(R.id.descriptionTextView).text.toString()
            val content = findViewById<EditText>(R.id.reviewEditText).text.toString()
            val userId = auth.currentUser!!.uid
            showProgress()

            if (selectedUri != null) {
                val photoUri = selectedUri ?: return@setOnClickListener
                uploadPhoto(photoUri,
                    successHandler = {uri ->
                        uploadBlog(0,userId,uri,title,content)
                    },
                    errorHandler = {
                        Toast.makeText(this, "사진 업로드에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                        hideProgress()
                    }
                )
            } else {
                uploadBlog(0,userId,"",title,content)
            }

        }


    }



    private fun uploadPhoto(uri: Uri, successHandler: (String) -> Unit, errorHandler: () -> Unit) {
        val fileName = "${System.currentTimeMillis()}.png"
        storage.reference.child("blog/photo").child(fileName)
            .putFile(uri)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    storage.reference.child("blog/photo").child(fileName).downloadUrl
                        .addOnSuccessListener { uri ->
                            successHandler(uri.toString())
                        }.addOnFailureListener {
                            errorHandler()
                        }
                } else {
                    errorHandler()
                }
            }
    }

    private fun uploadBlog(id:Long, userId: String, imageUrl: String, title: String, content: String) {
        val model = BlogModel(id, userId, imageUrl, title, content , System.currentTimeMillis())
        blogDB.push().setValue(model) //TODO

        hideProgress()
        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1010 ->
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startContentProvider()
                } else {
                    Toast.makeText(this, "권한을 거부했습니다.", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun startContentProvider() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, 2020)
    }

    private fun showProgress() {
        findViewById<ProgressBar>(R.id.progressBar).isVisible=true

    }

    private fun hideProgress() {
        findViewById<ProgressBar>(R.id.progressBar).isVisible=false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            return
        }
        when (requestCode) {
            2020 -> {
                val uri = data?.data
                if (uri != null) {
                    findViewById<ImageView>(R.id.coverImageView).setImageURI(uri)
                    selectedUri = uri
                } else {
                    Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showPermissionContextPopup() {  //사진에 불러올때에 대한 권한
        AlertDialog.Builder(this)
            .setTitle("권한이 필요합니다.")
            .setMessage("사진을 가져오기 위해 필요합니다.")
            .setPositiveButton("동의") { _, _ ->
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1010)

            }
            .create()
            .show()
    }

}


