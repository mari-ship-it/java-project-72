FROM eclipse-temurin:21-jdk

WORKDIR /app
COPY . .

RUN chmod +x gradlew
RUN ./gradlew installDist

CMD ["./build/install/app/bin/app"]