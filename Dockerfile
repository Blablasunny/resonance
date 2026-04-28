# Multi-stage build для минимального размера образа

# Stage 1: Builder - сборка приложения
FROM gradle:8.5-jdk21-alpine AS builder

WORKDIR /app

# Копируем gradle wrapper и конфигурацию
COPY gradle gradle
COPY gradlew .
COPY settings.gradle.kts .
COPY build.gradle.kts .

# Загружаем зависимости (этот слой кешируется отдельно)
RUN ./gradlew dependencies --no-daemon

# Копируем исходный код
COPY src src

# Собираем приложение (без тестов для ускорения)
RUN ./gradlew bootJar --no-daemon -x test

# Stage 2: Runtime - минимальный образ для запуска
FROM eclipse-temurin:21-jre-alpine

# Метаданные
LABEL maintainer="resonance-team"
LABEL description="Resonance Spring Boot Application"
LABEL version="1.0"

# Создаём непривилегированного пользователя для безопасности
RUN addgroup -S spring && adduser -S spring -G spring

WORKDIR /app

# Копируем только собранный JAR из builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Меняем владельца файла
RUN chown spring:spring app.jar

# Переключаемся на непривилегированного пользователя
USER spring:spring

# Открываем порт приложения
EXPOSE 8088

# Health check для Docker
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --quiet --tries=1 --spider http://localhost:8088/actuator/health || exit 1

# Запуск приложения с JVM параметрами
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE:-prod} -jar app.jar"]
