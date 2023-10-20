import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.moowork.gradle.node.npm.NpmTask

plugins {
	id("org.springframework.boot") version "2.4.3"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	id("com.moowork.node") version "1.3.1"
	kotlin("jvm") version "1.4.30"
	kotlin("plugin.spring") version "1.4.30"
}

group = "com.dajati"
version = "0.6.2"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.optaplanner:optaplanner-spring-boot-starter:8.4.1.Final")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

node {
	download = false

	// Set the work directory for unpacking node
	workDir = file("${project.buildDir}/nodejs")

	// Set the work directory for NPM
	npmWorkDir = file("${project.buildDir}/npm")
}

tasks.register<NpmTask>("appNpmInstall") {
	description = "Installs all dependencies from package.json"
	setWorkingDir(file("${project.projectDir}/src/main/webapp"))
	setArgs(listOf("install"))
}

tasks.register<NpmTask>("appNpmBuild") {
	dependsOn("appNpmInstall")
	description = "Builds production version of the webapp"
	setWorkingDir(file("${project.projectDir}/src/main/webapp"))
	setArgs(listOf("run", "build"))
}

tasks.register<Copy>("copyWebApp") {
	dependsOn("appNpmBuild")
	from("src/main/webapp/build")
	into("build/resources/main/static/.")
}

tasks.named("build") {
	dependsOn("copyWebApp")
}
