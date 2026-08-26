# Build stage
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENV PORT=8081
ENTRYPOINT ["java", "-Xms64m", "-Xmx200m", "-XX:+UseSerialGC", "-XX:TieredStopAtLevel=1", "-XX:ActiveProcessorCount=1", "-XX:MaxMetaspaceSize=96m", "-XX:ReservedCodeCacheSize=32m", "-Xss256k", "-jar", "app.jar"]
