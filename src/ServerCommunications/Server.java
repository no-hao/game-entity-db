package ServerCommunications;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
  private static final int PORT = 4446;

  public static void main(String[] args) {
    Database db = new Database();
    db.createConnection();

    /**
     * Main method to run the server.
     * Listens for client connections and handles SQL commands.
     */
    try (ServerSocket serverSocket = new ServerSocket(PORT)) {
      System.out.println("Server started on port " + PORT + ". Waiting for clients...");

      while (true) {
        try (
                Socket clientSocket = serverSocket.accept();
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
          System.out.println("Client connected: " + clientSocket.getInetAddress());
          String inputLine;
          while ((inputLine = in.readLine()) != null) {
            if (inputLine.equalsIgnoreCase("exit")) {
              System.out.println("Client requested disconnect");
              break;
            }
            System.out.println("Received: " + inputLine);
            String response = db.handleMessage(inputLine);
            out.println(response);
            out.println();
          }
          System.out.println("Client disconnected: " + clientSocket.getInetAddress());
        } catch (IOException e) {
          System.err.println("Client handling error: " + e.getMessage());
        }
      }
    } catch (IOException e) {
      System.err.println("Server fatal error: " + e.getMessage());
    } finally {
      db.closeConnection();
      System.out.println("Server terminated.");
    }
  }
}
