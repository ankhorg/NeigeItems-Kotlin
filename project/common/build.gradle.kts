val taboolib_version: String by project

dependencies {
    compileOnly(fileTree("libs"))
    compileOnly("com.comphenix.protocol:ProtocolLib:4.8.0")
    compileOnly("me.clip:placeholderapi:2.10.9")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("org.ow2.asm:asm:9.4")
    compileOnly("org.javassist:javassist:3.20.0-GA")

    compileOnly("org.openjdk.nashorn:nashorn-core:15.4")
    compileOnly("com.alibaba.fastjson2:fastjson2-kotlin:2.0.25")
    compileOnly("org.neosearch.stringsearcher:multiple-string-searcher:0.1.1")
    compileOnly("org.apache.maven:maven-model:3.9.1")

    compileOnly(project(":project:hook-mythicmobs"))
    compileOnly(project(":project:hook-mythicmobs-impl-v459"))
    compileOnly(project(":project:hook-mythicmobs-impl-v490"))
    compileOnly(project(":project:hook-mythicmobs-impl-v502"))
    compileOnly(project(":project:hook-mythicmobs-impl-v510"))
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
