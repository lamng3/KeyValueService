import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private final int PORT = 5000;
    private ServerSocket serverSocket;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private final KeyValueStore kvs;

    private final HashMap<String, Integer> messageTypes;

    public Server() {
        this.kvs = new KeyValueStore();
        this.messageTypes = new HashMap<>();
        messageTypes.put("help", 0);
        messageTypes.put("get", 1);
        messageTypes.put("put", 2);
        messageTypes.put("mappings", 0);
        messageTypes.put("keyset", 0);
        messageTypes.put("values", 0);
        messageTypes.put("bye", 0);
    }

    public void initServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        socket = serverSocket.accept();
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    // implement this message processing
    private List<String> processMessage(String msg) {
        List<String> params = new ArrayList<>();

        // extract message type
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < msg.length() && msg.charAt(i) != ' ') {
            sb.append(msg.charAt(i));
            i++;
        }
        String message = sb.toString().trim();
        params.add(message);

        // for specific methods, extract different number of elements
        // we now have start, we now traverse from end to get the number
        // if the message is put
        if (messageTypes.get(message) > 0) {
            int j = msg.length() - 1;

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

    public boolean sendResponseToClient(String msg) {
        // for the simple input, that would be okay, but in general this will not work
        if (msg.equals("")) {
            out.println("No command found!");
            return false;
        }
        List<String> params = processMessage(msg);
        System.out.println(Arrays.toString(params.toArray()));
        if (!messageTypes.containsKey(params.get(0))) {
            out.println("Invalid command: \"" + msg + "\"");
            return false;
        }
        if (!(params.size()-1 == messageTypes.get(params.get(0)))) {
            out.println("Invalid command: \"" + msg + "\"");
            return false;
        }

        switch (params.get(0)) {
            case "help" -> {
                out.println("help");
                out.println("another line");
            }
            case "get" -> {
                int value = kvs.get(params.get(1));
                if (value == -1) out.println("Cannot find key \"" + params.get(1) + "\" in the data store");
                else out.println(value);
            }
            case "put" -> {
                String key = params.get(2);
                try {
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

            }
            case "keyset" -> {
                List<String> keys = kvs.keyset();
                out.println(Arrays.toString(keys.toArray()));
            }
            case "values" -> {
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

        out.flush();
        return true;
    }

    public void closeServer() throws IOException {
        in.close();
        out.close();
        serverSocket.close();
        socket.close();
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();

//        String testMessage = "put abcd";
//        System.out.println(server.processMessage(testMessage).toString());
//        System.out.println(Integer.parseInt("a"));

        server.initServer(server.PORT);

        while (true) {
            String msg = server.in.readLine();
            boolean sentToClient = server.sendResponseToClient(msg);
            if (!sentToClient) System.out.println("Invalid message to server!");
            if (msg.equals("bye")) break;
        }

        server.closeServer();
    }
}
