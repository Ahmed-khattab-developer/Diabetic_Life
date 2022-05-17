package com.rx.diabeticlife.doctor.activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.rx.diabeticlife.R
import com.rx.diabeticlife.SessionManagement
import com.rx.diabeticlife.doctor.pojo.Doctor
import com.squareup.picasso.Picasso
import java.util.*

class DoctorCompleteDataActivity : AppCompatActivity() {

    private var btnSelect: Button? = null

    private var imageView: ImageView? = null
    private var name: EditText? = null

    private var selectedImageUri: Uri? = null

    private var generatedFilePath: String? = null

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference
    private lateinit var auth: FirebaseAuth

    private lateinit var iSessionManagement: SessionManagement

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_complete_data)
        iSessionManagement = SessionManagement(this)

        btnSelect = findViewById(R.id.btnChoose)
        imageView = findViewById(R.id.imgView)
        name = findViewById(R.id.name)

        auth = FirebaseAuth.getInstance()

        firebaseDatabase = FirebaseDatabase.getInstance()

        databaseReference = firebaseDatabase.getReference("doctor").child(auth.uid.toString())

        storage = FirebaseStorage.getInstance()
        storageReference = storage.getReference("doctor")


        btnSelect?.setOnClickListener {
            selectImage()
        }

        findViewById<Button>(R.id.button_save).setOnClickListener {
            when {
                name?.text?.trim()?.isEmpty() == true -> {
                    Toast.makeText(applicationContext, "Doctor Name Required", Toast.LENGTH_SHORT)
                        .show()
                }
                selectedImageUri == null -> {
                    Toast.makeText(applicationContext, "Doctor Image Required", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {
                    uploadImage()
                }
            }
        }
    }

    private fun selectImage() {
        ImagePicker.with(this)
            .createIntent { intent -> startForProfileImageResult.launch(intent) }
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val resultCode = it.resultCode
            val data = it.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    selectedImageUri = data?.data!!

                    Picasso.with(this).load(selectedImageUri).into(imageView)

                }
                ImagePicker.RESULT_ERROR -> {
                }
                else -> {
                }
            }
        }

    // UploadImage method
    private fun uploadImage() {
        if (selectedImageUri != null) {

            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Uploading...")
            progressDialog.show()

            val ref: StorageReference = storageReference
                .child("images/" + UUID.randomUUID().toString())

            ref.putFile(selectedImageUri!!)
                .addOnSuccessListener {
                    progressDialog.dismiss()

                    ref.downloadUrl.addOnSuccessListener { uri ->
                        generatedFilePath = uri.toString()
                        addDataToFirebase(name?.text.toString(), generatedFilePath!!)
                    }

                }
                .addOnFailureListener { e ->
                    progressDialog.dismiss()
                    Toast
                        .makeText(
                            this@DoctorCompleteDataActivity,
                            "Failed " + e.message, Toast.LENGTH_SHORT
                        )
                        .show()
                }
                .addOnProgressListener { taskSnapshot ->
                    val progress = (100.0
                            * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount)
                    progressDialog.setMessage("Uploaded " + progress.toInt() + "%")
                }
        }
    }

    private fun addDataToFirebase(name: String, image: String?) {

        val doctor = Doctor(auth.uid.toString(), name, image!!)

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                databaseReference.setValue(doctor)

                iSessionManagement.createLoginSession(
                    true, auth.uid.toString(),
                    "doctor", doctor.name, "", "", doctor.image!!
                )

                val intent = Intent(this@DoctorCompleteDataActivity, MainDoctorActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@DoctorCompleteDataActivity, "Fail to add data $error", Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

}