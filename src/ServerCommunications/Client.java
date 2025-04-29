package ServerCommunications;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


/**
 * Client that will communicate
 * with the Server allowing
 * Communication with the DB.
 *
 * @author Adolfo
 */
public class Client {
  private static final String SERVER_IP = "localhost";
  private static final int PORT = 4446;


  private static Socket socket;
  private static PrintWriter out;
  private static BufferedReader in;

  /**
   * Create connection with DB
   */
  static {
    try {
      socket = new Socket(SERVER_IP, PORT);
      out = new PrintWriter(socket.getOutputStream(), true);
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    } catch (IOException e) {
      throw new ExceptionInInitializerError("Failed to connect to DB server: " + e.getMessage());
    }
  }

  /**
   * Send command to DB
   * @param command Cmd to be sent
   * @return Response from Server
   * @throws IOException
   */
  public static String sendCommand(String command) throws IOException {
    out.println(command);
    StringBuilder response = new StringBuilder();
    String line;
    while ((line = in.readLine()) != null) {
      if (line.isEmpty()) break;
      response.append(line).append("\n");
    }

    if (response.indexOf("ERROR:") == 0) {
      System.out.println("ERROR");
    }

    return response.toString().trim();
  }

  /**
   * Closes connection to server
   * @throws IOException
   */
  public static void close() throws IOException {
    try {
      in.close();
      out.close();
      socket.close();
    } finally {
      in = null;
      out = null;
      socket = null;
    }
  }

}