import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private final int PORT = 5000;
    private ServerSocket serverSocket;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private KeyValueStore kvs;

    private final HashMap<String, Integer> messageTypes;

    public Server() {
        this.kvs = new KeyValueStore();
        this.messageTypes = new HashMap<>();
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
        String message = sb.toString();
        params.add(message);

        // for specific methods, extract different number of elements
        // we now have start, we now traverse from end to get the number
        // if the message is put
        if (messageTypes.get(message) > 0) {
            int j = msg.length() - 1;
            sb = new StringBuilder();

            while (j >= 0 && msg.charAt(j) != ' ') {
                sb.append(msg.charAt(j));
                j--;
            }
            String value = sb.toString();
            params.add(value);

            sb = new StringBuilder();
            i++;
            j--;
            while (i <= j) {
                sb.append(msg.charAt(i));
                i++;
            }
            if (sb.length() > 0) params.add(sb.toString());
        }

        return params;
    }

    public boolean sendResponseToClient(String msg) {
        // for the simple input, that would be okay, but in general this will not work
        List<String> params = processMessage(msg);
        if (!(params.size() == messageTypes.get(params.get(0)))) return false;

        switch (params.get(0)) {
            case "help" -> {
                out.println("Call help");
            }
            case "get" -> {
                int value = kvs.get(params.get(1));
                if (value == -1) out.println("Cannot find key \"" + params.get(1) + "\" in the data store");
            }
            case "put" -> {
                String key = params.get(2);
                int value = Integer.parseInt(params.get(1));
                kvs.put(key, value);
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
//        KeyValueStore kvs = new KeyValueStore();
//        kvs.put("OK", 1);
//        kvs.put("OKOK", 2);

        Server server = new Server();

        String testMessage = "put abcd";
        System.out.println(server.processMessage(testMessage).toString());

        server.initServer(server.PORT);

//        while (true) {
//            String msg = server.in.readLine();
//            boolean sentToClient = server.sendResponseToClient(msg);
//            if (!sentToClient) System.out.println("Invalid message!");
//            if (msg.equals("bye")) break;
//        }

        server.closeServer();
    }
}
