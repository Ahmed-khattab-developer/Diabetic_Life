package com.rx.diabeticlife.trainer.pojo

class Trainer() {

    var uid: String? = null
    var name: String? = null
    var age: String? = null
    var gender: String? = null
    var fees: String? = null

    constructor(uid: String,name: String, age: String, gender: String, fees: String) : this() {
        this.uid = uid
        this.name = name
        this.age = age
        this.gender = gender
        this.fees = fees
    }
}