package ru.sokolovromann.mynotepad.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import ru.sokolovromann.mynotepad.data.local.LocalDatabase
import ru.sokolovromann.mynotepad.data.local.account.AccountDataStore
import ru.sokolovromann.mynotepad.data.local.settings.SettingsDataStore
import ru.sokolovromann.mynotepad.data.mapping.NoteMapping
import ru.sokolovromann.mynotepad.data.mapping.NoteMappingImpl
import ru.sokolovromann.mynotepad.data.remote.auth.AuthApi
import ru.sokolovromann.mynotepad.data.remote.note.NoteApi
import ru.sokolovromann.mynotepad.data.repository.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private val Context.settingDataStore: DataStore<Preferences> by preferencesDataStore(
        name = "my_notepad_local_settings"
    )

    private val Context.accountDataStore: DataStore<Preferences> by preferencesDataStore(
        name = "my_notepad_local_account"
    )

    @Provides
    @Singleton
    fun providesNoteRepository(noteRepositoryImpl: NoteRepositoryImpl): NoteRepository {
        return noteRepositoryImpl
    }

    @Provides
    @Singleton
    fun providesNoteRepositoryImpl(localDatabase: LocalDatabase, noteApi: NoteApi, noteMapping: NoteMapping): NoteRepositoryImpl {
        return NoteRepositoryImpl(localDatabase, noteApi, noteMapping)
    }

    @Provides
    @Singleton
    fun providesLocalDatabase(@ApplicationContext context: Context): LocalDatabase {
        return Room.databaseBuilder(context, LocalDatabase::class.java, "my_notepad_local_database")
            .build()
    }

    @Provides
    @Singleton
    fun providesNoteApi(client: HttpClient): NoteApi {
        return NoteApi(client)
    }

    @Provides
    @Singleton
    fun providesHttpClient(): HttpClient {
        return HttpClient(Android) {
            install(JsonFeature) {
                serializer = KotlinxSerializer()
            }
        }
    }

    @Provides
    @Singleton
    fun providesNoteMapping(noteMappingImpl: NoteMappingImpl): NoteMapping {
        return noteMappingImpl
    }

    @Provides
    @Singleton
    fun providesNoteMappingImpl(): NoteMappingImpl {
        return NoteMappingImpl()
    }

    @Provides
    @Singleton
    fun providesSettingsRepository(settingsRepositoryImpl: SettingsRepositoryImpl): SettingsRepository {
        return settingsRepositoryImpl
    }

    @Provides
    @Singleton
    fun providesSettingsRepositoryImpl(settingsDataStore: SettingsDataStore): SettingsRepositoryImpl {
        return SettingsRepositoryImpl(settingsDataStore)
    }

    @Provides
    @Singleton
    fun providesSettingsDataStore(@ApplicationContext context: Context): SettingsDataStore {
        return SettingsDataStore(context.settingDataStore)
    }

    @Provides
    @Singleton
    fun providesAccountRepository(accountRepositoryImpl: AccountRepositoryImpl): AccountRepository {
        return accountRepositoryImpl
    }

    @Provides
    @Singleton
    fun providesAccountRepositoryImpl(accountDataStore: AccountDataStore, authApi: AuthApi, dispatcher: CoroutineDispatcher): AccountRepositoryImpl {
        return AccountRepositoryImpl(accountDataStore, authApi, dispatcher)
    }

    @Provides
    @Singleton
    fun providesAccountDataStore(@ApplicationContext context: Context): AccountDataStore {
        return AccountDataStore(context.accountDataStore)
    }

    @Provides
    @Singleton
    fun providesAccountApi(firebaseAuth: FirebaseAuth): AuthApi {
        return AuthApi(firebaseAuth)
    }

    @Provides
    @Singleton
    fun providesFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }

    @Provides
    @Singleton
    fun providesDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }
}