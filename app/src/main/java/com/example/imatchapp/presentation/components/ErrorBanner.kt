package com.example.imatchapp.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

/**
 * Краткое описание: Всплывающий баннер для отображения сетевых ошибок в течение 5 секунд.
 *
 * Дата создания: 12-11-2026
 * Автор создания: Гусенков
 */
@Composable
fun ErrorBanner(
    errorText: String?,
    onDismiss: () -> Unit
) {
    if (errorText != null) {
        LaunchedEffect(errorText) {
            delay(5000L)
            onDismiss()
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            color = Color(0xFFD32F2F),
            shape = MaterialTheme.shapes.medium,
            shadowElevation = 6.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = errorText,
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Закрыть ошибку",
                        tint = Color.White
                    )
                }
            }
        }
    }
}