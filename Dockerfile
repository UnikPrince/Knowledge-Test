FROM eclipse-temurin:21-jdk as builder
WORKDIR /workspace
COPY . .
RUN apt-get update && apt-get install -y maven && mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=builder /workspace/target/ai-interview-simulator-1.0.0.jar app.jar
COPY --from=builder /workspace/.env.example .env

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
