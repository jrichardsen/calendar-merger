package com.gmail.jrichardsen.calendar_merger.data

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.gmail.jrichardsen.calendar_merger.entities.Settings
import com.gmail.jrichardsen.calendar_merger.entities.toSettingsMessage
import com.google.protobuf.InvalidProtocolBufferException
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Singleton

object SettingsSerializer : Serializer<SettingsMessage> {
    override val defaultValue: SettingsMessage = Settings.getDefault().toSettingsMessage()

    override suspend fun readFrom(input: InputStream): SettingsMessage {
        try {
            return SettingsMessage.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(
        t: SettingsMessage,
        output: OutputStream
    ) = t.writeTo(output)
}

val Context.settingsDataStore: DataStore<SettingsMessage> by dataStore(
    fileName = "settings.pb",
    serializer = SettingsSerializer
)

@InstallIn(SingletonComponent::class)
@Module
class SettingsDataStoreModule {
    @Provides
    @Singleton
    fun provideSettingsDataStore(@ApplicationContext appContext: Context): DataStore<SettingsMessage> {
        return appContext.settingsDataStore
    }
}