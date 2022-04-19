import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CallbackServerInterface extends Remote {


    public String sayHello() throws RemoteException;

    public void registerForCallback(CallbackClientInterface callbackClientObj, String[] fileNames) throws RemoteException;

    public void unregisterForCallback(CallbackClientInterface callbackClientObj) throws RemoteException;

    public CallbackClientInterface searchFile(String fileName) throws RemoteException;
}
