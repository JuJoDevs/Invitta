package com.jujodevs.invitta.library.userRepository.impl.di

import com.jujodevs.invitta.library.userRepository.api.UserRepository
import com.jujodevs.invitta.library.userRepository.impl.data.DefaultUserRepository
import com.jujodevs.invitta.library.userRepository.impl.data.UserAuthDatasource
import com.jujodevs.invitta.library.userRepository.impl.data.UserRemoteDatasource
import com.jujodevs.invitta.library.userRepository.impl.data.datasource.DefaultUserAuthDatasource
import com.jujodevs.invitta.library.userRepository.impl.data.datasource.DefaultUserRemoteDatasource
import org.koin.dsl.module

val userModule =
    module {
        single<UserAuthDatasource> { DefaultUserAuthDatasource(get()) }
        single<UserRemoteDatasource> { DefaultUserRemoteDatasource(get()) }
        single<UserRepository> { DefaultUserRepository(get(), get()) }
    }
