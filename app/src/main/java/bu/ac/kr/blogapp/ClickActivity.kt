package bu.ac.kr.blogapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import bu.ac.kr.blogapp.data.BlogModel
import bu.ac.kr.blogapp.databinding.ActivityClickBinding
import com.bumptech.glide.Glide


class ClickActivity : AppCompatActivity() {

    private lateinit var binding : ActivityClickBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClickBinding.inflate(layoutInflater)
        setContentView(binding.root)

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