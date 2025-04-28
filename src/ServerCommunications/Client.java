package ServerCommunications;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
  private static final String SERVER_IP = "localhost";
  private static final int PORT = 4446;

  /**
   * Main method to run the client.
   * Connects to the server and allows the user to send SQL commands.
   */
  public static void main(String[] args) {
    try (
            Socket socket = new Socket(SERVER_IP, PORT);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner scanner = new Scanner(System.in)
    ) {
      System.out.println("Connected to server. Enter SQL commands (or 'exit' to quit):");
      System.out.println("Format examples:");
      System.out.println("  SELECT:table:column:operator:value");
      System.out.println("  INSERT:table:col1:col2:val1:val2");
      System.out.println("  UPDATE:table:column:newValue:whereColumn:whereValue");
      System.out.println("  DELETE:table:column:value");
      System.out.println("  PROC:procedure_name:param1");

      while (true) {
        System.out.print("> ");
        String input = scanner.nextLine().trim();

        if (input.equalsIgnoreCase("exit")) {
          out.println(input);
          break;
        }

        if (input.isEmpty()) continue;
        out.println(input);
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
          if (line.isEmpty()) break;
          response.append(line).append("\n");
        }
        System.out.println("Server response:\n" + response.toString().trim());
      }
    } catch (UnknownHostException e) {
      System.err.println("Error: Server not found. " + e.getMessage());
    } catch (IOException e) {
      System.err.println("Error: Communication failure. " + e.getMessage());
    } finally {
      System.out.println("Client terminated.");
    }
  }
}