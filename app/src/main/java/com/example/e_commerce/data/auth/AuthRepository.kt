package com.example.e_commerce.data.auth

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

// Thin wrapper over Firebase Auth + Credential Manager's Google Sign-In.
// The screen only sees high-level Success/Cancel/Error/NotConfigured.
//
// Prerequisites (do these before running):
//   1. Create a Firebase project at console.firebase.google.com
//   2. Add an Android app, package name com.example.e_commerce
//   3. Drop google-services.json into app/
//   4. Enable Google as a sign-in method in Authentication
//   5. Create an OAuth 2.0 Web client ID in Google Cloud console and
//      paste it below as WEB_CLIENT_ID
class AuthRepository {

    // OAuth 2.0 Web Client ID from Firebase's auto-generated Web client.
    // Lives in google-services.json under oauth_client[client_type=3].
    private val webClientId: String =
        "977271579452-g3qjumcipduooagg3gclviqf8ju5muhu.apps.googleusercontent.com"

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    sealed class Outcome {
        data class Success(val user: FirebaseUser) : Outcome()
        data object Cancelled : Outcome()
        data class Error(val message: String) : Outcome()
        data object NotConfigured : Outcome()
    }

    fun currentUser(): FirebaseUser? = auth.currentUser

    // Cold flow of the currently signed-in user. Emits on sign-in/sign-out.
    fun observeUser(): Flow<FirebaseUser?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { trySend(it.currentUser) }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    suspend fun signInWithGoogle(context: Context): Outcome {
        if (webClientId.isBlank()) return Outcome.NotConfigured

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(webClientId)
            .setAutoSelectEnabled(true)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val credentialManager = CredentialManager.create(context)

        return try {
            val response = credentialManager.getCredential(context, request)
            val credential = response.credential
            if (credential is CustomCredential &&
                credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            ) {
                val idToken = GoogleIdTokenCredential.createFrom(credential.data).idToken
                val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                val result = auth.signInWithCredential(firebaseCredential).await()
                val user = result.user
                if (user != null) Outcome.Success(user) else Outcome.Error("Sign-in returned no user")
            } else {
                Outcome.Error("Unsupported credential type")
            }
        } catch (_: GetCredentialCancellationException) {
            Outcome.Cancelled
        } catch (_: NoCredentialException) {
            // Most common cause: SHA-1 not registered in Firebase project,
            // or no Google accounts on the device.
            Outcome.Error(
                "No matching Google account found. Check that your app's SHA-1 " +
                    "is added in Firebase project settings and that you're signed " +
                    "into a Google account on this device."
            )
        } catch (e: GetCredentialException) {
            // Other credential errors (network, Play Services too old, etc.)
            Outcome.Error("Credential Manager error: ${e.javaClass.simpleName} — ${e.message ?: "unknown"}")
        } catch (_: GoogleIdTokenParsingException) {
            Outcome.Error("Invalid Google ID token")
        } catch (t: Throwable) {
            Outcome.Error(t.message ?: "Sign-in failed")
        }
    }

    suspend fun signOut() {
        auth.signOut()
    }
}