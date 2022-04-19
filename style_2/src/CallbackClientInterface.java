import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

public interface CallbackClientInterface extends Remote {

    public void saveLink(String fileName, CallbackClientInterface link) throws RemoteException;

    public Set<String> showAvailableFiles() throws RemoteException;

    public CallbackClientInterface getDownloadLink(String fileName) throws RemoteException;

    public void downloadFile(String fileName, String filePath, CallbackClientInterface link, CallbackClientInterface client) throws RemoteException;

    public void consumeData(String fileName, byte[] data, int length) throws RemoteException;

    public String giveMeSourceDirectory() throws RemoteException;

    public String getFilePaths(String fileName) throws RemoteException;

    public String[] getFileNames() throws RemoteException;
}
