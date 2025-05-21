# Database Management System

This project is a comprehensive database management system that implements a game-like environment with various entities such as players, characters, items, abilities, and locations. The system is built using Java and MySQL, featuring a client-server architecture with a graphical user interface.

## Project Structure

The project is organized into several key components:

### 1. Database Layer (`src/DB/`)
- `SchemaCreator.java`: Handles the creation of the database schema with all necessary tables and relationships
- `TableFill.java`: Populates the database with initial data for testing and demonstration

### 2. Server Communications (`src/ServerCommunications/`)
- `Server.java`: Implements the server-side logic that handles client connections and database operations
- `Database.java`: Manages database connections and executes SQL commands
- `DBDataProvider.java`: Acts as a bridge between the client and database operations
- `Client.java`: Handles client-side communication with the server

### 3. User Interface (`src/Table/`)
- `DisplayRenderer.java`: Main UI component that displays the database tables
- Multiple data providers for different views:
  - `DisplayOneDataProvider.java`: Manages game character data
  - `DisplayTwoDataProvider.java`: Handles player data
  - `DisplayThreeDataProvider.java`: Manages ability data

### 4. Exceptions (`src/Exceptions/`)
Custom exception classes for handling various error scenarios in the application

## Database Schema

The database includes several interconnected tables:

- `PERSON`: Base table for all users
- `MANAGER`: Subset of persons with management privileges
- `MODERATOR`: Subset of persons with moderation capabilities
- `PLAYER`: Subset of persons who are players
- `LOCATION`: Game locations
- `GAMECHARACTER`: Player characters
- `ITEM`: Base table for all items
- `ARMOR`: Specific type of items
- `CONTAINER`: Items that can hold other items
- `WEAPON`: Specific type of items
- `ABILITY`: Character abilities
- `CREATURE`: Game creatures
- Various relationship tables (e.g., `CREATURELIKES`, `PLAYERHATES`, etc.)

## Features

1. **Database Management**
   - Schema creation and initialization
   - Data population and management
   - Relationship handling between entities

2. **Client-Server Architecture**
   - Secure communication between client and server
   - SQL command handling
   - Real-time data updates

3. **User Interface**
   - Table displays for different entities
   - Data manipulation (insert, update, delete)
   - Real-time updates

4. **Security**
   - User authentication
   - Role-based access control
   - Secure database operations

## How to Run

1. **Prerequisites**
   - Java JDK (version 8 or higher)
   - MySQL Server
   - MySQL JDBC Driver

2. **Database Setup**
   - Ensure MySQL server is running
   - The application will automatically create the schema and populate initial data

3. **Running the Application**
   ```bash
   # Compile the project
   javac -cp ".:mysql-connector-java-8.0.xx.jar" src/*.java

   # Run the application
   java -cp ".:mysql-connector-java-8.0.xx.jar" src.Runner
   ```

4. **Using the Application**
   - The server will start automatically
   - The GUI will launch, showing different views of the database
   - You can perform CRUD operations through the interface

## Database Connection

The application connects to a MySQL database with the following default settings:
- Host: db.engr.ship.edu
- Port: 3306
- Database: cmsc471_18
- Username: cmsc471_18
- Password: Password_18

## Error Handling

The application includes comprehensive error handling for:
- Database connection issues
- SQL command execution errors
- Invalid data operations
- Client-server communication problems

## Contributing

This project was developed as part of a database systems course. For any questions or issues, please contact the development team.

## License

This project is proprietary and confidential. Unauthorized copying, distribution, or use is strictly prohibited. 