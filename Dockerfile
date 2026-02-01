FROM eclipse-temurin:21-jdk

WORKDIR /app
COPY . .

RUN apt-get update && apt-get install -y gradle
RUN gradle build

CMD ["sh", "-c", "java -jar build/libs/*.jar"]