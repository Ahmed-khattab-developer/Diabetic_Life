package com.rx.diabeticlife.doctor.activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.rx.diabeticlife.R
import com.rx.diabeticlife.SessionManagement
import com.rx.diabeticlife.doctor.pojo.ResultData
import com.rx.diabeticlife.trainer.activity.MainTrainerActivity
import com.squareup.picasso.Picasso
import java.util.*

class DoctorResultActivity : AppCompatActivity() {

    private var btnSelect: Button? = null

    private var imageView: ImageView? = null

    private var selectedImageUri: Uri? = null

    private var generatedFilePath: String? = null

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference
    private lateinit var auth: FirebaseAuth

    private lateinit var iSessionManagement: SessionManagement

    var doctorId: String? = null
    var doctorName: String? = null
    var doctorImage: String? = null
    var trainerId: String? = null
    var trainerName: String? = null
    var trainerAge: String? = null
    var trainerGender: String? = null
    var patientId: String? = null
    var patientName: String? = null
    var patientAge: String? = null
    var patientGender: String? = null
    var sugarLevel: String? = null
    var targetLevel: String? = null
    var weight: String? = null
    var height: String? = null
    var date: String? = null
    var type: String? = null
    var requestId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_result)

        doctorId = intent.getStringExtra("doctorId")
        doctorName = intent.getStringExtra("doctorName")
        doctorImage = intent.getStringExtra("doctorImage")
        trainerId = intent.getStringExtra("trainerId")
        trainerName = intent.getStringExtra("trainerName")
        trainerAge = intent.getStringExtra("trainerAge")
        trainerGender = intent.getStringExtra("trainerGender")
        patientId = intent.getStringExtra("patientId")
        patientName = intent.getStringExtra("patientName")
        patientAge = intent.getStringExtra("patientAge")
        patientGender = intent.getStringExtra("patientGender")
        sugarLevel = intent.getStringExtra("sugarLevel")
        targetLevel = intent.getStringExtra("targetLevel")
        weight = intent.getStringExtra("weight")
        height = intent.getStringExtra("height")
        date = intent.getStringExtra("date")
        type = intent.getStringExtra("type")
        requestId = intent.getStringExtra("requestId")

        iSessionManagement = SessionManagement(this)

        btnSelect = findViewById(R.id.btnChoose)
        imageView = findViewById(R.id.imgView)

        auth = FirebaseAuth.getInstance()

        firebaseDatabase = FirebaseDatabase.getInstance()

        databaseReference = firebaseDatabase.getReference("result")

        storage = FirebaseStorage.getInstance()
        storageReference = storage.getReference("result")

        btnSelect?.setOnClickListener {
            selectImage()
        }

        findViewById<Button>(R.id.button_save).setOnClickListener {
            when (selectedImageUri) {
                null -> {
                    Toast.makeText(applicationContext, "Result Image Required", Toast.LENGTH_SHORT)
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

    private fun uploadImage() {
        if (selectedImageUri != null) {

            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Uploading...")
            progressDialog.show()

            val ref: StorageReference =
                storageReference.child("images/" + UUID.randomUUID().toString())

            ref.putFile(selectedImageUri!!)
                .addOnSuccessListener {
                    progressDialog.dismiss()

                    ref.downloadUrl.addOnSuccessListener { uri ->
                        generatedFilePath = uri.toString()
                        addDataToFirebase(generatedFilePath!!)
                    }

                }
                .addOnFailureListener { e ->
                    progressDialog.dismiss()
                    Toast
                        .makeText(
                            this@DoctorResultActivity, "Failed " + e.message, Toast.LENGTH_SHORT
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

    private fun addDataToFirebase(image: String?) {

        val result: ResultData

        if (type == "doctor") {

            result = ResultData(
                requestId!!, doctorId!!, doctorName!!, doctorImage!!, trainerId!!,
                trainerName!!, trainerAge!!, trainerGender!!,
                patientId!!, patientName!!, patientAge!!, patientGender!!,
                sugarLevel!!, targetLevel!!, weight!!, height!!, date!!
            )
        } else {
            result = ResultData(
                requestId!!, doctorId!!, doctorName!!, doctorImage!!, trainerId!!,
                trainerName!!, trainerAge!!, trainerGender!!,
                patientId!!, patientName!!, patientAge!!, patientGender!!,
                sugarLevel!!, targetLevel!!, weight!!, height!!, date!!
            )
        }


        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (dataSnapshot.child(requestId!!).exists()) {

                    if (type == "doctor") {
                        databaseReference.child(requestId!!).child("doctorImageResult").setValue(
                            image
                        ) { error, ref ->
                            if (error != null) {
                                Toast.makeText(
                                    this@DoctorResultActivity, "Fail to add data $error",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val intent =
                                    Intent(this@DoctorResultActivity, MainDoctorActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }
                    } else {
                        databaseReference.child(requestId!!).child("trainerImageResult").setValue(
                            image
                        ) { error, ref ->
                            if (error != null) {
                                Toast.makeText(
                                    this@DoctorResultActivity, "Fail to add data $error",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val intent =
                                    Intent(this@DoctorResultActivity, MainTrainerActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }
                    }
                } else {

                    databaseReference.child(requestId!!).setValue(
                        result
                    ) { error, ref ->

                        if (error != null) {
                            Toast.makeText(
                                this@DoctorResultActivity, "Fail to add data $error", Toast.LENGTH_SHORT
                            ).show()
                        } else {

                            Toast.makeText(
                                this@DoctorResultActivity, "send result successfully",
                                Toast.LENGTH_SHORT
                            ).show()

                            if (type == "doctor") {
                                databaseReference.child(requestId!!).child("doctorImageResult").setValue(
                                    image
                                ) { error, ref ->
                                    if (error != null) {
                                        Toast.makeText(
                                            this@DoctorResultActivity, "Fail to add data $error",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        val intent =
                                            Intent(this@DoctorResultActivity, MainDoctorActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                }
                            } else {
                                databaseReference.child(requestId!!).child("trainerImageResult").setValue(
                                    image
                                ) { error, ref ->
                                    if (error != null) {
                                        Toast.makeText(
                                            this@DoctorResultActivity, "Fail to add data $error",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        val intent =
                                            Intent(this@DoctorResultActivity, MainTrainerActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                }
                            }
                        }
                    }


                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })



    }

}