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

These pathways ensure that client and server can communicate smoothly

#### Processing Input to Server
Since there are several cases of inputs that I need to take into account while 
developing the project, I decide to shape all input into the same format which
I call ```params``` that would be sent to the server. 

Before sending a message to server, I will use function ```trim()``` to make sure
that there are no leading spaces or ending spaces. I use ```Scanner``` library to
read the inputs from clients.

To process the input, I need to create a HashMap containing the method type as the key and 
the number of parameters that will be sent along with it as the value. 

First, I get the method name from the message string by iterating from the start to the first space. 
This will ensure that I will get the name of the message (e.g "get", "put", "bye", etc.). 

Second, in "get" or "put" message, the key will always follow the method name.
Therefore, in the case of "put" message, I will traverse from the end of the message string to the first space
to get the value of that message. After that, the middle part will be the key.

I will then return a list of params that will later on be used for server to respond 
appropriately to client.

#### Multithreading
Since TCP server works by creating a new connection socket upon connection acceptance,
I created a class called MultiThread in which for each client connected to a server, I assign an id to
the socket and add the socket to the current server. 

The client and the server will be communicating through a single port, in this case 5000. 

I first developed single thread application, and then refractored it by extending my server with Thread
class so that it can accept multiple sockets. The MultiThread class acts as a medium for clients to
send messages to server.

## Testing

Apart from testing invalid input to connect to a server, I will test most methods on localhost.

### Test invalid input
Invalid inputs will consist of wrong format IP address or 
Hostname is not found. 

- When I typed in "localhost", it connects successfully. 
- When I typed in "google.com", it returns ```Connection to "142.250.191.174" takes too long!``` with "142.250.191.174" being "google.com"'s IP address
- When I typed in nothing, making my hostname be "", the client returns ```No IP address or Hostname found!```
- When I typed in "1.2.1", the client returns ```Connection to "1.2.0.1" takes too long!```. It adds a 0 to the IP address to make it match ```IPv4_REGEX```
- When I typed in "local", the client returns ```IP address or Hostname "local" is not valid```, because "local"
cannot be found as a valid hostname and no IP address associated with it

After successful connection to localhost, I test some invalid commands:
- When I typed in nothing, making my message be "", the server returns ```No command found!```,
which is shown in the client
- When I typed in "test", the server returns ```Invalid command: "test"```,
which is shown in the client.

### Setting Up Key-Value Service (KVS)
I will first set up the KVS by adding some elements into it. An example of my adding would be: 
- ```put lam 5```
- ```put nguyen 6```
- ```put luke 7```
- ```put abc def 295```
All of those commands above are successful commands.

### Test get(key)

- When I typed in "get a", the server returns ```Cannot find key "a" in the data store```,
which is shown in the client.
- When I typed in "get lam", the server returns ```5```, which is shown in the client
- When I typed in "get", the server returns ```Invalid command: "get"``` 
because it is missing a key in the command.

### Test put(key, value)
As long as the value is an integer, the key can take the format of ```string + blank spaces + string```, 
with no leading spaces and no ending spaces.

- When I typed in "put a 30", the server responses ```Ok.```, which is shown in the client.
- When I typed in "put d ddd", the server responses ```Value "ddd" is not valid``` since "ddd"
is not an integer
- When I typed in "put d", the server responses ```Invalid command: "put d"``` 
because it is missing a value in the command.

### Test mappings()

- When I typed in "mappings", the server responses:

 ```["nguyen -> 6", "luke -> 7", "a -> 30", "lam -> 5", "abc def -> 295"]```
 
- When I typed in "mappings 30", the server still responses: 

```["nguyen -> 6", "luke -> 7", "a -> 30", "lam -> 5", "abc def -> 295"]```, because the 30 part
does not matter anymore and can be left out

### Test keyset()

- When I typed in "keyset", the server responses:

```[nguyen, luke, a, lam, abc def]```

- When I typed in "keyset 30", the server still responses: 

```[nguyen, luke, a, lam, abc def]```, because the 30 part
does not matter anymore and can be left out

### Test values()

- When I typed in "values", the server responses:

```[6, 7, 30, 5, 295]```

- When I typed in "values 30", the server still responses:

```[6, 7, 30, 5, 295]```, because the 30 part
does not matter anymore and can be left out

### Test multithreading
- When I opened a new Terminal and type in ```java Client.java```, a message appears in the multithreaded server
saying: ```Client [id] has connected to server!```
- I tested multithreading with put, get, mappings, keyset, and value switching back and forth between clients
- The problem in moving single-threaded application to multi-threaded application is how to unify database store amongst
clients. I solved this by adding a single database to every sockets. This way will ensure that the database
will be updated realtime and method calls will respond successfully.

### Test bye()
When I typed in "bye", the server responses ```See you later.```, which is shown in the client.
Now both the server and client closes at the same time.

## How To Run
You will first need to compile the code by running in Terminal the following script: ```javac KeyValueStore.java Client.java Server.java MultiThread.java```

To test on localhost, you will first have to start your MultiThread by running in Terminal: ```java MultiThread.java```
Then start the Client by running in Terminal: ```java Client.java```