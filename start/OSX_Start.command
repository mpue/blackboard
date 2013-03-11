#!/bin/bash
# if a user starts e.g. sh test/me/OSX_Start.command get the relative dir part "test/me"
RELATIVEDIR=`echo $0|sed s/OSX_Start.command//g`
if [ $RELATIVEDIR ];
then
  cd $RELATIVEDIR
fi

java -jar BlackBoard.jar
