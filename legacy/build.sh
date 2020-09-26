#!/bin/bash
./gradlew clean

projects=(utilities concurrency fs cache rx rx-runtime-reaktive graphql)

for i in "${projects[@]}"; do
  echo "-----------$i-begin------------"
  rm -rf ~/.m2/repository/suparnatural-kotlin-multiplatform
  ./gradlew :$i:release
  echo "-----------$i-done-------------"
done
