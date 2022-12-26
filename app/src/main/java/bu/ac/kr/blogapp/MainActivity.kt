package bu.ac.kr.blogapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toolbar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import bu.ac.kr.blogapp.data.BlogViewModel
import bu.ac.kr.blogapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mBlogViewModel : BlogViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViewModel()

        mBlogViewModel = ViewModelProvider(this).get(BlogViewModel::class.java)
    }
    private fun initViewModel() {
        mBlogViewModel =
            ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(BlogViewModel::class.java)
    }

}