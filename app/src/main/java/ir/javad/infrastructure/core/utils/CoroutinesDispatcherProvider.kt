package ir.javad.infrastructure.core.utils

import kotlinx.coroutines.CoroutineDispatcher

data class ThreadProvider(
    val main: CoroutineDispatcher,
    val computation: CoroutineDispatcher,
    val io: CoroutineDispatcher
)
