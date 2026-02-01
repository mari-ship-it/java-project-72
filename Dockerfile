FROM eclipse-temurin:21-jdk

WORKDIR /app
COPY . .

RUN chmod +x app/gradlew

RUN make build

CMD ["make", "run-dist"]