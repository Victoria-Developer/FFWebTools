# Stage 1: Build the JAR using Maven
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the JAR
FROM eclipse-temurin:17-jre-alpine
VOLUME /tmp
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["sh", "-c", "java  -XX:+UseContainerSupport  -XX:+UseSerialGC -Xms128M  -Xmx192M  -Xss256k   -XX:MaxRAMPercentage=90  -XX:+ExitOnOutOfMemoryError -jar /app.jar"]
EXPOSE 8080