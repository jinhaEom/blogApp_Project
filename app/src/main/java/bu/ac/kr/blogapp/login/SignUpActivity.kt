package bu.ac.kr.blogapp.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import bu.ac.kr.blogapp.R
import bu.ac.kr.blogapp.databinding.ActivityLoginBinding
import bu.ac.kr.blogapp.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
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
    private fun cancel() {
        val signupCancel = findViewById<Button>(R.id.signupCancel)
        signupCancel.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }


    }
}
