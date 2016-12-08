#!/bin/sh
java -Xmx2048m -Xms256m -cp latlab.jar:colt.jar:commons-cli-1.2.jar Classify $*
