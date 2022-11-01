package com.techarion.techarion.utils

import android.content.SharedPreferences
import android.util.Log
import com.techarion.techarion.HiltApplication.HiltApplication
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor() {
    private var sharedPreferences: SharedPreferences
    private var editor: SharedPreferences.Editor
    private var PRIVATE_MODE = 0



    fun clearAll() {
        editor.clear()
        editor.commit()
    }






    fun logOut() {
        val editor = sharedPreferences.edit()
        editor?.clear()
        editor?.apply()
    }

    fun saveToken(token: String,value:String) {
        val prefsEditor: SharedPreferences.Editor = sharedPreferences.edit()
        with(prefsEditor) {
            this.putString(token,value)
            this.commit()
        }
    }

    fun saveUserRole(token: String,value:Boolean) {
        val prefsEditor: SharedPreferences.Editor = sharedPreferences.edit()
        with(prefsEditor) {
            this.putBoolean(token,value)
            this.commit()
        }
    }



    fun getToken(): String? {
        return sharedPreferences.getString(Cons.USER_TOKEN, null)


    }

    fun getAdmin(key:String): Boolean {
        return sharedPreferences.getBoolean(key,false)
    }
    fun getEmp(key: String):Boolean{
        return sharedPreferences.getBoolean(key,false)
    }

    fun getTeamLead():Boolean{
        return sharedPreferences.getBoolean("",false)
    }


    companion object {
        private const val PREF_NAME = "prefname"
        private var instance: TokenManager? = null
        fun get(): TokenManager {
            if (instance == null) {
                instance = TokenManager()
            }
            return instance as TokenManager
        }
    }

    init {
        sharedPreferences = HiltApplication.get().getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = sharedPreferences.edit()
        editor.apply()
    }
}