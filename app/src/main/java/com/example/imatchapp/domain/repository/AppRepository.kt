package com.example.imatchapp.domain.repository

/**
 * Краткое описание: Интерфейс репозитория для работы с аутентификацией, играми и локальными настройками.
 *
 * Дата создания: 12-11-2026
 * Автор создания: Гусенков
 */
interface AppRepository {
    suspend fun login(email: String, password: String): Result<String>
    suspend fun register(name: String, email: String, password: String): Result<String>
    suspend fun scheduleGame(title: String, date: String, time: String): Result<Unit>
    fun isFirstLaunch(): Boolean
    fun setFirstLaunchDone()
}
