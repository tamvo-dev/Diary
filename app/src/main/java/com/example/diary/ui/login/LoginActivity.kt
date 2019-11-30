package com.example.diary.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.diary.R
import com.example.diary.ui.main.MainActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
        }

        login_btn_login.setOnClickListener(this)
        login_btn_forgot_password.setOnClickListener(this)
        login_btn_signup.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
       when (v?.id){

           R.id.login_btn_login -> {
               login_progressbar.visibility = View.VISIBLE
               val email =  login_user.text.toString().trim()
               val password = login_passwork.text.toString().trim()

               if(password.length < 6){
                   Toast.makeText(this,"Password qua ngan!", Toast.LENGTH_SHORT).show()
                   login_progressbar.visibility = View.GONE
                   return
               }
               if (!email.contains("@")){
                   Toast.makeText(this, "Email khong hop le!", Toast.LENGTH_SHORT).show()
                   login_progressbar.visibility = View.GONE
                   return
               }
               val auth = FirebaseAuth.getInstance()
               auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, OnCompleteListener {
                   if (it.isSuccessful){
                       Toast.makeText(this, "Log in thanh cong!", Toast.LENGTH_SHORT).show()
                       startActivity(Intent(this, MainActivity::class.java))
                   } else {
                       Toast.makeText(this, "Email hoac password bi sai!", Toast.LENGTH_SHORT).show()
                   }
               })
               login_progressbar.visibility = View.GONE
           }
       }
    }

}
