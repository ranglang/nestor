import com.palantir.gradle.docker.DockerExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  val kotlinVersion = "1.3.21"
  id("org.springframework.boot") version "2.1.3.RELEASE"
  id("io.spring.dependency-management") version "1.0.7.RELEASE"
  kotlin("jvm") version kotlinVersion
  kotlin("plugin.spring") version kotlinVersion
  kotlin("plugin.jpa") version kotlinVersion
  id("com.github.ben-manes.versions") version "0.21.0"
  id("com.palantir.docker") version "0.21.0"
}

group = "org.gotson"
version = "2.0.0"

repositories {
  jcenter()
}

dependencies {
  implementation(kotlin("stdlib-jdk8"))
  implementation(kotlin("reflect"))

  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("org.springframework.boot:spring-boot-starter-mail")

  implementation("io.github.microutils:kotlin-logging:1.6.25")

  implementation("io.springfox:springfox-swagger2:2.9.2")
  implementation("io.springfox:springfox-swagger-ui:2.9.2")

  val jacksonVersion = "2.9.8"
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
  implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")

  implementation("net.sf.biweekly:biweekly:0.6.3")

  implementation("org.seleniumhq.selenium:selenium-java:3.141.59")

  runtimeOnly("com.h2database:h2:1.4.199")

  testImplementation("org.springframework.boot:spring-boot-starter-test") {
    exclude(module = "junit")
    exclude(module = "mockito-core")
  }
  testImplementation("org.junit.jupiter:junit-jupiter-api")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
  testImplementation("com.ninja-squad:springmockk:1.1.1")
  testImplementation("io.mockk:mockk:1.9.3")
}

tasks {
  withType<KotlinCompile> {
    kotlinOptions {
      jvmTarget = "1.8"
      freeCompilerArgs = listOf("-Xjsr305=strict")
    }
  }

  withType<Test> {
    useJUnitPlatform()
  }

  withType<ProcessResources> {
    filesMatching("application*.yml") {
      expand(project.properties)
    }
  }

  withType<Wrapper>() {
    gradleVersion = "5.3"
    distributionType = Wrapper.DistributionType.ALL
  }

  register<Copy>("unpack") {
    dependsOn(bootJar)
    from(zipTree(getByName("bootJar").outputs.files.singleFile))
    into("build/dependency")
  }
}

configure<DockerExtension> {
  name = "gotson/nestor:latest"
  copySpec.from(tasks.getByName("unpack").outputs).into("dependency")
  buildArgs(mapOf("DEPENDENCY" to "dependency"))
}
