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
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) sb.append(line);
        return sb.toString();
    }

    public void closeClient() throws IOException {
        in.close();
        out.close();
        socket.close();
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Welcome to the KeyValue Service Client");
        System.out.print("Enter the IP address or Hostname of the server: ");

        Scanner scanner = new Scanner(System.in);
        String hostname = scanner.nextLine();

//        System.out.println(hostname);

        System.out.println("Please wait while I connect you...");
        Client client = new Client();
        client.connectToServer(hostname, client.PORT);
        System.out.println("Welcome to the KeyValue Service");

        boolean open = true;

        while (open) {
            System.out.print("KeyValue Service> ");
            String msg = scanner.nextLine();

            String res = client.sendMessageToServer(msg);
            if (res.equals("bye")) open = false;
            System.out.println(res);
        }

        client.closeClient();
        System.out.println("See you later.");
    }
}
