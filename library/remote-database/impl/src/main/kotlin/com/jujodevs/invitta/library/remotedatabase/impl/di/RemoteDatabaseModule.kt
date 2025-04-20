package com.jujodevs.invitta.library.remotedatabase.impl.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jujodevs.invitta.library.remotedatabase.api.RemoteEventDatabase
import com.jujodevs.invitta.library.remotedatabase.api.RemoteUserDatabase
import com.jujodevs.invitta.library.remotedatabase.impl.DummyRemoteEventDatabase
import com.jujodevs.invitta.library.remotedatabase.impl.FirestoreRemoteUserDatabase
import org.koin.dsl.module

val remoteDatabaseModule =
    module {
        single<FirebaseFirestore> { Firebase.firestore }
        single<RemoteEventDatabase> { DummyRemoteEventDatabase() }
        single<RemoteUserDatabase> { FirestoreRemoteUserDatabase(get(), get()) }
    }
