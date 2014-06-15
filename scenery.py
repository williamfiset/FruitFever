

"""
@Author William Fiset

This file changes the characters in a file depending on input.
ex:


	(Change everything to a '#' expect '-'')
	NOTE: by Default the python script performs this operation

	python charChange.py
		or
	python charChange.py '!' '#' '-'


	--aa--     --##--
	-abc12     -###??
	abbcc- --> #####-
	---FF|     ---##?

	Special characters such as flags/fruitrings/fruits are turned to '?'s
	which are ignored by the scenery loader

	--*--|     --?--?
	----cc     ----## 
	01---- --> ??----
	ABC---     ###---


	python charChange.py ! W EST

	This will change every character to a W expect E, S and T

    ####      WWWW
	PPPP      WWWW
	WWWW      WWWW
	EEEE  --> EEEE
	SSSS      SSSS
	TTTT      TTTT
	UUUU      WWWW
	$$$$      WWWW


	python charChange.py W EST

	This will replace every instance of the characters E, S and T with W

	####      ####
	PPPP      PPPP
	WWWW      WWWW
	EEEE  --> WWWW
	SSSS      WWWW
	TTTT      WWWW
	UUUU      UUUU
	$$$$      $$$$

"""

import sys


def changeCharacters(hasExclamation, letter, characters):
	

	myFile = None
	fileName = "scenery.txt"
	characters = list(characters)
	lines = []

	try:
		
		myFile = open(fileName, 'r+')

		currentLine = ""
		lengthOfFirstLine = 0
		lineNumber = 0

		for line in myFile.readlines():
			
			# These Crops all lines to be the same length
			if lineNumber == 0:	lengthOfFirstLine = len(line)
			if len(line) == lengthOfFirstLine:	line = line[:-1]

			for char in line:
				

				if hasExclamation:

					if char not in characters:
						currentLine += letter
					else:
						currentLine += char

				else:
					
					# Works
					if char in characters:
						currentLine += letter
					else:
						currentLine += char

			lines.append(currentLine)
			currentLine = ""
			lineNumber+=1

		myFile.close()

		# Erases the contents of file
		open(fileName, 'w').close()

				# Reopens the blank file
		myFile = open(fileName, 'w')

		for line in lines:
			myFile.write(line + "\n")


	except Exception as e:
		print("\n Could not find characterChangeFile.txt \n")
		raise e

	finally:
		myFile.close()


# Starts program
if __name__ == '__main__':
	
	commandLineArguments = sys.argv
	commandLineArguments.pop(0)

	args = []
	args = commandLineArguments

	if len(args) == 0:
		
		# changes all the characters @012~&| to dashes
		# This needs to be updated all the time as we add more and more symbols
		changeCharacters(False, "?", "@0123456789&%|*^:?")

		#
		changeCharacters(True, "#", '-?')

	elif len(args) == 1:
		print("Please provide at least one more argument")		

	else:

		hasExclamation = args[0] == "!"

		if hasExclamation:
			changeCharacters(True, args[1], args[2])
		else:
			changeCharacters(False, args[0], args[1])













