package ru.sokolovromann.mynotepad.data.local.account

data class Account(
    val uid: String,
    val email: String,
    val tokenId: String
) {

    companion object {
        const val DEFAULT_NAME = ""
        private const val LOCAL_UID = "local_uid"
        private const val LOCAL_EMAIL = "local_email"
        private const val LOCAL_TOKEN_ID = "local_token_id"

        val LocalAccount: Account = Account(
            uid = LOCAL_UID,
            email = LOCAL_EMAIL,
            tokenId = LOCAL_TOKEN_ID
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
