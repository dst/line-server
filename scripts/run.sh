#!/bin/sh

# Date: 31.08.2013
# Author: Dariusz Stefanski
#
# It starts Liner server.
# Argument: name of the file to serve

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

# read properties
. ../system.properties

java -Xms$ms -Xmx$mx  -jar ../target/*.jar $file
