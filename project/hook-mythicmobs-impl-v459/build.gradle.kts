val taboolib_version: String by project

dependencies {
    compileOnly(fileTree("libs"))
    compileOnly(project(":project:hook-mythicmobs"))
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
