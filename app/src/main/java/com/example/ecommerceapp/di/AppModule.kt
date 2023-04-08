package com.example.ecommerceapp.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.ecommerceapp.firebase.FirebaseCommon
import com.example.ecommerceapp.util.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent :: class)
object AppModule : Application() {

    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestoreDatabase() = Firebase.firestore

    @Provides
    fun provideIntroductionFragmentDataStore(
       @ApplicationContext context:Context
    ) : DataStore<Preferences> = PreferenceDataStoreFactory.create(
        produceFile = {
            context.preferencesDataStoreFile(Constants.INTRODUCTION_FRAGMENT_DS)
        }
    )

    @Provides
    @Singleton
    fun provideFirebaseCommon(firebaseAuth: FirebaseAuth , firebaseFirestore: FirebaseFirestore) =
        FirebaseCommon(firebaseFirestore , firebaseAuth)


    @Provides
    @Singleton
    fun provideStorage() = FirebaseStorage.getInstance().reference


}