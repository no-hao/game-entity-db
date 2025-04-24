package ServerCommunications;

import java.io.*;
import java.net.*;

public class Server {
  private static final int PORT = 4446;

  public static void main(String[] args) {
    Database db = new Database();
//    db.activateJDBC();
    db.createConnection();

    try (ServerSocket serverSocket = new ServerSocket(PORT)) {
      System.out.println("Server started. Waiting for clients...");

      while (true) {
        try (Socket clientSocket = serverSocket.accept();
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

          System.out.println("Client connected: " + clientSocket.getInetAddress());

          String line;
          while ((line = in.readLine()) != null) {
            System.out.println("Received: " + line);
            String response = db.handleMessage(line);
            out.println(response);
          }

          System.out.println("Client disconnected.");
        } catch (IOException e) {
          System.out.println("Error handling client: " + e.getMessage());
        }
      }

    } catch (IOException e) {
      System.out.println("Server error: " + e.getMessage());
    }
  }
}
