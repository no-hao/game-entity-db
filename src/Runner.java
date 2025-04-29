//import Table.DisplayRenderer;
import DB.SchemaCreator;
import DB.TableFill;
import Table.DisplayRenderer;
import ServerCommunications.Server;

/**
 * --> Main file to run <--
 * for executing all 4 parts of hw assignment
 *
 * @author Zach Kline
 */
public class Runner {

  public static void main(String args[]) throws Exception {
    // Initialize database schema (Part 1)
    System.out.println("Starting database schema creation...");
    SchemaCreator schemaCreator = new SchemaCreator();
    System.out.println("Database schema has been successfully created!");


    //Table Fill (Part 2)
    TableFill pro = new TableFill();
    pro.activateJDBC();
    pro.createConnection();
    long startTime = System.currentTimeMillis();
    pro.Make();
    long endTime = System.currentTimeMillis();
    long result = endTime - startTime;
    System.out.println(result);


    //Server (Part 3)
    Thread serverThread = new Thread(() -> {
      try {
        new Server();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }, "Server-Thread");
    serverThread.setDaemon(false);
    serverThread.start();
    
    // Start the application UI (part 4)
     DisplayRenderer dr = new DisplayRenderer();
  }

}