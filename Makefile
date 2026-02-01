build:
	make -C app build

report:
	make -C app report

run-dist:
	make -C app run-dist

test:
	make -C app test

lint:
	make -C app checkstyleMain

sonar:
	make -C app sonar

image-build:
	make -C app image-build

image-push:
	make -C app image-push
