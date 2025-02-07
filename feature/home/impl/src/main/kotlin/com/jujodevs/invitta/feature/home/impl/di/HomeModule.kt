package com.jujodevs.invitta.feature.home.impl.di

import com.jujodevs.invitta.core.coroutines.IO_COROUTINE
import com.jujodevs.invitta.feature.home.impl.data.DefaultEventRepository
import com.jujodevs.invitta.feature.home.impl.data.EventRemoteDatasource
import com.jujodevs.invitta.feature.home.impl.data.datasource.DefaultEventRemoteDatasource
import com.jujodevs.invitta.feature.home.impl.domain.EventRepository
import com.jujodevs.invitta.feature.home.impl.domain.GetEventsUseCase
import com.jujodevs.invitta.feature.home.impl.presentation.HomeScreen
import com.jujodevs.invitta.feature.home.impl.presentation.HomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val homeModule =
    module {
        single<EventRemoteDatasource> { DefaultEventRemoteDatasource(get()) }
        single<EventRepository> { DefaultEventRepository(get()) }
        single { GetEventsUseCase(get(), get()) }
        single { HomeScreen() }
        viewModel { HomeViewModel(get(), get(named(IO_COROUTINE))) }
    }
