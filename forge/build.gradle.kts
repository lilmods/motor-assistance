import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.text.SimpleDateFormat
import java.util.*

buildscript {
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinGradlePlugin")
    }
}

plugins {
    id("org.jetbrains.kotlin.jvm") version kotlinVersion
    id("net.minecraftforge.gradle") version forgeGradlePlugin
}

group = "$modGroup.forge"
version = "$forgeModVersion-forge"

repositories {
    maven("https://thedarkcolour.github.io/KotlinForForge/") // Kotlin for Forge
    maven("https://maven.shedaniel.me/") // Cloth config
}

val inJar: Configuration = configurations.create("inJar")
configurations.minecraftLibrary.get().extendsFrom(inJar)

dependencies {
    minecraft("net.minecraftforge:forge:$minecraftVersion-$forgeVersion")
    implementation("thedarkcolour:kotlinforforge:$kotlinForForge")
    api(fg.deobf("me.shedaniel.cloth:cloth-config-forge:$clothConfigVersion"))
    inJar(project(":common")) {
        exclude("org.jetbrains", "annotations")
        exclude("org.jetbrains.kotlin")
    }
}

val Project.minecraft: net.minecraftforge.gradle.common.util.MinecraftExtension
    get() = extensions.getByType()

minecraft.let {
    it.mappings("official", minecraftVersion)
    it.runs {
        create("client") {
            workingDirectory(project.file("run"))
            property("forge.logging.console.level", "debug")
            mods {
                create(modId) {
                    source(
                        sourceSets.main.get()
                            .apply {
                                resources {
                                    srcDirs.plus(
                                        // TODO: it's probably not the right way to do that
                                        // Add common module resources in this module at runtime
                                        resources {
                                            srcDirs(project(":common").sourceSets.main.get().resources.srcDirs)
                                        },
                                    )
                                }
                            },
                    )
                }
            }
        }
    }
}

tasks {
    val javaVersion = JavaVersion.valueOf("VERSION_$jvmTarget")
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        sourceCompatibility = javaVersion.toString()
        targetCompatibility = javaVersion.toString()
        if (JavaVersion.current().isJava9Compatible) {
            options.release.set(javaVersion.toString().toInt())
        }
    }

    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = javaVersion.toString()
        }
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(javaVersion.toString()))
        }
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }

    withType<Jar> {
        doFirst {
            from(
                inJar
                    .filterNot { it.absolutePath.contains("org.jetbrains") }
                    .filterNot { it.absolutePath.contains("kotlin-stdlib") }
                    .map { if (it.isDirectory) it else zipTree(it) },
            )
        }
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        archiveBaseName.set(modId)
        manifest {
            attributes(
                mapOf(
                    "Implementation-Title" to project.name,
                    "Implementation-Version" to project.version,
                    "Implementation-Timestamp" to SimpleDateFormat("yyyy-MM-dd").format(Date()),
                ),
            )
        }
        finalizedBy("reobfJar")
    }
}
