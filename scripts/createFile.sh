#!/bin/sh

# Date: 03.09.2013
# Author: Dariusz Stefanski
#
# It creates a file with specified count of lines (n) in current directory.
# The file looks in the following way:
# 1
# 2
# ...
# n

if [ $# -ne 2 ]
then
  echo "Usage: $0 lineCount file"
  exit
fi

lineCount=$1
file=$2

if [ -f $file ]
then
  echo "File $file exists. Aborting."
  exit 1
fi

echo "Creating file..."
time seq $lineCount | dd of=$file bs=1024 count=$lineCount

