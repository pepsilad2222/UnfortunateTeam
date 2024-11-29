#!/bin/bash

cd "$(dirname "$0")/../../../" 
export MAVEN_OPTS="--add-opens java.base/java.lang=ALL-UNNAMED"

echo "Starting test with seed 42..."
mvn compile exec:java \
    -Dexec.mainClass="ShadowLabyrinth" \
    -Dexec.args="42" \
    < <(printf "50\nLEFT\nLEFT\nLEFT\n") \
    | tee /dev/tty \
    | grep -E --color=auto "attack|dodge|hit|miss|RIGHT|LEFT"