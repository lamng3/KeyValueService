/**
 * @author: Lam Nguyen
 * @id: ltn18
 * @course: Computer Networks
 */

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;

// client for key-value service
public class Client {

    // port that server and client will talk through
    private final int PORT = 5000;

    // regex to validate ip address
    private final String IPv4_REGEX =
                    "^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                    "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                    "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                    "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";

    // pattern to match user's ip input
    private final Pattern IPv4_PATTERN = Pattern.compile(IPv4_REGEX);

    // socket created to for client to communicate with server
    private Socket socket;

    // pathway for client to send message to server
    private PrintWriter out;

    // pathway for client to read message from server
    private BufferedReader in;

    // string format for invalid ip/hostname
    private final String invalidIPAndHostnameError = "IP address or Hostname \"%s\" is not valid";

    /**
     * Constructor that creates a client
     */
    public Client() {

    }

    /**
     * Connect client to server
     * @param hostname the hostname that will be connected to
     * @param port the port that will be connected to
     * @return true if successful connection; otherwise, return false
     * @throws IOException handle exceptions upon invalid ip/hostname or connection takes too long
     */
    public boolean connectToServer(String hostname, int port) throws IOException {
        // get localhost ip address
        if (hostname.equals("localhost")) hostname = Inet4Address.getLocalHost().getHostAddress();
        // check if ip is valid or not
        else if (!IPv4_PATTERN.matcher(hostname).matches()) {
            try {
                hostname = Inet4Address.getByName(hostname).getHostAddress();
            }
            catch (UnknownHostException e) {
                System.out.printf(invalidIPAndHostnameError, hostname);
                return false;
            }
        }

        // after converting the user's input to be ip format,
        // check if the final result is an ip or not
        if (!IPv4_PATTERN.matcher(hostname).matches()) {
            System.out.printf(invalidIPAndHostnameError, hostname);
            return false;
        }

        // create a new socket that connect the client to server
        // with in and out pathway for communication
        try {
            socket = new Socket(hostname, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        catch (ConnectException e) {
            System.out.println("Connection to \"" + hostname + "\" takes too long!");
            return false;
        }

        return true;
    }

    /**
     * Send message from client to server
     * @param msg the message that will be sent to server
     * @return the response from the server
     * @throws IOException handle exceptions of the in and out pathways
     */
    public String sendMessageToServer(String msg) throws IOException {
        out.println(msg);
        return in.readLine();
    }

    /**
     * Close the client socket & in and out pathways
     * @throws IOException handle exceptions of in, out, and socket
     */
    public void closeClient() throws IOException {
        in.close();
        out.close();
        socket.close();
    }

    // Main method that will be used to execute Client side
    public static void main(String[] args) throws IOException {
        System.out.println("Welcome to the KeyValue Service Client");
        System.out.print("Enter the IP address or Hostname of the server: ");

        // read user's input
        Scanner scanner = new Scanner(System.in);
        String hostname = scanner.nextLine();

        System.out.println("Please wait while I connect you...");

        // connect to server
        Client client = new Client();
        boolean connected = client.connectToServer(hostname, client.PORT);

        // successful connection case
        if (connected) {
            System.out.println("Welcome to the KeyValue Service Client");

            while (true) {
                System.out.print("KeyValue Service> ");

                // get message from the command typed by user
                String msg = scanner.nextLine().trim();

                // retrieve the response from the server
                String res = client.sendMessageToServer(msg);

                // terminate loop when the message is "bye"
                if (res.equals("bye")) break;

                // print out the response of the server
                System.out.println(res);
            }

            client.closeClient();
            System.out.println("See you later.");
        }
    }
}
