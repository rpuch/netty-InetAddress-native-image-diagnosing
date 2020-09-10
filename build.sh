#!/bin/sh

red=`tput setaf 1`
reset=`tput sgr0`

mvn clean package && ./collect.sh && ./build-native.sh || echo "${red}BUILD FAILED${reset}"
