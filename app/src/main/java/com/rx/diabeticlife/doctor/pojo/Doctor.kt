package com.rx.diabeticlife.doctor.pojo

class Doctor() {

    var uid: String? = null
    var name: String? = null
    var image: String? = null
    var fees: String? = null

    constructor(uid: String, name: String, image: String, fees: String) : this() {
        this.uid = uid
        this.name = name
        this.image = image
        this.fees = fees
    }

}