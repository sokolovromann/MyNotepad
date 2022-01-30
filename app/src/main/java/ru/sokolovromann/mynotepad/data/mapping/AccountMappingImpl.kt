package ru.sokolovromann.mynotepad.data.mapping

import ru.sokolovromann.mynotepad.data.local.account.Account
import ru.sokolovromann.mynotepad.data.remote.auth.UserResponse

class AccountMappingImpl : AccountMapping {

    override fun toAccount(userResponse: UserResponse) = Account(
        uid = userResponse.uid,
        email = userResponse.email
    )
}