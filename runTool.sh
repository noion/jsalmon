#!/bin/zsh

./gradlew clean jar &&
cd build/classes/java/main/ &&
java ru/noion/tool/GenerateAst ../../../../src/main/java/ru/noion/jsalmon &&
git add ../../../../src/main/java/ru/noion/jsalmon