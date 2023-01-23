package bu.ac.kr.blogapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.CheckBox
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bu.ac.kr.blogapp.adapter.BlogAdapter
import bu.ac.kr.blogapp.data.BlogModel
import bu.ac.kr.blogapp.data.DBKey.Companion.DB_BLOG
import bu.ac.kr.blogapp.data.DBKey.Companion.USER_ID
import bu.ac.kr.blogapp.databinding.ActivityDetailBinding
import bu.ac.kr.blogapp.login.LoginActivity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.net.URI


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var blogDB: DatabaseReference
    private lateinit var blogAdapter: BlogAdapter
    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }
    private val blogList = mutableListOf<BlogModel>()
    private val listener = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val blogModel = snapshot.getValue(BlogModel::class.java)

            try {
                if (snapshot.child(USER_ID).value == auth.currentUser!!.uid) {
                    blogModel?.let { blogList.add(it) }
                    Log.e("Listeners", "ChildEventListener-onChildAdded : ${snapshot.value}")
                    blogAdapter.submitList(blogList)
                }
                blogModel ?: return
            } catch (e: NullPointerException) {}
        }
        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

        }
        override fun onChildRemoved(snapshot: DataSnapshot) {}
        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
        override fun onCancelled(error: DatabaseError) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.main_layout_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 드로어를 꺼낼 홈 버튼 활성화
        supportActionBar?.setHomeAsUpIndicator(R.drawable.navi_menu)

        drawerLayout = findViewById(R.id.main_drawer_layout)
        findViewById<NavigationView>(R.id.main_navigationView).setNavigationItemSelectedListener(
            this)
        blogList.clear()
        blogDB = Firebase.database.reference.child(DB_BLOG)



        blogAdapter = BlogAdapter(onItemClicked = {
            val intent = Intent(this, ClickActivity::class.java)
            intent.putExtra("blog", it)
            startActivity(intent)
        })
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView_List)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = blogAdapter

        blogDB.addChildEventListener(listener)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent = Intent(this, DetailActivity::class.java)

        return when (item.itemId) {
            R.id.btn_add -> {
                startActivity(intent)
                true
            }
            R.id.btn_delete -> {
               if(findViewById<CheckBox>(R.id.bookMarkBtn).isChecked){

                   blogDB.push().removeValue()
                   Firebase.database.reference.child(DB_BLOG).child("key").removeValue()
                   Toast.makeText(this,"삭제되었습니다.",Toast.LENGTH_SHORT).show()
               }
                true
            }
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                findViewById<TextView>(R.id.userName).text =
                    auth.currentUser?.email + "${"\n"}님 환영합니다."
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.logout -> {
                val builder = AlertDialog.Builder(this)
                    .setTitle("로그아웃")
                    .setMessage("로그아웃 하시겠습니까?")
                    .setPositiveButton("확인"
                    ) { _, _ ->
                        auth.signOut()
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        this.finish()
                    }
                    .setNegativeButton("취소"
                    ) { _, _ -> }
                builder.show()
            }
            R.id.cal -> {
                val intent = Intent(this, CalendarActivity::class.java)
                startActivity(intent)
            }

        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
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