package fr.feepin.devfinder.base

import android.app.Application
import com.google.firebase.auth.FirebaseAuthSettings
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.HiltAndroidApp
import com.google.firebase.firestore.FirebaseFirestoreSettings

@HiltAndroidApp
class BaseApplication : Application(){
    override fun onCreate() {
        super.onCreate()

    }
}