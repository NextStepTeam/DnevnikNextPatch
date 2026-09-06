package space.gonextstep.dnevniknextpatch

import app.morphe.patcher.patch.resourcePatch
import app.morphe.patcher.patch.stringOption
import app.morphe.util.asSequence
import app.morphe.util.findElementByAttributeValue
import org.w3c.dom.Element
import java.util.logging.Logger

@Suppress("unused")
val cloneDnevnikPatch = resourcePatch(
    name = "Клонирование Дневника",
    description = "Изменяет имя пакета приложения, позволяя установить несколько копий. ",
    default = true
) {
    compatibleWith("ru.mes.dnevnik")
    // Опция для свободного ввода package name
    val packageNameOption = stringOption(
        key = "packageName",
        default = "space.gonextstep.dnevniknp",
        title = "Новое имя пакета",
        description = "Введите новое имя пакета для клонированного приложения",
        required = true
    ) {
        it!!.matches(Regex("^[a-z]\\w*(\\.[a-z]\\w*)+$"))
    }

    finalize {
        val originalPackageName = packageMetadata.packageName
        val newPackageName = packageNameOption.value!!

        Logger.getLogger(this::class.java.name).info(
            "Клонирование: $originalPackageName → $newPackageName"
        )

        // Обновляем AndroidManifest.xml
        document("AndroidManifest.xml").use { document ->
            document.documentElement.setAttribute("package", newPackageName)

            // Обновляем permissions
            val permissions = document.getElementsByTagName("permission")
            val usesPermissions = document.getElementsByTagName("uses-permission")

            permissions.asSequence().map { it as Element }.forEach { permission ->
                val oldName = permission.getAttribute("android:name")
                if (oldName.startsWith(originalPackageName)) {
                    val newName = oldName.replaceFirst(originalPackageName, newPackageName)
                    permission.setAttribute("android:name", newName)
                    
                    // Обновляем соответствующий <uses-permission>
                    usesPermissions
                        .findElementByAttributeValue("android:name", oldName)
                        ?.setAttribute("android:name", newName)
                }
            }

            // Обновляем providers (если есть)
            val providers = document.getElementsByTagName("provider")
            for (i in 0 until providers.length) {
                val provider = providers.item(i) as Element
                val authorities = provider.getAttribute("android:authorities")
                if (authorities.contains(originalPackageName)) {
                    val newAuthorities = authorities.replace(originalPackageName, newPackageName)
                    provider.setAttribute("android:authorities", newAuthorities)
                }
            }
        }

        // Обновляем ссылки в ресурсах (если есть)
        try {
            document("res/values/strings.xml").use { document ->
                val children = document.documentElement.childNodes
                for (i in 0 until children.length) {
                    val node = children.item(i) as? Element ?: continue
                    val text = node.textContent
                    if (text.contains(originalPackageName)) {
                        node.textContent = text.replace(originalPackageName, newPackageName)
                    }
                }
            }
        } catch (e: Exception) {
            Logger.getLogger(this::class.java.name).info(
                "Файл res/values/strings.xml не найден или не содержит ссылок на package name"
            )
        }

        Logger.getLogger(this::class.java.name).info(
            "✅ Package name изменен на: $newPackageName"
        )
    }
}