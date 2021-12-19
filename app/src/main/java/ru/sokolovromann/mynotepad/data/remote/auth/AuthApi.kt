package ru.sokolovromann.mynotepad.data.remote.auth

import com.google.firebase.auth.FirebaseAuth
import kotlin.NullPointerException

class AuthApi(
    private val firebaseAuth: FirebaseAuth
) {

    fun getCurrentUser(onResult: (result: Result<UserResponse>) -> Unit) {
        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) {
            onResult(Result.failure(NullPointerException("Current user is null")))
        } else {
            currentUser.getIdToken(true)
                .addOnSuccessListener { tokenResult ->
                    val tokenId = tokenResult.token
                    if (tokenId == null) {
                        onResult(Result.failure(NullPointerException("Token is null")))
                    } else {
                        val userResponse = UserResponse(
                            uid = currentUser.uid,
                            email = currentUser.email ?: "",
                            tokenId = tokenId
                        )
                        onResult(Result.success(userResponse))
                    }
                }
                .addOnFailureListener { exception -> onResult(Result.failure(exception)) }
        }
    }

    fun createUserWithEmailPassword(email: String, password: String, onResult: (result: Result<UserResponse>) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                val firebaseUser = authResult.user
                if (firebaseUser == null) {
                    onResult(Result.failure(NullPointerException("Firebase user is null")))
                } else {
                    val userResponse = UserResponse(
                        uid = firebaseUser.uid,
                        email = firebaseUser.email ?: ""
                    )
                    onResult(Result.success(userResponse))
                }
            }
            .addOnFailureListener { exception -> onResult(Result.failure(exception)) }
    }

    fun signInWithEmailPassword(email: String, password: String, onResult: (result: Result<UserResponse>) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                val firebaseUser = authResult?.user
                if (firebaseUser == null) {
                    onResult(Result.failure(NullPointerException("Firebase user is null")))
                } else {
                    val userResponse = UserResponse(
                        uid = firebaseUser.uid,
                        email = firebaseUser.email ?: ""
                    )
                    onResult(Result.success(userResponse))
                }
            }
            .addOnFailureListener { exception -> onResult(Result.failure(exception)) }
    }

    fun updateEmail(email: String, onResult: (result: Result<Unit>) -> Unit) {
        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) {
            onResult(Result.failure(NullPointerException("Firebase user is null")))
        } else {
            currentUser.updateEmail(email)
                .addOnSuccessListener { onResult(Result.success(Unit)) }
                .addOnFailureListener { exception -> onResult(Result.failure(exception)) }
        }
    }

    fun updatePassword(password: String, onResult: (result: Result<Unit>) -> Unit) {
        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) {
            onResult(Result.failure(NullPointerException("Firebase user is null")))
        } else {
            currentUser.updatePassword(password)
                .addOnSuccessListener { onResult(Result.success(Unit)) }
                .addOnFailureListener { exception -> onResult(Result.failure(exception)) }
        }
    }

    fun sendPasswordResetEmail(email: String, onResult: (result: Result<Unit>) -> Unit) {
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnSuccessListener { onResult(Result.success(Unit)) }
            .addOnFailureListener { exception -> onResult(Result.failure(exception)) }
    }

    fun deleteCurrentUser(onResult: (result: Result<Unit>) -> Unit) {
        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) {
            onResult(Result.failure(NullPointerException("Firebase user is null")))
        } else {
            currentUser.delete()
                .addOnSuccessListener { onResult(Result.success(Unit)) }
                .addOnFailureListener { exception -> onResult(Result.failure(exception)) }
        }
    }

    fun signOut(onResult: (result: Result<Unit>) -> Unit) {
        firebaseAuth.signOut()
        onResult(Result.success(Unit))
    }
}