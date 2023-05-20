package com.actiangent.cuacagempa.core.data

suspend inline fun <T, V> networkBoundResource(
    crossinline getLocalData: suspend () -> T,
    crossinline shouldFetch: (T) -> Boolean,
    crossinline requestFromNetwork: suspend () -> V,
    crossinline saveNetworkRequest: suspend (V) -> Unit,
): T {
    // get current local data
    val data = getLocalData()

    // if we should fetch from network
    return if (shouldFetch(data)) {
        // request data from network
        val response = requestFromNetwork()

        // save requested data from network
        saveNetworkRequest(response)

        // return updated local data
        getLocalData()
    } else {
        // else return the current local data
        data
    }
}