package com.example.data.di

import android.content.Context
import androidx.room.Room
import com.example.data.local.ContactDao
import com.example.data.local.ContactDatabase
import com.example.data.remote.ContactApi
import com.example.data.repository.ContactRepositoryImp
import com.example.domain.repository.ContactRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @Provides
    @Singleton
    fun provideRetrofit(moshi: Moshi): Retrofit =
        Retrofit
            .Builder()
            .baseUrl("https://randomuser.me/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    @Provides
    @Singleton
    fun provideContactApi(retrofit: Retrofit): ContactApi = retrofit.create(ContactApi::class.java)

    @Provides
    @Singleton
    fun provideContactDatabase(
        @ApplicationContext context: Context,
    ): ContactDatabase =
        Room
            .databaseBuilder(
                context,
                ContactDatabase::class.java,
                "randomuser.db",
            ).build()

    @Provides
    @Singleton
    fun provideContactDao(contactDatabase: ContactDatabase): ContactDao = contactDatabase.dao

    @Provides
    @Singleton
    fun provideContactRepository(
        contactDao: ContactDao,
        contactApi: ContactApi,
        contactDatabase: ContactDatabase,
    ): ContactRepository = ContactRepositoryImp(contactDao, contactApi, contactDatabase)
}
