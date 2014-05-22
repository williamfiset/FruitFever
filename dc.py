#!/usr/bin/env python

"""
@author William Fiset

All this file does is it deletes all the .class files in a folder when
GameStarter is not running. This program is meant to run as a Daemon 
throughout the time you work.

"""

import os
import time
import sys
import signal
import re
import subprocess

# returns true or false on whether a subprocess is running
def is_running(process):

	subprocesses = subprocess.Popen(["ps", "axw"],stdout=subprocess.PIPE)
	for executingApplication in subprocesses.stdout:

		if re.search(process, executingApplication):

			return True

	return False

# Deletes all the files that end in the extension specified within a given a directory 
def deleteFiles(directory, extension):

	# Ensures that we do not delete our own Java files by accident 
	if extension == "java": return

	for _file in os.listdir(directory):
		if str(_file)[-len(extension):] == extension:
			os.remove(directory + _file)

def runDaemon():

    while True:

    	# Sleeps for one second
        time.sleep(1)
        
        if not is_running("GameStarter"):
        	deleteFiles( os.getcwd() + "/src/" , "class" )

if __name__ == "__main__":
    runDaemon()



