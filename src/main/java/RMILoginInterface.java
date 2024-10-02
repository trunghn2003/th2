import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMILoginInterface extends Remote{
    public String checkLogin(User user) throws RemoteException;

}