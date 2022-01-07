package com.example.trace.hilt

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import com.example.trace.common.Constants.DATABASE_NAME
import com.example.trace.common.Constants.KEY_FIRST_TIME_TOGGLE
import com.example.trace.common.Constants.KEY_NAME
import com.example.trace.common.Constants.KEY_WEIGHT
import com.example.trace.common.Constants.SHARED_PREFERENCES_NAME
import com.example.trace.db.RunDAO
import com.example.trace.db.RunningDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideRunningDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        RunningDatabase::class.java,
        DATABASE_NAME
    ).build()

    @Provides
    @Singleton
    fun provideRunDao(db: RunningDatabase): RunDAO {
        return db.getRunDAO()
    }

    @Provides
    @Singleton
    fun provideSharedPrefs(
        @ApplicationContext app: Context
    ) = app.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)


    @Provides
    @Singleton
    fun provideName(sharedPref: SharedPreferences) = sharedPref.getString(KEY_NAME, "") ?: ""

    @Provides
    @Singleton
    fun provideWeight(sharedPref: SharedPreferences) = sharedPref.getFloat(KEY_WEIGHT, 80f)

    @Provides
    @Singleton
    fun provideFirstTime(sharedPref: SharedPreferences) =
        sharedPref.getBoolean(KEY_FIRST_TIME_TOGGLE, true)

}