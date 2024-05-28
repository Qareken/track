plugins {
	java
	id("org.springframework.boot") version "3.2.5"
	id("io.spring.dependency-management") version "1.1.4"
}
group = "com.example"
version = "0.0.1-SNAPSHOT"
java {
	sourceCompatibility = JavaVersion.VERSION_17
}
configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}
repositories {
	mavenCentral()
}
dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation ("org.springframework.boot:spring-boot-starter-validation")
	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	annotationProcessor("org.projectlombok:lombok")
	implementation("org.mapstruct:mapstruct:1.5.5.Final")
	implementation("org.testcontainers:mongodb")
	implementation("org.testcontainers:junit-jupiter")
	implementation ("org.springframework.boot:spring-boot-starter-security")
	implementation("io.jsonwebtoken:jjwt:0.9.1")
	implementation ("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("redis.clients:jedis")
	implementation("com.fasterxml.jackson.core:jackson-databind:2.16.1")
	implementation("javax.xml.bind:jaxb-api:2.3.1")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
	testImplementation ("org.springframework.security:spring-security-test")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
	testImplementation("org.assertj:assertj-core:3.25.3")

}
tasks.withType<Test> {
	useJUnitPlatform()
}
