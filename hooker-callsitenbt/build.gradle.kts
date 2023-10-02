repositories {
    maven("https://repo.codemc.io/repository/nms")
}

dependencies {
    compileOnly("org.spigotmc:spigot:1.12.2-R0.1-SNAPSHOT")
    compileOnly(fileTree("${rootProject.rootDir}/libs/callsite-nbt-1.0-dev-SNAPSHOT-fat.jar"))
}
