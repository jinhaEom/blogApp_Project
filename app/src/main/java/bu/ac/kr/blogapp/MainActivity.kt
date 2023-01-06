package bu.ac.kr.blogapp

import android.content.Intent
import android.graphics.Insets.add
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.core.graphics.Insets.add
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bu.ac.kr.blogapp.adapter.BlogAdapter
import bu.ac.kr.blogapp.data.BlogModel
import bu.ac.kr.blogapp.data.DBKey.Companion.DB_BLOG
import bu.ac.kr.blogapp.databinding.ActivityMainBinding
import com.google.android.gms.common.util.WorkSourceUtil.add
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    private lateinit var blogDB : DatabaseReference
    private lateinit var userDB : DatabaseReference
    private lateinit var blogAdapter : BlogAdapter

    private val blogList = mutableListOf<BlogModel>()

    private val listener = object : ChildEventListener{
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val blogModel = snapshot.getValue(BlogModel::class.java)
            blogModel ?: return
            blogList.add(blogModel)
            blogAdapter.submitList(blogList)
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?){}
        override fun onChildRemoved(snapshot: DataSnapshot) {}
        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
        override fun onCancelled(error: DatabaseError) {}

    }
    private var binding : ActivityMainBinding? = null

    private val auth : FirebaseAuth by lazy{
        Firebase.auth
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        blogList.clear()
        blogDB = Firebase.database.reference.child(DB_BLOG)



        blogAdapter = BlogAdapter(onItemClicked = {
            //TODO
        })
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView_List)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = blogAdapter


        findViewById<ImageView>(R.id.btn_add).setOnClickListener {
            val intent = Intent(this, DetailActivity::class.java)
            startActivity(intent)
        }
        blogDB.addChildEventListener(listener)

    }
    override fun onResume() {
        super.onResume()

        blogAdapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        blogDB.removeEventListener(listener)
    }
}