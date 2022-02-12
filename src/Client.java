import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;

public class Client {
    private final int PORT = 5000;
    private final String LOCALHOST_IP = "172.19.95.248";
    private final String IPv4_REGEX =
                    "^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                    "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                    "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                    "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
    private final Pattern IPv4_PATTERN = Pattern.compile(IPv4_REGEX);
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private String invalidIPAndHostnameError = "IP address or Hostname \"%s\" is not valid ...";

    public boolean connectToServer(String hostname, int port) throws IOException {
        if (hostname.equals("localhost")) hostname = Inet4Address.getLocalHost().getHostAddress();
        else if (!IPv4_PATTERN.matcher(hostname).matches()) {
            try {
                hostname = Inet4Address.getByName(hostname).getHostAddress();
            }
            catch (UnknownHostException e) {
//                System.out.println("Cannot find ip of \"" + hostname + "\"");
                System.out.printf(invalidIPAndHostnameError, hostname);
                return false;
            }
        }

        System.out.println("ip: " + hostname);

        if (!IPv4_PATTERN.matcher(hostname).matches()) {
            System.out.printf(invalidIPAndHostnameError, hostname);
            return false;
        }

        try {
            socket = new Socket(hostname, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        catch (ConnectException e) {
//            System.out.println("Cannot connect to \"" + hostname + "\"");
            System.out.printf(invalidIPAndHostnameError, hostname);
            return false;
        }

        return true;
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

        System.out.print("Enter the IP address or Hostname of the server: ");
        Scanner scanner = new Scanner(System.in);
        String hostname = scanner.nextLine();

        System.out.println("Please wait while I connect you...");
        Client client = new Client();
        boolean connected = client.connectToServer(hostname, client.PORT);

        if (connected) {
            System.out.println("Welcome to the KeyValue Service");

            while (true) {
                System.out.print("KeyValue Service> ");
                String msg = scanner.nextLine().trim();

                String res = client.sendMessageToServer(msg);
                if (res.equals("bye")) break;
                System.out.println(res);
            }

            client.closeClient();
            System.out.println("See you later.");
        }
//        else {
//            System.out.println("IP address or Hostname \"" + hostname + "\" is not valid ...");
//        }
    }
}
