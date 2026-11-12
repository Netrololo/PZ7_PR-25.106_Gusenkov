package com.example.imatchapp.data.datasource

import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Краткое описание: Интерфейс Retrofit для взаимодействия с REST API бэкенда.
 *
 * Дата создания: 12-11-2026
 * Автор создания: Гусенков
 */
interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body body: Map<String, String>): Map<String, String>

    @POST("auth/register")
    suspend fun register(@Body body: Map<String, String>): Map<String, String>

    @POST("games/schedule")
    suspend fun scheduleGame(@Body body: Map<String, String>)
}