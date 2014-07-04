"""

William Fiset
July, 3, 2014

This script is the client side of the application.
It is currently functional with the localhost IP but not an external IP

socket DOCS:
https://docs.python.org/2/howto/sockets.html#socket-howto
https://docs.python.org/2/library/socket.html

"""


from socket import *

client_socket = socket()

port = int(input("Enter Port Number: "))
serverIP = raw_input("Enter serverIP Address (or '?' for localhost IP): ").strip()

if serverIP == '?':
	serverIP = "127.0.0.1"

try:

	client_socket.connect( (serverIP, port) )

	# This the number of bytes you can receive from the server
	print client_socket.recv(1024)
	client_socket.close()

except:
	print "Could not connect to address: ", serverIP, ":", port, " \n"















