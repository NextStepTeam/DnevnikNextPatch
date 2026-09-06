group = "space.gonextstep.dnevniknextpatch"

patches {
    about {
        name = "DnevnikNextPatch"
        description = "Patch dnevnik the best"
        source = "git@github.com:NextStepTeam/DnevnikNextPatch.git"
        author = "NextStep"
        contact = "help@gonextstep.space"
        website = "https://gonextstep.space"
        license = "GPLv3"
    }
}

dependencies {
    // Основные зависимости Morphe
    implementation("app.morphe:morphe-patches-library:1.6.2")
    //implementation("app.morphe:morphe-library:1.4.0")
    implementation("app.morphe:morphe-patcher:1.12.0")

    
    // Остальные
    implementation("com.google.guava:guava:32.1.3-jre")
    implementation("com.google.code.gson:gson:2.10.1")
}

tasks {
    register<JavaExec>("generatePatchesList") {
        description = "Generate patches list"
        dependsOn(build)
        classpath = sourceSets["main"].runtimeClasspath
        mainClass.set("util.PatchListGeneratorKt")
    }

    publish {
        dependsOn("generatePatchesList")
    }
}
