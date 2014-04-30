
"""
@Author William Fiset

This file speeds up the process of making scenery by replacing all the 
characters expect the '-' to hashTags. This makes the outline of the blocks
easy to see and easier to place plants and fruits on.

NOTE: HashTags are read in by the Data file as empty sprites (Added by Will).

"""

import sys

def changeAllCharacters(fileName, letter, exclusions):
	
	_file = None
	lines = []

	try:

		_file = open(fileName, 'r+')

		# Stores lines in an array
		currentLine = ""

		lineNumber = 0
		totalLines = len(_file.readlines())

		# Loops through all the lines in the file
		for line in _file.readlines():

			if lineNumber == totalLines:
				print "lastLine"
				line += " "

			# Assumes that the last line always has a \n
			line = line[:-1]
			
			# Replaces a Character with 'letter' variable if it is not 
			# one of the exclusions
			for char in line:
				
				if exclusions[0] == '!':
				
					if char == letter:
						currentLine += letter
					else:
						currentLine += char

				else:

					if char not in exclusions:
						currentLine += letter
					else:
						currentLine += char


			lines.append(currentLine)
			currentLine = ""

			lineNumber += 1

		_file.close()

		# Erases the contents of file
		open(fileName, 'w').close()

		# Reopens the blank file
		_file = open(fileName, 'w')

		for line in lines:
			_file.write(line + "\n")


	except Exception, e:
		raise e
		print "\nProblem opening {0}.txt\n".format(fileName)

	finally:
		_file.close()


if __name__ == '__main__':

	commandLineArgs = sys.argv
	args = []


	for argument in commandLineArgs:
		if len(argument) == 1:
			args.append(argument)


	if len(commandLineArgs) == 0:
		changeAllCharacters("tempFile.txt", '#', '-')		
	elif len(commandLineArgs) == 1:
		print "Please provide an extra argument"
	else:
		changeAllCharacters("tempFile.txt", args[0], args[1:])








