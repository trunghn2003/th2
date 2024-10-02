import java.rmi.RemoteException;

public class RMILoginServerView {
    public RMILoginServerView() throws RemoteException {
        new RMILoginServerControl();
        showMessage("RMI server is running...");
    }
    public void showMessage(String msg){
        System.out.println(msg);
    }
}