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
     * Constructor for creating Client
     */
    public Client() {

    }

    /**
     * Process hostname and convert it into IP format
     * @param hostname the hostname to be process
     * @return the ip address associated with the hostname
     */
    private String processHostname(String hostname) {
        // get localhost ip address
        if (hostname.equals("localhost")) {
            try {
                hostname = Inet4Address.getLocalHost().getHostAddress();
            }
            catch (UnknownHostException e) {
                System.out.println("Cannot get the IP address of localhost");
            }
        }

        // check if ip is valid or not
        else if (!IPv4_PATTERN.matcher(hostname).matches()) {
            try {
                hostname = Inet4Address.getByName(hostname).getHostAddress();
            }
            catch (UnknownHostException e) {
                System.out.printf(invalidIPAndHostnameError, hostname);
                return null;
            }
        }

        // after converting the user's input to be ip format,
        // check if the final result is an ip or not
        if (!IPv4_PATTERN.matcher(hostname).matches()) {
            System.out.printf(invalidIPAndHostnameError, hostname);
            return null;
        }

        return hostname;
    }

    /**
     * Connect client to server
     * @param hostname the hostname that will be connected to
     * @param port the port that will be connected to
     * @return true if successful connection; otherwise, return false
     */
    public boolean connectToServer(String hostname, int port) {
        hostname = processHostname(hostname);

        if (hostname != null) {
            // create a new socket that connect the client to server
            // with in and out pathway for communication
            try {
                socket = new Socket(hostname, port);
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                System.out.println("Connection to \"" + hostname + "\" takes too long!");
                return false;
            }

            return true;
        }

        return false;
    }

    /**
     * Send message from client to server
     * @param msg the message that will be sent to server
     * @return the response from the server
     */
    public String sendMessageToServer(String msg) {
        try {
            out.println(msg);
            return in.readLine();
        }
        catch (IOException e) {
            System.out.println("Cannot send message to server!");
            return null;
        }
    }

    /**
     * Close the client socket & in and out pathways
     */
    public void closeClient() {
        try {
            in.close();
            out.close();
            socket.close();
        }
        catch (IOException e) {
            System.out.println("Cannot close client!");
        }
    }

    // Main method that will be used to execute Client side
    public static void main(String[] args) {
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
