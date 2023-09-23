repositories {
    maven("https://repo.codemc.io/repository/nms")
}

dependencies {
    compileOnly("org.bukkit:craftbukkit:1.12.2-R0.1-SNAPSHOT")
    compileOnly(project(":"))
}
