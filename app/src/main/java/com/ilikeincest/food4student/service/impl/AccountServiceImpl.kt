package com.ilikeincest.food4student.service.impl

import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.ilikeincest.food4student.model.Account
import com.ilikeincest.food4student.service.AccountService
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

// If shit throws here, you are calling it before logging in.
class AccountServiceImpl @Inject constructor() : AccountService {
    override val currentUser: Flow<Account?>
        get() = callbackFlow {
            val listener =
                FirebaseAuth.AuthStateListener { auth ->
                    this.trySend(auth.currentUser!!.toAppUser())
                }
            Firebase.auth.addAuthStateListener(listener)
            awaitClose { Firebase.auth.removeAuthStateListener(listener) }
        }

    override val currentUserId: String?
        get() = Firebase.auth.currentUser?.uid

    override suspend fun getUserToken(): String? {
        val currentUser = Firebase.auth.currentUser ?: return null
        return currentUser.getIdToken(false).await().token
    }

    override fun hasUser(): Boolean {
        return Firebase.auth.currentUser != null
    }

    override fun getUserProfile(): Account {
        return Firebase.auth.currentUser?.toAppUser() ?: Account()
    }

    override suspend fun createAccountWithEmail(email: String, password: String) {
        Firebase.auth.createUserWithEmailAndPassword(email, password).await()
    }

    override suspend fun updateDisplayName(newDisplayName: String) {
        val profileUpdates = userProfileChangeRequest {
            displayName = newDisplayName
        }

        Firebase.auth.currentUser?.updateProfile(profileUpdates)?.await()
    }

    override suspend fun forgetPassword(email: String) {
        Firebase.auth.sendPasswordResetEmail(email)
    }

    override suspend fun linkAccountWithGoogle(idToken: String) {
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        Firebase.auth.currentUser?.linkWithCredential(firebaseCredential)?.await()
    }

    override suspend fun linkAccountWithEmail(email: String, password: String) {
        val credential = EmailAuthProvider.getCredential(email, password)
        Firebase.auth.currentUser?.linkWithCredential(credential)?.await()
    }

    override suspend fun signInWithGoogle(idToken: String) {
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        Firebase.auth.signInWithCredential(firebaseCredential).await()
    }

    override suspend fun signInWithEmail(email: String, password: String) {
        Firebase.auth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun signOut() {
        Firebase.auth.signOut()
    }

    override suspend fun deleteAccount() {
        Firebase.auth.currentUser?.delete()?.await()
    }

    override suspend fun getUserRole(): String? { // TODO: move to enum return type
        val currentUser = Firebase.auth.currentUser ?: return null
        return currentUser.getIdToken(false).await().claims["role"] as String?
    }

    private fun FirebaseUser.toAppUser(): Account {
        return Account(
            id = this.uid,
            email = this.email ?: "",
            provider = this.providerId,
            displayName = this.displayName ?: "",
            photoUrl = this.photoUrl,
        )
    }

    override suspend fun reloadToken() {
        Firebase.auth.currentUser?.getIdToken(true)?.await()
    }
}