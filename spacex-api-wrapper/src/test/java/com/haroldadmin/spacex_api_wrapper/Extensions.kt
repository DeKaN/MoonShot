package com.haroldadmin.spacex_api_wrapper

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import java.net.URL

fun Any.getResource(path: String): URL = this::class.java.getResource(path)

fun MockResponse.fromFile(path: String, responseCode: Int = 200) = this.apply {
    setBody(getResource(path).readText())
    setResponseCode(responseCode)
}

fun MockWebServer.enqueue(responseCreator: MockResponse.() -> MockResponse) {
    this.enqueue(responseCreator(MockResponse()))
}