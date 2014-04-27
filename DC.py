
"""
@author William Fiset

All this file does is it deletes all the .class files in a folder

"""

import os

def deleteFiles(directory, extension):

	if extension == "java": return

	for _file in os.listdir(directory):
		if str(_file)[-len(extension):] == extension:
			os.remove(directory + _file)

deleteFiles( os.getcwd() + "/src/", "class" )








