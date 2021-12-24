package ru.sokolovromann.mynotepad.data.local.account

data class Account(
    val uid: String,
    val email: String,
    val tokenId: String
) {

    companion object {
        val LocalAccount: Account = Account(
            uid = "local_uid",
            email = "local_email",
            tokenId = "local_token_id"
        )
    }

    fun isEmpty(): Boolean {
        return uid.isEmpty()
    }

    fun isNotEmpty() = !isEmpty()

    fun isLocalAccount(): Boolean {
        return uid == "local_uid"
    }
}
