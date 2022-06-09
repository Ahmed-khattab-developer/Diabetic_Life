package com.rx.diabeticlife.patient.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.rx.diabeticlife.R
import com.rx.diabeticlife.SessionManagement
import com.rx.diabeticlife.SessionManagement.Constants.AGE
import com.rx.diabeticlife.SessionManagement.Constants.BLOODSUGARLEVEL
import com.rx.diabeticlife.SessionManagement.Constants.GENDER
import com.rx.diabeticlife.SessionManagement.Constants.HEIGHT
import com.rx.diabeticlife.SessionManagement.Constants.NAME
import com.rx.diabeticlife.SessionManagement.Constants.TARGET
import com.rx.diabeticlife.SessionManagement.Constants.WEIGHT
import com.rx.diabeticlife.patient.activity.MainPatientActivity
import com.rx.diabeticlife.patient.pojo.Patient

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private lateinit var radioSexGroup: RadioGroup
    private var gender: String? = "male"

    var sugarList = ArrayList<String>()
    var targetList = ArrayList<String>()
    var spinner: Spinner? = null
    var spinner2: Spinner? = null
    var sugarLevel: String? = null
    var targetLevel: String? = null

    private lateinit var iSessionManagement: SessionManagement

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        iSessionManagement = SessionManagement(context)

        view.findViewById<TextView>(R.id.user).text =
            "Hello " + iSessionManagement.getUserDetails()[NAME]

        auth = FirebaseAuth.getInstance()

        firebaseDatabase = FirebaseDatabase.getInstance()

        databaseReference = firebaseDatabase.getReference("patient").child(auth.uid.toString())

        view.findViewById<EditText>(R.id.et_name).setText(iSessionManagement.getUserDetails()[NAME])
        view.findViewById<EditText>(R.id.et_age).setText(iSessionManagement.getUserDetails()[AGE])
        view.findViewById<EditText>(R.id.ed_weight)
            .setText(iSessionManagement.getUserDetails()[WEIGHT])
        view.findViewById<EditText>(R.id.ed_height)
            .setText(iSessionManagement.getUserDetails()[HEIGHT])


        spinner = view.findViewById(R.id.spinner_sample)
        spinner2 = view.findViewById(R.id.spinner_sample_2)

        sugarList.add("low")
        sugarList.add("medium")
        sugarList.add("high")

        targetList.add("lose weight")
        targetList.add("gain muscles")
        targetList.add("get stronger")

        spinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View, position: Int, id: Long
            ) {
                val textView: TextView = parent.getChildAt(0) as TextView
                textView.setTextColor(Color.BLACK)
                sugarLevel = sugarList[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        val aa = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, sugarList)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner!!.adapter = aa


        val spinnerPosition: Int =
            aa.getPosition(iSessionManagement.getUserDetails()[BLOODSUGARLEVEL])
        spinner!!.setSelection(spinnerPosition)


        spinner2!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View, position: Int, id: Long
            ) {
                val textView: TextView = parent.getChildAt(0) as TextView
                textView.setTextColor(Color.BLACK)
                targetLevel = targetList[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        val bb = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, targetList)
        bb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner2!!.adapter = bb

        val spinnerPosition2: Int = bb.getPosition(iSessionManagement.getUserDetails()[TARGET])
        spinner2!!.setSelection(spinnerPosition2)


        radioSexGroup = view.findViewById(R.id.genderRG)

        radioSexGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioM -> gender = "male"
                R.id.radioF -> gender = "female"
            }
        }

        if (iSessionManagement.getUserDetails()[GENDER].equals("male")) {
            view.findViewById<RadioButton>(R.id.radioM).isChecked = true
        } else {
            view.findViewById<RadioButton>(R.id.radioF).isChecked = true
        }

        view.findViewById<Button>(R.id.button_save).setOnClickListener {
            when {
                view.findViewById<EditText>(R.id.et_name).text.trim().isEmpty() -> {
                    Toast.makeText(requireContext(), "Patient Name Required ", Toast.LENGTH_SHORT)
                        .show()
                }
                view.findViewById<EditText>(R.id.et_age).text.trim().isEmpty() -> {
                    Toast.makeText(requireContext(), "Patient Age Required ", Toast.LENGTH_SHORT)
                        .show()
                }
                view.findViewById<EditText>(R.id.ed_weight).text.trim().isEmpty() -> {
                    Toast.makeText(requireContext(), "Enter Weight", Toast.LENGTH_SHORT).show()
                }
                view.findViewById<EditText>(R.id.ed_height).text.trim().isEmpty() -> {
                    Toast.makeText(requireContext(), "Enter Height", Toast.LENGTH_SHORT).show()
                }
                sugarLevel == null -> {
                    Toast.makeText(requireContext(), "Enter Sugar Level", Toast.LENGTH_SHORT).show()
                }
                targetLevel == null -> {
                    Toast.makeText(requireContext(), "Enter Target Level", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {
                    addDataToFirebase(
                        view.findViewById<EditText>(R.id.et_name).text.toString(),
                        view.findViewById<EditText>(R.id.et_age).text.toString(),
                        gender.toString()
                    )
                }
            }
        }

    }

    private fun addDataToFirebase(name: String, age: String, gender: String) {

        val patient = Patient(
            auth.uid.toString(), name, age, gender,
            view?.findViewById<EditText>(R.id.ed_weight)?.text.toString(),
            view?.findViewById<EditText>(R.id.ed_height)?.text.toString(),
            sugarLevel!!, targetLevel!!
        )

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                databaseReference.setValue(patient)
                iSessionManagement.createLoginSession(
                    true, auth.uid.toString(), "patient", name,"", age, gender, "",
                    view?.findViewById<EditText>(R.id.ed_weight)?.text.toString(),
                    view?.findViewById<EditText>(R.id.ed_height)?.text.toString(),
                    sugarLevel!!, targetLevel!!
                )

                val intent =
                    Intent(requireContext(), MainPatientActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    requireContext(), "Fail to add data $error", Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}