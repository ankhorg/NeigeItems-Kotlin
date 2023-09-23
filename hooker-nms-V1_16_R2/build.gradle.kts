repositories {
    maven("https://repo.codemc.io/repository/nms")
}

dependencies {
    compileOnly("org.bukkit:craftbukkit:1.16.3-R0.1-SNAPSHOT")
    compileOnly(project(":"))
}
