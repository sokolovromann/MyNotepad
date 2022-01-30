package ru.sokolovromann.mynotepad.data.mapping

import ru.sokolovromann.mynotepad.data.local.account.Account
import ru.sokolovromann.mynotepad.data.remote.auth.UserResponse

interface AccountMapping {

    fun toAccount(userResponse: UserResponse): Account
}