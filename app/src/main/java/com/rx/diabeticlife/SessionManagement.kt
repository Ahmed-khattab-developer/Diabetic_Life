package com.rx.diabeticlife

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.rx.diabeticlife.SessionManagement.Constants.AGE
import com.rx.diabeticlife.SessionManagement.Constants.BLOODSUGARLEVEL
import com.rx.diabeticlife.SessionManagement.Constants.FEES
import com.rx.diabeticlife.SessionManagement.Constants.GENDER
import com.rx.diabeticlife.SessionManagement.Constants.HEIGHT
import com.rx.diabeticlife.SessionManagement.Constants.ID
import com.rx.diabeticlife.SessionManagement.Constants.IMAGE
import com.rx.diabeticlife.SessionManagement.Constants.IS_LOGIN
import com.rx.diabeticlife.SessionManagement.Constants.NAME
import com.rx.diabeticlife.SessionManagement.Constants.PREF_NAME
import com.rx.diabeticlife.SessionManagement.Constants.TARGET
import com.rx.diabeticlife.SessionManagement.Constants.TYPE
import com.rx.diabeticlife.SessionManagement.Constants.WEIGHT
import java.util.*

@SuppressLint("CommitPrefEdits")
class SessionManagement constructor(context: Context?) {

    private var pref: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private var _context: Context? = context

    object Constants {
        const val PREF_NAME = "diabetic"
        const val IS_LOGIN = "IsLoggedIn"
        const val TYPE = "TYPE"
        const val ID = "ID"
        const val NAME = "NAME"
        const val FEES = "FEES"
        const val AGE = "AGE"
        const val GENDER = "GENDER"
        const val IMAGE = "IMAGE"
        const val WEIGHT = "WEIGHT"
        const val HEIGHT = "HEIGHT"
        const val BLOODSUGARLEVEL = "BLOODSUGARLEVEL"
        const val TARGET = "TARGET"
    }

    init {
        val privateMode = 0
        pref = _context!!.getSharedPreferences(PREF_NAME, privateMode)
        editor = pref?.edit()
    }

    /**
     * Get stored session data
     */
    fun createLoginSession(
        Status: Boolean?,
        id: String?,
        type: String?,
        name: String?,
        fees: String?,
        age: String?,
        gender: String?,
        image: String?,
        weight: String?,
        height: String?,
        bloodSugarLevel: String?,
        target: String?
    ) {
        editor!!.putBoolean(IS_LOGIN, Status!!)
        editor!!.putString(ID, id)
        editor!!.putString(TYPE, type)
        editor!!.putString(NAME, name)
        editor!!.putString(FEES, fees)
        editor!!.putString(AGE, age)
        editor!!.putString(GENDER, gender)
        editor!!.putString(IMAGE, image)
        editor!!.putString(WEIGHT, weight)
        editor!!.putString(HEIGHT, height)
        editor!!.putString(BLOODSUGARLEVEL, bloodSugarLevel)
        editor!!.putString(TARGET, target)
        editor!!.commit()
    }

    fun getUserDetails(): HashMap<String, String?> {
        val users = HashMap<String, String?>()
        users[ID] = pref!!.getString(ID, null)
        users[TYPE] = pref!!.getString(TYPE, null)
        users[NAME] = pref!!.getString(NAME, null)
        users[FEES] = pref!!.getString(FEES, null)
        users[AGE] = pref!!.getString(AGE, null)
        users[GENDER] = pref!!.getString(GENDER, null)
        users[IMAGE] = pref!!.getString(IMAGE, null)
        users[WEIGHT] = pref!!.getString(WEIGHT, null)
        users[HEIGHT] = pref!!.getString(HEIGHT, null)
        users[BLOODSUGARLEVEL] = pref!!.getString(BLOODSUGARLEVEL, null)
        users[TARGET] = pref!!.getString(TARGET, null)
        return users
    }

    /**
     * Clear session details
     */
    fun logoutUser() {
        editor!!.clear()
        editor!!.commit()
        val intent = Intent(_context, SplashActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        _context!!.startActivity(intent)
    }

    fun logout() {
        editor!!.clear()
        editor!!.commit()
    }

    /**
     * Quick check for login
     */
    fun isLoggedIn(): Boolean {
        return pref!!.getBoolean(IS_LOGIN, false)
    }
}