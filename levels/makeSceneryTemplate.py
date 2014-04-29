
"""
@Author William Fiset

This file speeds up the process of making scenery by replacing all the 
characters expect the '-' to hashTags. This makes the outline of the blocks
easy to see and easier to place plants and fruits on.

NOTE: HashTags are read in by the Data file as empty sprites (Added by Will).

"""

def changeAllCharacters(fileName, letter, *exclusions):
	
	_file = None
	lines = []
	
	try:

		_file = open(fileName, 'r+')

		# Stores lines in an array
		currentLine = ""

		# Loops through all the lines in the file
		for line in _file.readlines():

			# Assumes that the last line always has a \n
			line = line[:-1]
			
			# Replaces a Character with 'letter' variable if it is not 
			# one of the exclusions
			for char in line:
				if char not in exclusions: currentLine += letter
				else: currentLine += char

			lines.append(currentLine)
			currentLine = ""

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


changeAllCharacters("sceneryTemplateFile.txt", '#', '-')








