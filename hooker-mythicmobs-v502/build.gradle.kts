val taboolib_version: String by project

dependencies {
    compileOnly(fileTree("libs"))
    compileOnly(project(":"))
}
