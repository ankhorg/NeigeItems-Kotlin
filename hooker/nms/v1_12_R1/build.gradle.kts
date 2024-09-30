repositories {
    maven("https://repo.codemc.io/repository/nms")
}

dependencies {
    compileOnly("org.bukkit:craftbukkit:1.12.2-R0.1-SNAPSHOT")
    compileOnly(project(":hooker:nms:v1_14_R1"))
    compileOnly(project(":hooker:nms:v1_16_R2"))
    compileOnly(project(":"))
}
