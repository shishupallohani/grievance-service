# ------------ STAGE 1: BUILD  ------------
FROM maven:3-eclipse-temurin-25 AS builder
WORKDIR /app

# Copy root POM + modules
COPY pom.xml .
COPY api-module ./api-module
COPY common-module ./common-module
COPY data-module ./data-module
COPY service-module ./service-module
COPY main-app ./main-app

# Download dependencies (cache optimization)
RUN mvn -B -q dependency:go-offline

# Build full multi-module project
RUN mvn -B clean package -DskipTests

# ------------ STAGE 2: RUNTIME IMAGE ------------
FROM eclipse-temurin:25-jre-jammy
WORKDIR /app

RUN apt-get update && apt-get install -y \
    libfreetype6 \
    fonts-dejavu-core \
    fontconfig \
  && rm -rf /var/lib/apt/lists/*

# Copy the final jar from main module
COPY --from=builder /app/main-app/target/*.jar /app/app.jar

# Copy environment file
COPY .env /app/.env

EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=dev
ENV JAVA_OPTS=""

CMD ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar --spring.profiles.active=$SPRING_PROFILES_ACTIVE"]
