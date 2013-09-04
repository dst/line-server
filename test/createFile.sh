#!/bin/sh

# Date: 03.09.2013
# Author: Dariusz Stefanski
#
# It creates file.txt with specified count of lines in current directory.

FILE_NAME="file.txt"

if [ $# -ne 1 ]
then
  echo "Usage: $0 lineCount"
  exit
fi
lineCount=$1

rm $FILE_NAME
time seq $lineCount | dd of=file.txt bs=1024 count=$lineCount

