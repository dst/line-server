#!/bin/sh

# Date: 31.08.2013
# Author: Dariusz Stefanski
#
# It builds LineServer application.

setup-ubuntu.sh
mvn compile  -f line-server/pom.xml
