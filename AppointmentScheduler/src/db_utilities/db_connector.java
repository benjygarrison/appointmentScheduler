
package db_utilities;

/**
 *
 * @author Ben Garrison
 */
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class db_connector 
{
    
    private static Connection conn;
    
    public db_connector(){}
      
    private static final String url = ""; //Database disconnected - see schema
    private static final String user = ""; //Database disconnected - see schema
    private static final String pass = ""; //Database disconnected - see schema
    

    
    public static void init()
    {
        System.out.println("Connecting to the database");
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            
            conn = DriverManager.getConnection(url, user, pass);
        }
        catch (ClassNotFoundException ce)
        {
            System.out.println("Cannot find the right class.  Did you remember to add the mysql library to your Run Configuration?");
            ce.printStackTrace();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
    
    //Returns Connection
    public static Connection getConn(){
    
        System.out.println("Connected to the database");
        return conn;                        
    }
    
    //Closes connections
    public static void closeConn(){
        try{
            conn.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        finally{
            System.out.println("Connection closed.");
        }
    }
    
}    
    