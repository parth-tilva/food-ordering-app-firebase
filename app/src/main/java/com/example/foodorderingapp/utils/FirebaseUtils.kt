package com.example.foodorderingapp.utils

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseUtils {
}


//object FirebaseProfileService {
//    private const val TAG = "FirebaseProfileService"
//    suspend fun getProfileData(userId: String): User? {
//        val db = FirebaseFirestore.getInstance()
//        return try {
//            db.collection("users")
//                .document(userId).get().await().toUser()
//        } catch (e: Exception) {
//            Log.e(TAG, "Error getting user details", e)
//            FirebaseCrashlytics.getInstance().log("Error getting user details")
//            FirebaseCrashlytics.getInstance().setCustomKey("user id", xpertSlug)
//            FirebaseCrashlytics.getInstance().recordException(e)
//            null
//        }
//    }
//}