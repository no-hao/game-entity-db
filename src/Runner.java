//import Display.DisplayRenderer;
import Table.SchemaCreator;

public class Runner {

  public static void main(String args[]) {
    // Initialize database schema
    System.out.println("Starting database schema creation...");
    SchemaCreator schemaCreator = new SchemaCreator();
  
    
    // Start the application UI
    // DisplayRenderer dr = new DisplayRenderer();

    System.out.println("Database schema has been successfully created!");
  }

}