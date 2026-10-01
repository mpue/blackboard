#!/bin/bash

cd "$(dirname "$0")"

exec java --enable-native-access=ALL-UNNAMED -jar BlackBoard.jar "$@"
