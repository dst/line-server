#!/bin/sh

# Date: 31.08.2013
# Author: Dariusz Stefanski
#
# It builds Liner application.

mvn clean compile assembly:single -f ../pom.xml
