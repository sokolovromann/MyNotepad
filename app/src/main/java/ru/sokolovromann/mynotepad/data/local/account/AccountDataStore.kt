package ru.sokolovromann.mynotepad.data.local.account

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AccountDataStore(
    private val dataStore: DataStore<Preferences>
) {
    private val uidKey = stringPreferencesKey("uid")
    private val emailKey = stringPreferencesKey("email")
    private val tokenIdKey = stringPreferencesKey("tokenId")

    fun getAccount(): Flow<Account> {
        return dataStore.data.map { preferences ->
            Account(
                uid = preferences[uidKey] ?: "",
                email = preferences[emailKey] ?: "",
                tokenId = preferences[tokenIdKey] ?: ""
            )
        }
    }

    suspend fun saveAccount(account: Account) {
        dataStore.edit { preferences ->
            preferences[uidKey] = account.uid
            preferences[emailKey] = account.email
            preferences[tokenIdKey] = account.tokenId
        }
    }

    suspend fun saveEmail(email: String) {
        dataStore.edit { preferences ->
            preferences[emailKey] = email
        }
    }

    suspend fun clear() {
        dataStore.edit { preferences ->
            preferences[uidKey] = ""
            preferences[emailKey] = ""
            preferences[tokenIdKey] = ""
        }
    }
}