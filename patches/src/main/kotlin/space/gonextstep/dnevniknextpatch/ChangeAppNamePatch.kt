package space.gonextstep.dnevniknextpatch

import app.morphe.patcher.patch.rawResourcePatch
import app.morphe.patcher.patch.compatibleWith

@Suppress("unused")
val changeAppNamePatch = rawResourcePatch(
    name = "Смена названия приложения",
    description = "Изменяет название приложения на 'Дневник NP' через замену в strings.xml",
    default = true
) {
    compatibleWith("ru.mes.dnevnik")
    
    execute {
        // 1. Находим нужный файл ресурсов
        val targetFile = context.apkFiles.firstOrNull { 
            it.name == "strings.xml" && it.path.startsWith("res/values")
        } ?: error("strings.xml not found in APK")

        // 2. Читаем содержимое как строку
        val content = targetFile.readText()
        
        // 3. Заменяем название (учитываем возможные варианты)
        val newContent = content.replace(
            "Дневник МЭШ", 
            "Dnevnik NP"
        )
        
        // 4. Если строка изменилась — записываем обратно
        if (newContent != content) {
            targetFile.writeText(newContent)
            println("✅ Название приложения изменено на 'Дневник NP'")
        } else {
            println("❌ Строка 'Дневник МЭШ' не найдена в strings.xml")
        }
    }
}