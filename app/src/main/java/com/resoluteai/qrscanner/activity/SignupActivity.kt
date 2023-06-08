package com.resoluteai.qrscanner.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.resoluteai.qrscanner.models.User
import com.resoluteai.qrscanner.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Signup"

        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()

        binding.loginText.setOnClickListener{
            startActivity(Intent(this,LoginActivity::class.java))
        }

        binding.signupBtn.setOnClickListener {
            auth.createUserWithEmailAndPassword(binding.emailLogin.text.toString().trim(),binding.passLogin.text.toString().trim())
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        db.collection("users")
                            .document(auth.uid.toString())
                            .set(User(auth.uid.toString(),
                            binding.nameLogin.text.toString().trim(),
                            binding.emailLogin.text.toString().trim(),
                            binding.passLogin.text.toString().trim()))
                            .addOnCompleteListener {dbTask ->
                                if(dbTask.isSuccessful){
                                    startActivity(Intent(this,MainActivity::class.java))
                                }else{
                                    Toast.makeText(this, dbTask.exception?.message.toString(), Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                }
        }
    }
}