package DB;

import java.sql.*;
import java.time.LocalDate;
import java.util.Random;


/**
 * Fill all tables in DB with data
 * Part II of HW 5
 *
 * @author Adam Czorapinski
 */
public class TableFill {

	/**
	 * This is the recommended way to activate the JDBC drivers, but is
	 * only setup to work with one specific driver.  Setup to work with
	 * a MySQL JDBC driver.
	 *
	 * If the JDBC Jar file is not in your build path this will not work.
	 * I have the Jar file posted in D2L.
	 * 
	 * @return Returns true if it successfully sets up the driver.
	 */
	public boolean activateJDBC()
	{
	    try
	    {
	        DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
	    }
	    catch (SQLException sqle)
	    {
	        sqle.printStackTrace();
	    }

	    return true;
	}


	/**
	 * url to access db
	 */
	public static final String DB_LOCATION = "jdbc:mysql://db.engr.ship.edu:3306/cmsc471_18?useTimezone=true&serverTimezone=UTC";

	/**
	 * DB login name
	 */
	public static final String LOGIN_NAME = "cmsc471_18";

	/**
	 * DB password
	 */
	public static final String PASSWORD = "Password_18";
	protected Connection m_dbConn = null;

	/** 
	* Creates a connection to the database that you can then send commands to.
	* Needs to be in a method.
	*/
	public void createConnection() {
		try {
			m_dbConn = DriverManager.getConnection(DB_LOCATION, LOGIN_NAME, PASSWORD);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * Create a random string
	 * with letters and numbers
	 * @param n length
	 * @return
	 */
	 static String getRandString(int n) 
	 { 
	 
	  String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
	         + "0123456789"
	         + "abcdefghijklmnopqrstuvxyz"; 
	 
	  StringBuilder sb = new StringBuilder(n); 
	 
	  for (int i = 0; i < n; i++) { 
	 
	   int index = (int)(AlphaNumericString.length() * Math.random()); 
	 

	   sb.append(AlphaNumericString.charAt(index)); 
	  } 
	 
	  return sb.toString(); 
	 }

	/**
	 * Fills all tables in DB with
	 * at least 5 values
	 * @throws Exception
	 */
	public void Make() throws Exception
	{
	    // Using Statement to insert a value
	    // Best used when all values are hard coded.
	    Statement stmt = m_dbConn.createStatement();
	    String insertData;
	    Random rand = new Random();

	    // Using a PreparedStatement to insert a value (best option when providing values
	    // from variables).
	    // Use place holders '?' to mark where I am going to provide the data.
	    for(int i = 1; i <= 15; i++) {
				insertData = new String("INSERT INTO PERSON (loginId,email,password,dateCreated) VALUES (?,?,?,?)");
				PreparedStatement stmt2 = m_dbConn.prepareStatement(insertData);
				stmt2.setString(1, "Person" + Integer.toString(i));
				stmt2.setString(2, "Email" + Integer.toString(i));
				stmt2.setString(3, getRandString(10));
				Date d = Date.valueOf(LocalDate.now());
				stmt2.setDate(4,d);
				stmt2.executeUpdate();
	    }

	    for(int i = 1; i <= 5; i++) {
		    insertData = new String("INSERT INTO MANAGER (loginId) VALUES (?)");
		    PreparedStatement stmt2 = m_dbConn.prepareStatement(insertData);
		    stmt2.setString(1, "Person" + Integer.toString(i));
		    stmt2.executeUpdate();
		   }
	    for(int i = 6; i <= 10; i++) {
		    insertData = new String("INSERT INTO MODERATOR (loginId,isSilenced,isBlocked,worksWith) VALUES (?,?,?,?)");
		    PreparedStatement stmt2 = m_dbConn.prepareStatement(insertData);
		    stmt2.setString(1, "Person" + Integer.toString(i));
		    stmt2.setString(4, "Person" + Integer.toString(i-5));
		    stmt2.setBoolean(2, false);
		    stmt2.setBoolean(3, false);
		    stmt2.executeUpdate();
		    }
	    for(int i = 11; i <= 15; i++) {
		    insertData = new String("INSERT INTO PLAYER (loginId,isSilenced,isBlocked,watchedBy) VALUES (?,?,?,?)");
		    PreparedStatement stmt2 = m_dbConn.prepareStatement(insertData);
		    stmt2.setString(1, "Person" + Integer.toString(i));
		    stmt2.setString(4, "Person" + Integer.toString(i-5));
		    stmt2.setBoolean(2, false);
		    stmt2.setBoolean(3, false);
		    stmt2.executeUpdate();
		    }
	    for(int i = 1; i <= 10; i++) {
		    insertData = new String("INSERT INTO LOCATION (lId,size,type) VALUES (?,?,?)");
		    PreparedStatement stmt2 = m_dbConn.prepareStatement(insertData);
		    stmt2.setString(1, "Loc" + Integer.toString(i));
		    stmt2.setString(3, "Land");
		    stmt2.setInt(2, i);
		    stmt2.executeUpdate();
		    }
	    for(int i = 1; i <= 5; i++) {
		    insertData = new String("INSERT INTO EXITSTO (enterId,exitId) VALUES (?,?)");
		    PreparedStatement stmt2 = m_dbConn.prepareStatement(insertData);
		    stmt2.setString(1, "Loc" + Integer.toString(i));
		    stmt2.setString(2, "Loc" + Integer.toString(i+5));
		    stmt2.executeUpdate();
		   }
	    for(int i = 11; i <= 15; i++) {
		    insertData = new String("INSERT INTO GAMECHARACTER (name,playerId,maxPoints,currentPoints,stamina,strength,locationId) VALUES (?,?,?,?,?,?,?)");
		    PreparedStatement stmt2 = m_dbConn.prepareStatement(insertData);
		    stmt2.setString(1, "PC" + Integer.toString(i-10));
		    stmt2.setString(2, "Person" + Integer.toString(i));
		    int hp = rand.nextInt(11);
		    stmt2.setInt(3, hp);
		    stmt2.setInt(4, hp);
		    stmt2.setInt(5, rand.nextInt(11));
		    stmt2.setInt(6, rand.nextInt(11));
		    stmt2.setString(7, "Loc" + Integer.toString(i-10));
		    stmt2.executeUpdate();
		    }
	    for(int i = 1; i <= 10; i++) {
		    insertData = new String("INSERT INTO ITEM (IdItem,volume,weight,isTwoHanded) VALUES (?,?,?,?)");
		    PreparedStatement stmt2 = m_dbConn.prepareStatement(insertData);
		    stmt2.setString(1, "Item" + Integer.toString(i));
		    stmt2.setInt(2, i);
		    stmt2.setDouble(3, 1.0);
		    stmt2.setBoolean(4, false);
		    stmt2.executeUpdate();
		    }
	    for(int i = 1; i <= 5; i++) {
		    insertData = new String("INSERT INTO ITEM (IdItem,volume,weight,isTwoHanded,ownedBy) VALUES (?,?,?,?,?)");
		    PreparedStatement stmt2 = m_dbConn.prepareStatement(insertData);
		    stmt2.setString(1, "Armor" + Integer.toString(i));
		    stmt2.setInt(2, i);
		    stmt2.setDouble(3, 1.0);
		    stmt2.setBoolean(4, false);
		    stmt2.setString(5, "PC" + Integer.toString(i));
		    stmt2.executeUpdate();
		    }
	    for(int i = 1; i <= 5; i++) {
		    insertData = new String("INSERT INTO ARMOR (aId,protection,equipLocation,isEquiped) VALUES (?,?,?,?)");
		    PreparedStatement stmt2 = m_dbConn.prepareStatement(insertData);
		    stmt2.setString(1, "Armor" + Integer.toString(i));
		    stmt2.setString(3, "Head");
		    stmt2.setInt(2, rand.nextInt(11));
		    stmt2.setBoolean(4, true);
		    stmt2.executeUpdate();
		    }
	    for(int i = 1; i <= 5; i++) {
		    insertData = new String("INSERT INTO ITEM (IdItem,volume,weight,isTwoHanded) VALUES (?,?,?,?)");
		    PreparedStatement stmt2 = m_dbConn.prepareStatement(insertData);
		    stmt2.setString(1, "Container" + Integer.toString(i));
		    stmt2.setInt(2, i);
		    stmt2.setDouble(3, 1.0);
		    stmt2.setBoolean(4, false);
		    stmt2.executeUpdate();
		    }
	    for(int i = 1; i <= 5; i++) {
		    insertData = new String("INSERT INTO CONTAINER (cId,volume,weight) VALUES (?,?,?)");
		    PreparedStatement stmt2 = m_dbConn.prepareStatement(insertData);
		    stmt2.setString(1, "Container" + Integer.toString(i));
		    stmt2.setInt(2, 20);
		    stmt2.setDouble(3, 10.0);
		    stmt2.executeUpdate();
		    }
	    for(int i = 1; i <= 5; i++) {
		    insertData = new String("INSERT INTO CONTAINEDIN (cId,IdItem) VALUES (?,?)");
		    PreparedStatement stmt2 = m_dbConn.prepareStatement(insertData);
		    stmt2.setString(1, "Container" + Integer.toString(i));
		    stmt2.setString(2, "Item" + Integer.toString(i+5));
		    stmt2.executeUpdate();
		    }
	    for(int i = 1; i <= 10; i++) {
		    insertData = new String("INSERT INTO ABILITY (name,targetStat,amount,durationToExecute,cooldown,uses) VALUES (?,?,?,?,?,?)");
		    PreparedStatement stmt2 = m_dbConn.prepareStatement(insertData);
		    stmt2.setString(1, "Ability" + Integer.toString(i));
		    stmt2.setString(2, "HP");
		    stmt2.setInt(3, rand.nextInt(11));
		    stmt2.setInt(4, rand.nextInt(11));
		    stmt2.setInt(5, rand.nextInt(11));
		    stmt2.setDouble(6, 1.0);
		    stmt2.executeUpdate();
		    }
	    for(int i = 1; i <= 5; i++) {
		    insertData = new String("INSERT INTO ITEM (IdItem,volume,weight,isTwoHanded,ownedBy) VALUES (?,?,?,?,?)");
		    PreparedStatement stmt2 = m_dbConn.prepareStatement(insertData);
		    stmt2.setString(1, "Weapon" + Integer.toString(i));
		    stmt2.setInt(2, i);
		    stmt2.setDouble(3, 1.0);
		    stmt2.setBoolean(4, false);
		    stmt2.setString(5, "PC" + Integer.toString(i));
		    stmt2.executeUpdate();
		    }
	    for(int i = 1; i <= 5; i++) {
		    insertData = new String("INSERT INTO WEAPON (wId,ability) VALUES (?,?)");
		    PreparedStatement stmt2 = m_dbConn.prepareStatement(insertData);
		    stmt2.setString(1, "Weapon" + Integer.toString(i));
		    stmt2.setString(2, "Ability" + Integer.toString(i));
		    stmt2.executeUpdate();
		    }
	    for(int i = 1; i <= 10; i++) {
		    insertData = new String("INSERT INTO CREATURE (name,damageProtection,currentPoints,stamina,maxPoints,locationId,ability) VALUES (?,?,?,?,?,?,?)");
		    PreparedStatement stmt2 = m_dbConn.prepareStatement(insertData);
		    stmt2.setString(1, "Creature" + Integer.toString(i));
		    stmt2.setInt(2, rand.nextInt(11));
		    int hp = rand.nextInt(11);
		    stmt2.setInt(3, hp);
		    stmt2.setInt(5, hp);
		    stmt2.setInt(4, rand.nextInt(11));
		    stmt2.setString(7, "Ability" + Integer.toString(i));
		    stmt2.setString(6, "Loc" + Integer.toString(i));
		    stmt2.executeUpdate();
		    }
	    for(int i = 1; i <= 5; i++) {
		    insertData = new String("INSERT INTO CREATURELIKES (cId,rId) VALUES (?,?)");
		    PreparedStatement stmt2 = m_dbConn.prepareStatement(insertData);
		    stmt2.setString(1, "Creature" + Integer.toString(i));
		    stmt2.setString(2, "Creature" + Integer.toString(i+5));
		    stmt2.executeUpdate();
		    }
	    for(int i = 1; i <= 5; i++) {
		    insertData = new String("INSERT INTO CREATUREHATES (cId,rId) VALUES (?,?)");
		    PreparedStatement stmt2 = m_dbConn.prepareStatement(insertData);
		    stmt2.setString(1, "Creature" + Integer.toString(i+5));
		    stmt2.setString(2, "Creature" + Integer.toString(i));
		    stmt2.executeUpdate();
		    }
	    for(int i = 1; i <= 5; i++) {
		    insertData = new String("INSERT INTO PLAYERLIKES (pId,rId) VALUES (?,?)");
		    PreparedStatement stmt2 = m_dbConn.prepareStatement(insertData);
		    stmt2.setString(1, "Person" + Integer.toString(i+10));
		    stmt2.setString(2, "Creature" + Integer.toString(i));
		    stmt2.executeUpdate();
		    }
	    for(int i = 1; i <= 5; i++) {
		    insertData = new String("INSERT INTO PLAYERHATES (pId,rId) VALUES (?,?)");
		    PreparedStatement stmt2 = m_dbConn.prepareStatement(insertData);
		    stmt2.setString(1, "Person" + Integer.toString(i+10));
		    stmt2.setString(2, "Creature" + Integer.toString(i+5));
		    stmt2.executeUpdate();
		    }
	    for(int i = 1; i <= 5; i++) {
		    insertData = new String("INSERT INTO ALLOWEDTOGO (cId,lId) VALUES (?,?)");
		    PreparedStatement stmt2 = m_dbConn.prepareStatement(insertData);
		    stmt2.setString(1, "Creature" + Integer.toString(i));
		    stmt2.setString(2, "Loc" + Integer.toString(i));
		    stmt2.executeUpdate();
		    }
	    for(int i = 1; i <= 5; i++) {
		    insertData = new String("INSERT INTO ITEMSPRESENT (lId,ItemId) VALUES (?,?)");
		    PreparedStatement stmt2 = m_dbConn.prepareStatement(insertData);
		    stmt2.setString(1, "Loc" + Integer.toString(i));
		    stmt2.setString(2, "Container" + Integer.toString(i));
		    stmt2.executeUpdate();
		    }
	    for(int i = 1; i <= 5; i++) {
		    insertData = new String("INSERT INTO CHARACTERSPRESENT (lId,cId) VALUES (?,?)");
		    PreparedStatement stmt2 = m_dbConn.prepareStatement(insertData);
		    stmt2.setString(1, "Loc" + Integer.toString(i));
		    stmt2.setString(2, "PC" + Integer.toString(i));
		    stmt2.executeUpdate();
		    }
	    for(int i = 1; i <= 5; i++) {
		    insertData = new String("INSERT INTO CREATURESPRESENT (lId,creatureId) VALUES (?,?)");
		    PreparedStatement stmt2 = m_dbConn.prepareStatement(insertData);
		    stmt2.setString(1, "Loc" + Integer.toString(i));
		    stmt2.setString(2, "Creature" + Integer.toString(i));
		    stmt2.executeUpdate();
			}
	}
	

}    
