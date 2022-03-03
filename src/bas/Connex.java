package bas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connex
{
    Connection connexion;
    public Connex()throws ClassNotFoundException, SQLException
    {
        Class.forName ("com.mysql.jdbc.Driver");
        Connection connex=DriverManager.getConnection( "jdbc:mysql://localhost/TestFrame","root", "" );
        this.connexion=connex;
    }

    public Connection getConnexion() {
        return connexion;
    }

    public void setConnexion(Connection connexion) {
        this.connexion = connexion;
    }
    
}