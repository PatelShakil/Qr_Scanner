package com.resoluteai.qrscanner.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.resoluteai.qrscanner.R
import com.resoluteai.qrscanner.adapter.QRListAdapter
import com.resoluteai.qrscanner.databinding.ActivityMainBinding
import com.resoluteai.qrscanner.models.QRCode
import io.github.g00fy2.quickie.QRResult
import io.github.g00fy2.quickie.ScanQRCode
import java.util.Date

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var qrResult: String
    var db = FirebaseFirestore.getInstance()
    var auth = FirebaseAuth.getInstance()
    val scanQrCodeLauncher = registerForActivityResult(ScanQRCode()) { result ->
        // handle QRResult
        when (result) {
            is QRResult.QRSuccess -> {
                qrResult = result.content.rawValue
                db.collection("qr")
                    .document()
                    .set(QRCode(qrResult,auth.uid.toString(), System.currentTimeMillis()))
                    .addOnSuccessListener {
                        Toast.makeText(this, "QR Code saved", Toast.LENGTH_SHORT).show()
                    }
            }
            else -> {
                Toast.makeText(this, "Does not detected any QR Code", Toast.LENGTH_SHORT).show()

            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db.collection("users")
            .document(auth.uid!!)
            .get()
            .addOnSuccessListener {
                supportActionBar?.title = it.get("name").toString() + " | Your QR List"
            }
        val list = ArrayList<QRCode>()
        db.collection("qr")
            .whereEqualTo("uid", auth.uid.toString())
            .addSnapshotListener { value, error ->
                if (error != null) {
                    return@addSnapshotListener
                }
                if (value != null) {
                    list.clear()
                    if (!value.documents.isEmpty()) {
                        for (i in value.documents) {
                            val data = i.toObject(QRCode::class.java)
                            if (data != null) {
                                list.add(data)
                            }
                        }
                    }
                }
                list.sortBy {
                    it.time
                }
                if (list.isNotEmpty()) {
                    val adapter = QRListAdapter(list)
                    binding.qrList.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
            }


        binding.startScanning.setOnClickListener {
            scanQrCodeLauncher.launch(null)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}