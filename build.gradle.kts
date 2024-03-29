plugins {
    id("java")
}

group = "ru.noion"
version = "0.1.1"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "ru.noion.jsalmon.Salmon"
    }
    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absolutePath))
    }
}