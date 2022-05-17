package com.rx.diabeticlife.trainer.pojo

class Trainer() {

    var uid: String? = null
    var name: String? = null
    var age: String? = null
    var gender: String? = null

    constructor(uid: String,name: String, age: String, gender: String) : this() {
        this.uid = uid
        this.name = name
        this.age = age
        this.gender = gender
    }
}