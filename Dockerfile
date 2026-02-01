FROM eclipse-temurin:21-jdk

WORKDIR /app
COPY . .

RUN apt-get update && apt-get install -y make

RUN chmod +x app/gradlew

# Сборка проекта без Checkstyle и без демона Gradle
RUN make build-daemonless

CMD ["make", "run-dist-daemonless"]