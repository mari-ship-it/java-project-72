FROM eclipse-temurin:21-jdk

WORKDIR /app
COPY . .

RUN apt-get update && apt-get install -y make

RUN chmod +x app/gradlew
RUN make build

CMD ["make", "run-dist"]