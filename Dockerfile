FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY . .

RUN ./gradlew installDist

CMD ./build/install/app/bin/app
