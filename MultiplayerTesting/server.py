"""

William Fiset
July, 3, 2014

This script is the server side of the application.

socket DOCS:
https://docs.python.org/2/howto/sockets.html#socket-howto
https://docs.python.org/2/library/socket.html

"""

import socket, sys

server_socket = socket.socket()

 # Perhaps this needs to change if we're connecting to other servers but our own

#           Williams-iMac.local 
host_name = 'localhost'
port = 5001  

portAvailable = False

# Find a free port to use. Port range must be between 0-65535
while not portAvailable:

	if port < 0 or port > 65535:
		print "No Ports available or port out of range"
		sys.exit(0)

	try:
		server_socket.bind ( ( host_name, port) ) 
		server_socket.listen(1) # maximum of one connection
		portAvailable = True
	except:
		port += 1

		
print "\nServer is connected to port: ", port, "\n" 

# returns 10.255.250.180 as my IP rather than 207.115.103.90
# print socket.gethostbyname(socket.gethostname()) 

while True:

	print "Server trying to connect to client..."
	
	client, address = server_socket.accept()
	print "Server just connected to: " , address

	client.send("Hello World From Server\n")
	client.close()




