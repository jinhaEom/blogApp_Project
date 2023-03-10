package bu.ac.kr.blogapp.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import bu.ac.kr.blogapp.MainActivity
import bu.ac.kr.blogapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    val binding by lazy { bu.ac.kr.blogapp.databinding.ActivityLoginBinding.inflate(layoutInflater)}
    private var auth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = Firebase.auth
        binding.btLogin.setOnClickListener {
            signIn(binding.appId.text.toString(),binding.appPw.text.toString())
        }
        binding.signUpMake.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
    fun signIn(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth?.signInWithEmailAndPassword(email, password)
                ?.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            baseContext, "로그인에 성공 하였습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                        moveMainPage(auth?.currentUser)
                    } else {
                        Toast.makeText(
                            baseContext, "로그인에 실패 하였습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
    public override fun onStart() {
        super.onStart()
        moveMainPage(auth?.currentUser)

    }
    // 유저정보 넘겨주고 메인 액티비티 호출
    private fun moveMainPage(user: FirebaseUser?){
        if(user!= null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }
}