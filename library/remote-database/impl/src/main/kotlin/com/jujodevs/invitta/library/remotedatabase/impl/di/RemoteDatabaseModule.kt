package com.jujodevs.invitta.library.remotedatabase.impl.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jujodevs.invitta.library.remotedatabase.api.RemoteEventDatabase
import com.jujodevs.invitta.library.remotedatabase.api.RemoteGroupDatabase
import com.jujodevs.invitta.library.remotedatabase.api.RemoteMemberDatabase
import com.jujodevs.invitta.library.remotedatabase.api.RemoteNucleusDatabase
import com.jujodevs.invitta.library.remotedatabase.api.RemoteUserDatabase
import com.jujodevs.invitta.library.remotedatabase.impl.FirestoreRemoteEventDatabase
import com.jujodevs.invitta.library.remotedatabase.impl.FirestoreRemoteGroupDatabase
import com.jujodevs.invitta.library.remotedatabase.impl.FirestoreRemoteMemberDatabase
import com.jujodevs.invitta.library.remotedatabase.impl.FirestoreRemoteNucleusDatabase
import com.jujodevs.invitta.library.remotedatabase.impl.FirestoreRemoteUserDatabase
import org.koin.dsl.module

val remoteDatabaseModule =
    module {
        single<FirebaseFirestore> { Firebase.firestore }
        single<RemoteEventDatabase> { FirestoreRemoteEventDatabase(get(), get()) }
        single<RemoteUserDatabase> { FirestoreRemoteUserDatabase(get(), get()) }
        single<RemoteGroupDatabase> { FirestoreRemoteGroupDatabase(get()) }
        single<RemoteNucleusDatabase> { FirestoreRemoteNucleusDatabase(get()) }
        single<RemoteMemberDatabase> { FirestoreRemoteMemberDatabase(get()) }
    }
