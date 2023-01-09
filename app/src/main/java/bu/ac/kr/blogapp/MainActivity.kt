package bu.ac.kr.blogapp

import android.content.Intent
import android.graphics.Insets.add
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.graphics.Insets.add
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bu.ac.kr.blogapp.adapter.BlogAdapter
import bu.ac.kr.blogapp.data.BlogModel
import bu.ac.kr.blogapp.data.DBKey.Companion.DB_BLOG
import bu.ac.kr.blogapp.databinding.ActivityMainBinding
import com.google.android.gms.common.util.WorkSourceUtil.add
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView : NavigationView
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
            //TODO
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
        setContentView(R.layout.activity_main)


        val toolbar : Toolbar = findViewById(R.id.main_layout_toolbar)



        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 드로어를 꺼낼 홈 버튼 활성화
        supportActionBar?.setHomeAsUpIndicator(R.drawable.navi_menu)

        drawerLayout = findViewById(R.id.main_drawer_layout)



        blogList.clear()
        blogDB = Firebase.database.reference.child(DB_BLOG)





        blogAdapter = BlogAdapter(onItemClicked = {
            //TODO
        })
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView_List)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = blogAdapter


//        findViewById<ImageView>(R.id.btn_add).setOnClickListener {
//            val intent = Intent(this, DetailActivity::class.java)
//            startActivity(intent)
//        }
        blogDB.addChildEventListener(listener)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu,menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent = Intent(this, DetailActivity::class.java)
        return when(item.itemId){
            R.id.btn_add -> {
                startActivity(intent)
                true
            }
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }

        }

        return super.onOptionsItemSelected(item)
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