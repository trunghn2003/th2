import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMILoginClientControl {
    private String serverHost = "localhost";
    private int serverPort = 3232;
    private RMILoginInterface rmiServer;
    private Registry registry;
    private String rmiService = "rmiLoginServer";
    public RMILoginClientControl(){
        try{
// lay the dang ki
            registry = LocateRegistry.getRegistry(serverHost,
                    serverPort);
// tim kiem RMI server
            rmiServer = (RMILoginInterface)
                    (registry.lookup(rmiService));
        }catch(RemoteException e){
            e.printStackTrace();
        }catch(NotBoundException e){
            e.printStackTrace();
        }
    }

    public String remoteCheckLogin(User user){
        String result = null;
        try {
            result = rmiServer.checkLogin(user);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return result;
    }
}