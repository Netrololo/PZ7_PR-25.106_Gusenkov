package com.example.imatchapp.presentation.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import java.util.Calendar

/**
 * Краткое описание: Главный экран приложения (Landing Screen) с боковым выдвижным меню и нижней панелью.
 *
 * Дата создания: 12-11-2026
 * Автор создания: Гусенков
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingScreen(
    onOpenScheduleGame: () -> Unit,
    onLogout: () -> Unit
) {
    // ModalNavigationDrawer обеспечивает плавный выезд слева и закрытие свайпом влево
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Меню", style = MaterialTheme.typography.titleLarge)
                    // Закрытие меню по нажатию на крестик
                    IconButton(onClick = { scope.launch { drawerState.close() } }) {
                        Icon(Icons.Default.Close, contentDescription = "Закрыть меню")
                    }
                }
                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text("Профиль") },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() } }
                )
                NavigationDrawerItem(
                    label = { Text("Мои игры") },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() } }
                )
                NavigationDrawerItem(
                    label = { Text("Выйти") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onLogout()
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Landing Screen") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Открыть меню")
                        }
                    }
                )
            },
            bottomBar = {
                BottomAppBar {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Главная")
                        // Центральная кнопка перехода на Schedule Game
                        FloatingActionButton(onClick = onOpenScheduleGame) {
                            Icon(Icons.Default.Add, contentDescription = "Schedule Game")
                        }
                        Text("Матчи")
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Добро пожаловать в IMATCH!\nНажмите '+' внизу, чтобы запланировать игру.",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

/**
 * Краткое описание: Экран планирования игры (Schedule Game) с системным выбором даты и времени.
 *
 * Дата создания: 12-11-2026
 * Автор создания: Гусенков
 */
@Composable
fun ScheduleGameScreen(
    onPublishClick: (title: String, date: String, time: String) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    var title by rememberSaveable { mutableStateOf("") }
    var selectedDate by rememberSaveable { mutableStateOf("Выберите дату") }
    var selectedTime by rememberSaveable { mutableStateOf("Выберите время") }

    // Стандартный системный DatePickerDialog
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, day ->
            selectedDate = String.format("%02d.%02d.%04d", day, month + 1, year)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

// Стандартный системный TimePickerDialog
    val timePickerDialog = TimePickerDialog(
        context,
        { _, hour, minute ->
            selectedTime = String.format("%02d:%02d", hour, minute)
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Schedule Game", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Название / описание игры") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedButton(
            onClick = { datePickerDialog.show() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(selectedDate)
        }

        OutlinedButton(
            onClick = { timePickerDialog.show() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(selectedTime)
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { onPublishClick(title, selectedDate, selectedTime) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Publish")
        }
    }
}

/**
 * Краткое описание: Экран успешного создания игры (Success) с возвратом на главный экран.
 *
 * Дата создания: 12-11-2026
 * Автор создания: Гусенков
 */
@Composable
fun SuccessScreen(
    onBackToLanding: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Успех",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Success!", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Игра успешно опубликована и сохранена.")
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onBackToLanding,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Landing Screen")
        }
    }
}