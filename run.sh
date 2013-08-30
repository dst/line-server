#!/bin/sh

# Date: 31.08.2013
# Author: Dariusz Stefanski
#
# It starts LineServer application.
# Argument: name of the file to serve

SERVER_CLASS="com.stefanski.lineserver.App"
SERVER_CLASSPATH="line-server/target/classes/"

if [ $# -ne 1 ]
then
  echo "Usage: $0 fileName"
  exit
fi
file=$1

if [ ! -f $file ]
then
  echo "File $file doesn't exist"
  exit 1
fi

# TODO(dst): pass argument
java -classpath $SERVER_CLASSPATH $SERVER_CLASS 
