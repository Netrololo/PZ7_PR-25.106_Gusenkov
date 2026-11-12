package com.example.imatchapp.common

import retrofit2.HttpException
import java.io.IOException

/**
 * Краткое описание: Обертка для безопасного выполнения сетевых запросов и перехвата ошибок.
 *
 * Дата создания: 12-11-2026
 * Автор создания: Гусенков
 */
object NetworkHelper {
    /**
     * Выполняет сетевой запрос и возвращает Result с данными или текстом ошибки.
     *
     * @param apiCall Лямбда-функция с вызовом метода API.
     * @return Объект Result<T> с результатом или исключением.
     */
    suspend fun <T> safeApiCall(apiCall: suspend () -> T): Result<T> {
        return try {
            Result.success(apiCall())
        } catch (e: IOException) {
            Result.failure(Exception("Отсутствует соединение с сетью Интернет"))
        } catch (e: HttpException) {
            Result.failure(Exception("Ошибка сервера: код ${e.code()}"))
        } catch (e: Exception) {
            Result.failure(Exception(e.localizedMessage ?: "Неизвестная ошибка"))
        }
        }
    }