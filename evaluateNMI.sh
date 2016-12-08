#!/bin/sh
java -Xmx2048m -Xms1024m -cp latlab.jar:colt.jar:commons-cli-1.2.jar EvaluateNMI $*
