# Estágio 1: Build da aplicação
FROM maven:3.9.6-eclipse-temurin-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# Estágio 2: Execução leve
FROM eclipse-temurin:17-jdk-alpine
COPY --from=build /target/*.jar app.jar

# Configuração mágica: Limita a memória do Java para caber nos 512MB do Render
ENV JAVA_OPTS="-Xmx300m -Xss512k"

EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app.jar"]