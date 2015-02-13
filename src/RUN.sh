#!/bin/bash
if [ $# -eq 0 ]
	  then
		      echo "No arguments supplied"
	      else

			javac -cp .:lib/* *.java
			java -cp .:lib/* $1
	      fi
