import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.16"
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    paperweight.paperDevBundle("1.21.3-R0.1-SNAPSHOT")
    compileOnly(project(":"))
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

tasks {
    assemble {
        paperweight.reobfArtifactConfiguration =
            io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION
    }

//    build {
//        dependsOn(reobfJar)
//    }

    compileJava {
        options.release.set(21)
    }

    compileKotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }
}
