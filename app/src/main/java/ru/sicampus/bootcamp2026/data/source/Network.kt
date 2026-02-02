package ru.sicampus.bootcamp2026.data.source

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation

object Network {
    const val HOST= "http://Localhost:8080"

    val client by lazy {
        HttpClient(CIO) {  }
            install(ContentNegotiation){

            }
    }
}