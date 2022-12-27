package bu.ac.kr.blogapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

import androidx.lifecycle.ViewModelProviders
import bu.ac.kr.blogapp.adapter.BlogAdapter
import bu.ac.kr.blogapp.data.BlogModel
import bu.ac.kr.blogapp.data.BlogViewModel
import bu.ac.kr.blogapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var blogViewModel: BlogViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        findViewById<ImageView>(R.id.btn_add).setOnClickListener {
            val intent = Intent(this, DetailActivity::class.java)
            startActivity(intent)
        }

    }
}




