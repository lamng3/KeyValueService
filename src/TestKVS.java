import org.junit.*;
import static org.junit.Assert.*;

public class TestKVS {
    private final String hostname = "localhost";
    private final int PORT = 5000;

    @Test
    public void connectToLocalHost() {
        Client client = new Client();
        boolean connected = client.connectToServer(hostname, PORT);
        assertTrue(connected);
    }

    @Test
    public void validateIPAddress() {
//        Server server = new Server();
//        server.initServer(PORT);
    }
}

