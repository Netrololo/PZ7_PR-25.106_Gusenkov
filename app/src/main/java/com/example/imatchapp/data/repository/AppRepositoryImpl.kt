package com.example.imatchapp.data.repository

import android.content.Context
import com.example.imatchapp.common.NetworkHelper
import com.example.imatchapp.data.datasource.ApiService
import com.example.imatchapp.domain.repository.AppRepository

/**
 * Краткое описание: Реализация репозитория, объединяющая работу с API и SharedPreferences.
 *
 * Дата создания: 12-11-2026
 * Автор создания: Гусенков
 */
class AppRepositoryImpl(
    private val apiService: ApiService,
    context: Context
) : AppRepository {
    private val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    /**
     * Выполняет запрос на авторизацию пользователя.
     *
     * @param email Почта пользователя.
     * @param password Пароль пользователя.
     * @return Result с токеном доступа или ошибкой.
     */
    override suspend fun login(email: String, password: String): Result<String> {
        return NetworkHelper.safeApiCall {
            val response = apiService.login(mapOf("email" to email, "password" to password))
            response["token"] ?: "mock_token"
        }
    }

    /**
     * Выполняет запрос на регистрацию нового пользователя.
     *
     * @param name Имя пользователя.
     * @param email Почта пользователя.
     * @param password Пароль пользователя.
     * @return Result с токеном или ошибкой.
     */
    override suspend fun register(name: String, email: String, password: String): Result<String> {
        return NetworkHelper.safeApiCall {
            val response = apiService.register(mapOf("name" to name, "email" to email, "password" to password))
            response["token"] ?: "mock_token"
        }
    }

    /**
     * Отправляет данные новой игры на сервер.
     *
     * @param title Название игры.
     * @param date Выбранная дата.
     * @param time Выбранное время.
     * @return Result выполнения операции.
     */
    override suspend fun scheduleGame(title: String, date: String, time: String): Result<Unit> {
        return NetworkHelper.safeApiCall {
            apiService.scheduleGame(mapOf("title" to title, "date" to date, "time" to time))
        }
    }

    /**
     * Проверяет, запускается ли приложение впервые.
     *
     * @return true, если первый запуск, иначе false.
     */
    override fun isFirstLaunch(): Boolean {
        return prefs.getBoolean("is_first_launch", true)
    }

    /**
     * Сохраняет отметку о завершении первого запуска.
     */
    override fun setFirstLaunchDone() {
        prefs.edit().putBoolean("is_first_launch", false).apply()
    }
}
