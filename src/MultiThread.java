import java.io.*;
import java.net.*;

public class MultiThread {
    // key-value store database
    private static KeyValueStore kvs;

    // port that server and client will talk through
    private final static int PORT = 5000;

    // Main method that will be used to execute Server side
    public static void main(String[] args) {
        try {
            // single key value store
            kvs = new KeyValueStore();

            // initialize the server
            ServerSocket serverSocket = new ServerSocket(PORT);
            int id = 0;

            while (true) {
                id++;
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client " + id + " has connected to server!");
                Server server = new Server(clientSocket, kvs, id);
                server.start();
            }
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
