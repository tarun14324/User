package com.example.userapp.util

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel

/**
 * Creates a [Channel] with conflated behavior.
 */
@Suppress("NOTHING_TO_INLINE")
inline fun <T> conflatedChannel() = Channel<T>(Channel.CONFLATED)

/**
 * Safely sends a value to the channel.
 */
fun <T> Channel<T>.sendValue(value: T) = runCatching { trySend(value) }

/**
 * Collects from a [ReceiveChannel] and executes the [action] for each received value.
 */
suspend inline fun ReceiveChannel<Unit>.collect(crossinline action: suspend () -> Unit) {
    try {
        val iterator = iterator()
        while (iterator.hasNext()) {
            iterator.next()
            action()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        throw e
    }
}
