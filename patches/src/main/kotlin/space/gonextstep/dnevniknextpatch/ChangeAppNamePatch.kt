package space.gonextstep.dnevniknextpatch

import app.morphe.patcher.patch.bytecodePatch

@Suppress("unused")
val changeAppNamePatch = bytecodePatch(
    name = "Смена названия приложения",
    description = "Изменяет название приложения на 'Дневник NP' (заглушка)",
    default = true
) {
    execute {
        println("Патч 'Смена названия приложения' применен!")
    }
}