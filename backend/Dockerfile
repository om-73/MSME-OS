# Build stage
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml ./pom.xml
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java -Xms64m -Xmx200m -XX:+UseSerialGC -XX:TieredStopAtLevel=1 -XX:ActiveProcessorCount=1 -XX:MaxMetaspaceSize=96m -XX:ReservedCodeCacheSize=32m -Xss256k -Dserver.port=${PORT:-8080} -Dserver.address=0.0.0.0 -jar app.jar"]
