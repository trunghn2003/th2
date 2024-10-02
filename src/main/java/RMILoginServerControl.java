import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class RMILoginServerControl extends UnicastRemoteObject
        implements RMILoginInterface{
    private int serverPort = 3232;
    private Registry registry;
    private Connection con;
    private String rmiService = "rmiLoginServer";

    public RMILoginServerControl() throws RemoteException{
        getDBConnection("th1", "root", "your_password");
// dang ki RMI server
        try{
            registry = LocateRegistry.createRegistry(serverPort);
            registry.rebind(rmiService, this);
        }catch(RemoteException e){
            throw e;
        }
    }
    public String checkLogin(User user) throws RemoteException{
        String result = "";
        if(checkUser(user))
            result = "ok";
        return result;
    }

    private void getDBConnection(String dbName, String username,
                                 String password){
        String dbUrl = "jdbc:mysql://localhost:3307/" + dbName;
        String dbClass = "com.mysql.jdbc.Driver";
        try {
            Class.forName(dbClass);
            con = DriverManager.getConnection (
                    dbUrl, username, password);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkUser(User user) {
        String query = "Select * FROM users WHERE username ='"
                + user.getUserName()
                + "' AND password ='" + user.getPassword() + "'";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                return true;
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}