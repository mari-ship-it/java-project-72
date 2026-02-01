.PHONY: build report run-dist test lint sonar image-build image-push
.DEFAULT_GOAL := build

build:
	make -C app build

build-daemonless:
	make -C app build-daemonless

report:
	make -C app report

run-dist:
	make -C app run-dist

run-dist-daemonless:
	make -C app run-dist-daemonless

test:
	make -C app test

lint:
	make -C app lint

sonar:
	make -C app sonar

image-build:
	make -C app image-build

image-push:
	make -C app image-push