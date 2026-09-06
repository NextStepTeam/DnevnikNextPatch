package space.gonextstep.dnevniknextpatch

import app.morphe.patcher.patch.rawResourcePatch
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

@Suppress("unused")
val replaceMosLogosPatch = rawResourcePatch(
    name = "Замена логотипов МЭШ",
    description = "Заменяет логотипы mos_logo.png, mos_logo_fs.png и mos_logo_ny.png на кастомные",
    default = true
) {
    execute {
        println("✅ Патч 'Замена логотипов' применен!")
        
        // Список файлов для замены
        val filesToReplace = listOf(
            "res/drawable/mos_logo.png",
            "res/drawable/mos_logo_fs.png",
            "res/drawable/mos_logo_ny.png"
        )
        
        // Путь к ресурсам патча
        val resourcesDir = File("patches/src/main/resources/images")
        
        for (targetPath in filesToReplace) {
            try {
                val fileName = targetPath.substringAfterLast("/")
                val newFile = File(resourcesDir, fileName)
                
                if (!newFile.exists()) {
                    println("⚠️ Файл не найден: ${newFile.absolutePath}")
                    continue
                }
                
                // Получаем файл из APK
                val targetFile = get(targetPath)
                
                if (targetFile == null) {
                    println("⚠️ Файл не найден в APK: $targetPath")
                    continue
                }
                
                // Заменяем содержимое
                Files.copy(newFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
                println("✅ Заменен: $targetPath")
                
            } catch (e: Exception) {
                println("❌ Ошибка при замене $targetPath: ${e.message}")
            }
        }
    }
}