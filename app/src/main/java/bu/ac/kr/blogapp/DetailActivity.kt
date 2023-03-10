package bu.ac.kr.blogapp
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import bu.ac.kr.blogapp.data.BlogModel
import bu.ac.kr.blogapp.data.DBKey.Companion.DB_BLOG
import bu.ac.kr.blogapp.databinding.ActivityDetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class DetailActivity : AppCompatActivity() {
    private var selectedUri: Uri? = null
    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }
    private val storage: FirebaseStorage by lazy {
        Firebase.storage
    }
    private val blogDB: DatabaseReference by lazy {
        Firebase.database.reference.child(DB_BLOG)
    }
    private lateinit var binding : ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)

        setContentView(binding.root)


        binding.coverImageView.setOnClickListener {
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
        binding.saveButton.setOnClickListener {
            val title = binding.descriptionTextView.text.toString()
            val content = binding.reviewEditText.text.toString()
            val userId = auth.currentUser!!.uid
            showProgress()
            if (selectedUri != null) {
                val photoUri = selectedUri ?: return@setOnClickListener
                uploadPhoto(photoUri,
                    successHandler = {uri ->
                        uploadBlog(0,userId,uri,title,content)
                    },
                    errorHandler = {
                        Toast.makeText(this, "?????? ???????????? ?????????????????????.", Toast.LENGTH_SHORT).show()
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
        blogDB.push().setValue(model)

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
                    Toast.makeText(this, "????????? ??????????????????.", Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(this, "????????? ???????????? ???????????????.", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                Toast.makeText(this, "????????? ???????????? ???????????????.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun showPermissionContextPopup() {  //????????? ??????????????? ?????? ??????
        AlertDialog.Builder(this)
            .setTitle("????????? ???????????????.")
            .setMessage("????????? ???????????? ?????? ???????????????.")
            .setPositiveButton("??????") { _, _ ->
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1010)
            }
            .create()
            .show()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val imm: InputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        return super.dispatchTouchEvent(ev)

    }

}