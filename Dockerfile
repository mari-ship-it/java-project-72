FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY . .

RUN make -C app run-dist

CMD ./build/install/app/bin/app
