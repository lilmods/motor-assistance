plugins {
    kotlin("jvm")
}

group = property("maven_group")!!.toString() + ".core"
version = property("mod_version")!!

repositories {
    mavenCentral()
}

dependencies {
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }
}
