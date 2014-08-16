#!/bin/sh
javac -cp .:lib/* *.java
java -cp .:lib/* $1
