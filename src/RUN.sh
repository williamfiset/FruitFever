#!/bin/sh
javac *.java
java -cp .:lib/* $1
