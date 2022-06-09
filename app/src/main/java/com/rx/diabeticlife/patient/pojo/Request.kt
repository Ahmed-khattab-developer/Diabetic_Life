package com.rx.diabeticlife.patient.pojo

class Request() {

    var uId: String? = null
    var doctorId: String? = null
    var doctorName: String? = null
    var doctorImage: String? = null
    var doctorFees: String? = null
    var trainerId: String? = null
    var trainerName: String? = null
    var trainerAge: String? = null
    var trainerGender: String? = null
    var trainerFees: String? = null
    var patientId: String? = null
    var patientName: String? = null
    var patientAge: String? = null
    var patientGender: String? = null
    var sugarLevel: String? = null
    var targetLevel: String? = null
    var weight: String? = null
    var height: String? = null
    var date: String? = null
    var doctorUpload: String? = null
    var trainerUpload: String? = null

    constructor(
        uId: String,doctorId: String, doctorName: String, doctorImage: String,doctorFees: String,
        trainerId: String, trainerName: String, trainerAge: String, trainerGender: String,trainerFees: String,
        patientId: String, patientName: String, patientAge: String, patientGender: String,
        sugarLevel: String, targetLevel: String, weight: String, height: String,
        date: String,doctorUpload: String,trainerUpload: String
    ) : this() {
        this.uId = uId
        this.doctorId = doctorId
        this.doctorName = doctorName
        this.doctorImage = doctorImage
        this.doctorFees = doctorFees
        this.trainerId = trainerId
        this.trainerName = trainerName
        this.trainerAge = trainerAge
        this.trainerGender = trainerGender
        this.trainerFees = trainerFees
        this.patientId = patientId
        this.patientName = patientName
        this.patientAge = patientAge
        this.patientGender = patientGender
        this.sugarLevel = sugarLevel
        this.targetLevel = targetLevel
        this.weight = weight
        this.height = height
        this.date = date
        this.doctorUpload = doctorUpload
        this.trainerUpload = trainerUpload
    }

}