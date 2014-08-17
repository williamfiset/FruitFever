#!/usr/local/bin/python3.4

"""

This script pulls all your git branches and makes them up to date with 
the git repository. It also ensures that you are on the branch on which you first started.

"""

import subprocess
import sys

__author__ = 'William Fiset'

# If application is being run as an executable 
if __name__ == '__main__':
	
	current_branch = ''

	# Grab the standard output of calling 'git branch'
	output = subprocess.Popen(['git', 'branch'], stdout = subprocess.PIPE)

	# Read the output into a list
	branches = output.communicate()[0]
	branches = branches.splitlines() # not sure if it's \n or \r or even \n\r on windows

	pull_branches = []

	# Loop through the branches and find the current branch
	for branch in branches:
		if branch is not '':
			branch = branch.decode()
			# * indicates the current checked-out branch
			if '*' in branch:

				branch = branch.split()
				branch.remove('*')
				branch = branch[0]
				
				current_branch = branch

			branch = branch.strip()	
			pull_branches.append(branch)


	git_status_output = subprocess.Popen(['git', 'status'], stdout = subprocess.PIPE).communicate()[0]

	# User did not commit the changes in their current branch, either they stash or commit first
	if b'nothing to commit, working directory clean' not in git_status_output:
		print ("\nPlease commit or stash your changes before launching this script\n")
		sys.exit()

	# Loop through the branches and pull em!
	for branch in pull_branches:

		print()
		subprocess.call(['git', 'checkout', branch])
		subprocess.call(['git', 'pull'])
	
	# Return to original branch
	subprocess.call(['git', 'checkout', current_branch])




