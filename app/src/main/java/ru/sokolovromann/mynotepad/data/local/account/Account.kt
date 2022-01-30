package ru.sokolovromann.mynotepad.data.local.account

data class Account(
    val uid: String,
    val email: String
) {

    companion object {
        const val DEFAULT_NAME = ""
        private const val LOCAL_UID = "local_uid"
        private const val LOCAL_EMAIL = "local_email"

        val LocalAccount: Account = Account(
            uid = LOCAL_UID,
            email = LOCAL_EMAIL
        )
    }

    fun getName(): String {
        return if (isLocalAccount()) DEFAULT_NAME else email
    }

    fun isEmpty(): Boolean {
        return uid.isEmpty()
    }

    fun isNotEmpty() = !isEmpty()

    fun isLocalAccount(): Boolean {
        return uid == LOCAL_UID
    }
}
