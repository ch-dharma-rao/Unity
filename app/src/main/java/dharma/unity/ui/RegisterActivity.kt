package dharma.unity.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import dharma.unity.R
import dharma.unity.utils.login
import dharma.unity.utils.toast
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mAuth = FirebaseAuth.getInstance()

        button_register.setOnClickListener {
            val email = reg_text_email.text.toString().trim()
            val password = reg_text_password.text.toString().trim()

            if (email.isEmpty()) {
                reg_text_email.error = "Email Required"
                reg_text_email.requestFocus()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                reg_text_email.error = "Valid Email Required"
                reg_text_email.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty() || password.length < 6) {
                reg_text_password.error = "6 char password required"
                reg_text_password.requestFocus()
                return@setOnClickListener
            }

            registerUser(email, password)


        }

        text_view_login.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        }
    }

    private fun registerUser(email: String, password: String) {
        reg_progressbar.visibility = View.VISIBLE
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                reg_progressbar.visibility = View.GONE
                if (task.isSuccessful) {
                    login()
                } else {
                    task.exception?.message?.let {
                        toast(it)
                    }
                }
            }

    }

}