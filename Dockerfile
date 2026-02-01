FROM eclipse-temurin:21-jdk

RUN apt-get update && apt-get install -y make

WORKDIR /app

COPY /app .

RUN make -C app run-dist

CMD ./build/install/app/bin/app
