# Key-Value Service
## High-level Approach
### Overview
In this project, I will be developing client-server program 
that communicates with each other through TCP sockets.

For a client to connect to a server, it must first create a socket 
with specified hostname and port number. After that, the server
will create a new socket, and accept the client's socket if the 
connection is valid. 

### Approach
#### Key-Value Database
I first develop a key-value database with the help of HashMap for best time efficiency.
- ```get(key)```: HashMap's builtin function with time complexity being O(1)
- ```put(key, value)```: HashMap's builtin function with time complexity being O(1)
- ```mappings()```: Iterate through all elements of HashMap, and store the mappings into a list. 
              Time complexity: O(n)
- ```keyset()```: Iterate through all elements of HashMap, and store the keys into a list. 
            Time complexity: O(n)
- ```values()```: Iterate through all elements of HashMap, and store the values into a list. 
           Time complexity: O(n)
           
Overall, Key-Value Database resembles a lot like HashMap, so the choice HashMap in 
this situation I think would be optimal.    
       
#### IP/Hostname Validating
To check whether an IP or a Hostname is valid, I will use Regular Expression along with Pattern matching. 
However, before using any libraries that is provided beforehand, I need to process 
all hostname into the same format. 

In this case, I would change all hostname into "IP format", which means
that after being processed, all hostname will become IP address if it is 
not already one. 

```processHostname(hostname)```: If the hostname is "localhost", I will return 
the IP address of the localhost by using function ```Inet4Address.getLocalHost().getHostAddress()```.
Otherwise, if the hostname does not match the Pattern of Regex, I would try to get the
IP address of the hostname with the use of ```Inet4Address.getByName(hostname).getHostAddress()```. 
In the end, the hostname now has become an IP address. In this case, I will check one more time 
if the hostname matches the Regex Pattern and return ```null``` if not matched. 

#### TCP Sockets
For client to communicate with server and for server to send response back to client,
they need pathways. This is why I make the ```in``` and ```out``` for both client and server. 

- ```in```: a BufferedReader
- ```out```: a PrintWriter with ```autoFlush = true``` for auto message flushing after composed

For client-side:
- out: The streamline where the message will be sent from client to server
- in: The streamline where the response of the server will be received from the client 

For server-side:
- in: The streamline where the message will be read from the client 
- out: The streamline where the response will be sent back to the client


