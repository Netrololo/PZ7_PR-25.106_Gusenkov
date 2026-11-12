package com.example.imatchapp.common

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File

/**
 * Краткое описание: Утилита для копирования PDF из assets в кэш и открытия его во внешнем просмотрщике.
 *
 * Дата создания: 12-11-2026
 * Автор создания: Гусенков
 */
object PdfHelper {
    /**
     * Открывает файл privacy_policy.pdf.
     *
     * @param context Контекст приложения.
     */
    fun openPrivacyPolicy(context: Context) {
        try {
            val file = File(context.cacheDir, "privacy_policy.pdf")
            context.assets.open("privacy_policy.pdf").use { input ->
                file.outputStream().use { output -> input.copyTo(output) }
            }
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(intent, "Открыть политику конфиденциальности"))
        } catch (e: Exception) {
            Toast.makeText(context, "Не удалось открыть PDF-файл", Toast.LENGTH_SHORT).show()
        }
    }
}
