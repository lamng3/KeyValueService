import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    private final int PORT = 8080;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public void connectToServer(String hostname, int port) throws IOException {
        if (hostname.equals("localhost")) hostname = InetAddress.getLocalHost().getHostAddress();
        socket = new Socket(hostname, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public String sendMessageToServer(String msg) throws IOException {
        out.println(msg);
        return in.readLine();
    }

    public void closeClient() throws IOException {
        in.close();
        out.close();
        socket.close();
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Welcome to the KeyValue Service Client");
        System.out.println("Enter the IP address or Hostname of the server: ");

        Scanner scanner = new Scanner(System.in);
        String hostname = scanner.next();

        System.out.println("Please wait while I connect you...");
        Client client = new Client();
        client.connectToServer(hostname, client.PORT);
        System.out.println("Welcome to the KeyValue Service");

        while (true) {
            System.out.print("KeyValue Service> ");
            String msg = scanner.next();

            if (msg.equals("bye")) break;

            String res = client.sendMessageToServer(msg);
            System.out.println(res);
        }
    }
}
