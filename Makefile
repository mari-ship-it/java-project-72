build:
	make -C app build

report:
	make -C app report

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
