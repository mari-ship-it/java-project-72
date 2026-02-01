FROM eclipse-temurin:21-jdk

WORKDIR /app
COPY . .

RUN apt-get update && apt-get install -y gradle
RUN gradle installDist

CMD ["./build/install/app/bin/app"]