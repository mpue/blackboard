#!/bin/bash
# if a user starts e.g. sh test/me/Linux_Start.sh get the relative dir part "test/me"
RELATIVEDIR=`echo $0|sed s/Linux_Start.sh//g`
if [ $RELATIVEDIR ];
then
  cd $RELATIVEDIR
fi

java -Dsun.java2d.opengl=true -jar BlackBoard.jar
