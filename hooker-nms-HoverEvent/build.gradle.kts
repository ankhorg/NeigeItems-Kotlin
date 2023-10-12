dependencies {
    compileOnly(project(":"))
    compileOnly(fileTree("${rootProject.rootDir}/libs/callsite-nbt-1.0-dev-SNAPSHOT-fat.jar"))
}
