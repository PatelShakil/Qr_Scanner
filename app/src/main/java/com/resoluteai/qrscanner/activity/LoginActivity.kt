package com.resoluteai.qrscanner.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.resoluteai.qrscanner.R
import com.resoluteai.qrscanner.databinding.ActivityLoginBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseApp.initializeApp(this)
        supportActionBar?.title = "Login"
        val auth = FirebaseAuth.getInstance()

        if(auth.uid != null){
            startActivity(Intent(this,MainActivity::class.java))
        }
        binding.signupText.setOnClickListener{
            startActivity(Intent(this,SignupActivity::class.java))
        }
        binding.loginBtn.setOnClickListener {
            auth.signInWithEmailAndPassword(binding.emailLogin.text.toString().trim(),
                binding.passLogin.text.toString().trim())
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        startActivity(Intent(this,MainActivity::class.java))
                    }else{
                        toast(it.exception?.message.toString())
                    }
                }
        }
    }

    fun Context.toast(message : String){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }
}