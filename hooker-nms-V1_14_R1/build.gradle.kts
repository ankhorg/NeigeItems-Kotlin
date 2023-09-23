repositories {
    maven("https://repo.codemc.io/repository/nms")
}

dependencies {
    compileOnly("org.bukkit:craftbukkit:1.14.4-R0.1-SNAPSHOT")
    compileOnly(project(":"))
}
