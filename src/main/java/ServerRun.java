import java.rmi.RemoteException;

public class ServerRun {
    public static void main(String[] args) throws RemoteException {
        RMILoginServerView view = new RMILoginServerView();
    }
}