package bu.ac.kr.blogapp.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import bu.ac.kr.blogapp.R
import bu.ac.kr.blogapp.data.DBKey
import bu.ac.kr.blogapp.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    private var auth : FirebaseAuth? = null

    private val binding by lazy { ActivitySignupBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        setContentView(binding.root)

        binding.signupOk.setOnClickListener {
            createAccount(binding.emailEditText.text.toString() , binding.passwordEditText.text.toString(), binding.passwordCheck.text.toString())
        }
        cancel()
    }
    private fun createAccount(email: String, password: String, passwordCheck: String) {
        val signupOk = findViewById<Button>(R.id.signupOk)

        if (email.isNotEmpty() && password.isNotEmpty()) {
            if (password != passwordCheck) {
                Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()

            } else {
                auth?.createUserWithEmailAndPassword(email, password)
                    ?.addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            signupOk.isEnabled = true

                            Toast.makeText(
                                this,
                                "회원가입에 성공했습니다. 로그인 버튼을눌러 로그인 해주세요.",
                                Toast.LENGTH_SHORT
                            ).show()

                            finish()
                        }
                    }
            }


        } else {
            Toast.makeText(this, "이메일이 이미 존재하거나 회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show()

        }
    }
//    private fun saveUserName(name : String){
//        val userId = getCurrentUserID()
//        val currentUserDB = userDB.child(auth!!.currentUser!!.uid)
//        val user = mutableMapOf<String, Any>()
//        user["name"] = name
//        currentUserDB.updateChildren(user)
//
//    }


    private fun cancel() {
        val signupCancel = findViewById<Button>(R.id.signupCancel)
        signupCancel.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
