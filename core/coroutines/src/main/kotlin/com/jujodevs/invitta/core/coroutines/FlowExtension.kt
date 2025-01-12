package com.jujodevs.invitta.core.coroutines

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf

inline fun <reified T> List<Flow<T>>.combineAll(): Flow<List<T>> =
    if (this.isEmpty()) {
        flowOf(emptyList())
    } else {
        combine(this) { it.toList() }
    }
