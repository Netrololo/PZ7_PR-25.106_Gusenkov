package com.example.imatchapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.imatchapp.data.datasource.ApiService
import com.example.imatchapp.data.repository.AppRepositoryImpl
import com.example.imatchapp.domain.repository.AppRepository
import com.example.imatchapp.presentation.components.ErrorBanner
import com.example.imatchapp.presentation.screens.*
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Краткое описание: Главная точка входа в приложение, настройка навигации и глобального вывода ошибок.
 *
 * Дата создания: 12-11-2026
 * Автор создания: Гусенков
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Инициализация Retrofit и репозитория через интерфейс AppRepository
        val retrofit = Retrofit.Builder()
            .baseUrl("https://example.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiService = retrofit.create(ApiService::class.java)
        val repository: AppRepository = AppRepositoryImpl(apiService, applicationContext)

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    val scope = rememberCoroutineScope()
                    // rememberSaveable сохраняет состояние ошибки при повороте и сворачивании
                    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }

                    Box(modifier = Modifier.fillMaxSize()) {
                        NavHost(
                            navController = navController,
                            startDestination = "splash"
                        ) {
                            composable("splash") {
                                SplashScreen(
                                    isFirstLaunch = repository.isFirstLaunch(),
                                    onNavigateToOnboarding = {
                                        navController.navigate("onboarding") {
                                            popUpTo("splash") { inclusive = true }
                                        }
                                    },
                                    onNavigateToLogin = {
                                        navController.navigate("login") {
                                            popUpTo("splash") { inclusive = true }
                                        }
                                    }
                                )
                            }

                            composable("onboarding") {
                                OnboardingScreen(
                                    onFinishOnboarding = {
                                        repository.setFirstLaunchDone()
                                        navController.navigate("register") {
                                            popUpTo("onboarding") { inclusive = true }
                                        }
                                    }
                                )
                            }

                            composable("register") {
                                RegisterScreen(
                                    onRegisterClick = { name, email, pass ->
                                        scope.launch {
                                            val result = repository.register(name, email, pass)
                                            result.onFailure { error ->
                                                // Вывод баннера ошибки на 5 секунд
                                                errorMessage = error.message
                                            }
// Переход на главный экран для демонстрации функционала
                                            navController.navigate("landing") {
                                                popUpTo("register") { inclusive = true }
                                            }
                                        }
                                    },
                                    onNavigateToLogin = { navController.navigate("login") }
                                )
                            }

                            composable("login") {
                                LoginScreen(
                                    onLoginClick = { email, pass ->
                                        scope.launch {
                                            val result = repository.login(email, pass)
                                            result.onFailure { error ->
                                                errorMessage = error.message
                                            }
                                            navController.navigate("landing") {
                                                popUpTo("login") { inclusive = true }
                                            }
                                        }
                                    },
                                    onNavigateToRegister = { navController.navigate("register") }
                                )
                            }

                            composable("landing") {
                                LandingScreen(
                                    onOpenScheduleGame = { navController.navigate("schedule") },
                                    onLogout = {
                                        navController.navigate("login") {
                                            popUpTo("landing") { inclusive = true }
                                        }
                                    }
                                )
                            }

                            composable("schedule") {
                                ScheduleGameScreen(
                                    onPublishClick = { title, date, time ->
                                        scope.launch {
                                            val result = repository.scheduleGame(title, date, time)
                                            result.onFailure { error ->
                                                errorMessage = error.message
                                            }
                                            navController.navigate("success")
                                        }
                                    }
                                )
                            }

                            composable("success") {
                                SuccessScreen(
                                    onBackToLanding = {
                                        navController.navigate("landing") {
                                            popUpTo("landing") { inclusive = true }
                                        }
                                    }
                                )
                            }
                        }

// Глобальный баннер ошибки на 5 секунд поверх любого экрана
                        Box(modifier = Modifier.align(Alignment.TopCenter)) {
                            ErrorBanner(
                                errorText = errorMessage,
                                onDismiss = { errorMessage = null }
                            )
                        }
                    }
                }
            }
        }
    }
}
