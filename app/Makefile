.PHONY: setup clean build install run-dist run dev test report lint sonar update-deeps image-build image-push build-run
.DEFAULT_GOAL := build-run

setup:
	./gradlew wrapper --gradle-version 8.14

clean:
	./gradlew clean

build:
	./gradlew clean build

build-daemonless:
	./gradlew clean build --no-daemon -x checkstyleMain

install:
	./gradlew clean install

run-dist:
	./gradlew installDist

run-dist-daemonless:
	./gradlew installDist --no-daemon

run:
	./gradlew run

test:
	./gradlew test

report:
	./gradlew jacocoTestReport

lint:
	./gradlew checkstyleMain

sonar:
	./gradlew build sonar --info

update-deeps:
	./gradlew refreshVersions

image-build:
	docker build -t hexletcomponents/java-javalin-example:latest .

image-push:
	docker push hexletcomponents/java-javalin-example:latest

build-run: build run