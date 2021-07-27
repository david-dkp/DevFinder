package fr.feepin.devfinder.activitycontracts

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import java.lang.NullPointerException

class GoogleSignInActivityContract : ActivityResultContract<GoogleSignInOptions, AuthCredential?>() {

    override fun createIntent(context: Context, input: GoogleSignInOptions?): Intent {
        input?.let { gso ->
            val googleClient = GoogleSignIn.getClient(context, gso)
            return googleClient.signInIntent
        } ?: throw NullPointerException()
    }

    override fun parseResult(resultCode: Int, intent: Intent?): AuthCredential? {
        val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
        return try {
            GoogleAuthProvider.getCredential(task.getResult(ApiException::class.java)!!.idToken, null)
        } catch (e: ApiException) {
            null
        }
    }
}