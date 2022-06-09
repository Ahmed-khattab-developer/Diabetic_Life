package com.rx.diabeticlife.patient.pojo

class Patient() {

    var uid: String? = null
    var name: String? = null
    var age: String? = null
    var gender: String? = null
    var weight: String? = null
    var height: String? = null
    var bloodSugarLevel: String? = null
    var target: String? = null

    constructor(
        uid: String, name: String, age: String, gender: String,
        weight: String, height: String, bloodSugarLevel: String, target: String
    ) : this() {
        this.uid = uid
        this.name = name
        this.age = age
        this.gender = gender
        this.weight = weight
        this.height = height
        this.bloodSugarLevel = bloodSugarLevel
        this.target = target
    }

}