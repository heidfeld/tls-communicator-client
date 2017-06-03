package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseUtil {
	
	public static final String DRIVER = "org.sqlite.JDBC";
    public static final String DB_URL = "jdbc:sqlite:tls.db";
 
    private static Connection conn;
    private static Statement stat;
    
    static {
        try {
            Class.forName(DataBaseUtil.DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.println("Brak sterownika JDBC");
            e.printStackTrace();
        }
 
        try {
            conn = DriverManager.getConnection(DB_URL);
            stat = conn.createStatement();
        } catch (SQLException e) {
            System.err.println("Problem z otwarciem polaczenia");
            e.printStackTrace();
        }
        createTables();
    }
    
    public static boolean createTables()  {
        String createTls = "CREATE TABLE IF NOT EXISTS tls (password varchar(255));";
        try {
            stat.execute(createTls);
        } catch (SQLException e) {
            System.err.println("Blad przy tworzeniu tabeli");
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
	public static char[] getTlsPassword() {
		return getTextTlsPassword().toCharArray();
	}
	
	private static String getTextTlsPassword() {
		try {
            ResultSet result = stat.executeQuery("SELECT password FROM tls");
            String password = "";
            while(result.next()) {
                password = result.getString("password");
                break;
            }
            closeConnection();
            return password;
        } catch (SQLException e) {
            throw new RuntimeException("Cannot get tls password from database.");
        }
	}
	
    public static void closeConnection() {
        try {
        	stat.close();
            conn.close();
        } catch (SQLException e) {
            System.err.println("Problem z zamknieciem polaczenia");
            e.printStackTrace();
        }
    }
    
	public static boolean setTlsPassword(String password) {
        try {
            PreparedStatement prepStmt = conn.prepareStatement(
                    "insert into tls values (?);");
            prepStmt.setString(1, password);
            prepStmt.execute();
        } catch (SQLException e) {
        	closeConnection();
            System.err.println("Insert error");
            e.printStackTrace();
            return false;
        }
        closeConnection();
        return true;
	}

}
