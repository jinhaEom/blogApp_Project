package bu.ac.kr.blogapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import bu.ac.kr.blogapp.data.BlogViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var mBlogViewModel : BlogViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViewModel()
    }
    private fun initViewModel() {
        mBlogViewModel =
            ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(BlogViewModel::class.java)
    }
}