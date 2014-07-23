#!/usr/bin/env python

"""
@author William Fiset

All this file does is it deletes all the .class files in the cwd
"""

import os
import sys

# Deletes all the files that end in the extension specified within a given a directory 
def deleteFiles(directory, extension):

	for file_ in os.listdir(directory):
		if file_.endswith(extension):
			os.remove(directory + file_)

if __name__ == '__main__':
	deleteFiles(sys.argv[1] , sys.argv[2])


