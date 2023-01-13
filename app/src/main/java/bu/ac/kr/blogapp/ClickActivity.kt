package bu.ac.kr.blogapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.room.Room
import bu.ac.kr.blogapp.data.Blog
import bu.ac.kr.blogapp.data.BlogDatabase
import bu.ac.kr.blogapp.data.BlogModel
import bu.ac.kr.blogapp.databinding.ActivityClickBinding
import bu.ac.kr.blogapp.databinding.ClickToolbarBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ktx.ChildEvent
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageTask.SnapshotBase


class ClickActivity : AppCompatActivity() {

    private lateinit var binding : ActivityClickBinding
    private lateinit var db : BlogDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClickBinding.inflate(layoutInflater)
        setContentView(binding.root)




        db = Room.databaseBuilder(
            applicationContext,
            BlogDatabase::class.java,
            "BlogDB"
        ).build()


        val model = intent.getParcelableExtra<BlogModel>("blog")
        binding.descriptionTextView.text = model?.title
        binding.reviewEditText.text = model?.content

        Glide.with(binding.coverImageView.context)
            .load(model?.imageUrl.orEmpty())
            .into(binding.coverImageView)

        binding.btnBack.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

    }

}