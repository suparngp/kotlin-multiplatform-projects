#!/bin/bash
./gradlew clean
rm -rf ~/.m2/repository

projects=( utilities threading fs cache )

for i in "${projects[@]}"
do
    echo "-----------$i-------------"
    rm -rf ~/.m2/repository/com/suparnatural
	./gradlew :$i:clean
	./gradlew :$i:build -x test
	./gradlew :$i:connectedAndroidTest
	./gradlew :$i:iosTest
	./gradlew :$i:podspec
	./gradlew :$i:publishToMavenLocal
	./gradlew :$i:bintrayUpload
	echo "--------------------------"
done