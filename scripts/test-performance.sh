#!/bin/sh

# Date: 05.09.2013
# Author: Dariusz Stefanski
#
# It runs unit and _performance_ tests of Liner application.

MAX_LINE_NR="400000000"
FILE="file4x10^8.txt"

./createFile.sh $MAX_LINE_NR $FILE

# read properties
. ../system.properties

export MAVEN_OPTS="-Xms$ms -Xmx$mx"

export PerformanceTesting="true"
export TestFile=`pwd`/$FILE
export MaxLineNr=$MAX_LINE_NR

mvn test  -f ../pom.xml
