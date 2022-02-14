/**
 * @author: Lam Nguyen
 * @id: ltn18
 * @course: Computer Networks
 */

import java.io.*;
import java.net.*;
import java.util.*;

// server for key-value service
public class Server {

    // port that server and client will talk through
    private final int PORT = 5000;

    // socket that will be created in the server
    private ServerSocket serverSocket;

    // the socket that will then be accepted by the server socket
    private Socket socket;

    // pathway for server to send message to client
    private PrintWriter out;

    // pathway for server to read message from client
    private BufferedReader in;

    // key-value store database
    private final KeyValueStore kvs;

    // hash map that stores all types of messages with the number of parameters included along with it
    private final HashMap<String, Integer> messageTypes;

    /**
     * Constructor that creates the server
     */
    public Server() {
        this.kvs = new KeyValueStore();

        // create the hash map and fill in all data
        this.messageTypes = new HashMap<>();
        messageTypes.put("help", 0);
        messageTypes.put("get", 1);
        messageTypes.put("put", 2);
        messageTypes.put("mappings", 0);
        messageTypes.put("keyset", 0);
        messageTypes.put("values", 0);
        messageTypes.put("bye", 0);
    }

    /**
     * Process message received from client to appropriate format
     * @param msg message sent from client
     * @return the list containing all params that will be used later by the server
     */
    private List<String> processMessage(String msg) {
        // params that will be returned
        List<String> params = new ArrayList<>();

        // extract the message type
        // eg. "get", "help", ...
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < msg.length() && msg.charAt(i) != ' ') {
            sb.append(msg.charAt(i));
            i++;
        }
        String message = sb.toString().trim();
        params.add(message);

        // handle the case when the extracted message is not of valid type
        if (!messageTypes.containsKey(message)) return params;

        // handle the case of "get" and "put"
        // when there are other params to consider
        if (messageTypes.get(message) > 0) {

            // initialize j to be the end of the message string
            int j = msg.length() - 1;

            // find the value by iterating from the end of the message
            if (message.equals("put")) {
                sb = new StringBuilder();
                while (j >= 0 && msg.charAt(j) != ' ') {
                    sb.append(msg.charAt(j));
                    j--;
                }
                String value = sb.reverse().toString().trim();
                params.add(value);
                j--;
            }

            // get the key of the message
            sb = new StringBuilder();
            i++;
            while (i <= j) {
                sb.append(msg.charAt(i));
                i++;
            }
            if (sb.length() > 0) params.add(sb.toString().trim());
        }

        return params;
    }


    /**
     * Initialize the server
     * @param port the port that will be used to communicate with client
     */
    public void initServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            socket = serverSocket.accept();
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        catch (IOException e) {
            System.out.println("Cannot start server!");
        }
    }

    /**
     * Send response back to the client
     * @param msg message received from the client
     * @return true if message is sent successfully; otherwise, return false;
     */
    public boolean sendResponseToClient(String msg) {
        // handle the case when user types in nothing and hits enter
        if (msg.equals("")) {
            out.println("No command found!");
            return false;
        }

        // the params extracted from the message
        List<String> params = processMessage(msg);

        // print out all the params
        System.out.println(Arrays.toString(params.toArray()));

        // check if the message is of valid type
        if (!messageTypes.containsKey(params.get(0))) {
            out.println("Invalid command: \"" + msg + "\"");
            return false;
        }

        // check if there are sufficient params passed into the server
        if (!(params.size()-1 == messageTypes.get(params.get(0)))) {
            out.println("Invalid command: \"" + msg + "\"");
            return false;
        }

        // check all the cases of the message type
        switch (params.get(0)) {
            case "help" -> {
                // message that will be responded back to client when "help" is received from client
                String callHelp = "\"help\"    \"get [key]\"    \"put [key] [value]\"    \"mappings\"    \"keyset\"    \"values\"    \"bye\"";
                out.println(callHelp);
            }
            case "get" -> {
                // the value that retrieved from the KVS database
                int value = kvs.get(params.get(1));

                // handle case if value not found
                if (value == -1) out.println("Cannot find key \"" + params.get(1) + "\" in the data store");
                else out.println(value);
            }
            case "put" -> {
                String key = params.get(2);
                try {
                    // convert the value to integer
                    int value = Integer.parseInt(params.get(1));
                    kvs.put(key, value);
                    out.println("Ok.");
                }
                catch (NumberFormatException e) {
                    out.println("Value \"" + params.get(1) + "\" is not valid");
                    return false;
                }
            }
            case "mappings" -> {
                // KVS list of mappings
                List<String> mps = kvs.mappings();
                out.println(Arrays.toString(mps.toArray()));
            }
            case "keyset" -> {
                // KVS list of all values
                List<String> keys = kvs.keyset();
                out.println(Arrays.toString(keys.toArray()));
            }
            case "values" -> {
                // KVS list of all keys
                List<Integer> vals = kvs.values();
                out.println(Arrays.toString(vals.toArray()));
            }
            case "bye" -> {
                out.println("bye");
            }
            default -> {
                out.println("Invalid command: \"" + msg + "\"");
            }
        }

        // flush out all messages
        out.flush();
        return true;
    }

    /**
     * Close the server socket & in and out pathways
     */
    public void closeServer() {
        try {
            in.close();
            out.close();
            serverSocket.close();
            socket.close();
        }
        catch (IOException e) {
            System.out.println("Cannot close the server!");
        }
    }

    // Main method that will be used to execute Server side
    public static void main(String[] args) throws IOException {
        // initialize the server
        Server server = new Server();
        server.initServer(server.PORT);

        while (true) {

            // message read from the client
            String msg = server.in.readLine();

            // sending response to client
            boolean sentToClient = server.sendResponseToClient(msg);

            // handle case if response cannot be sent to client
            if (!sentToClient) System.out.println("Invalid message to server!");

            // terminate loop when the message is "bye"
            if (msg.equals("bye")) break;
        }

        server.closeServer();
    }
}
