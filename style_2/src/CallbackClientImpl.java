import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CallbackClientImpl extends UnicastRemoteObject implements CallbackClientInterface{

    private String[] fileNames;
    private String sourceDir;
    private Map<String, CallbackClientInterface> downloadLinks;
    private Map<String, String> filePaths;


    public CallbackClientImpl(String folderPath) throws RemoteException {
        super();
        File dir = new File("E:/" + folderPath);
        this.fileNames = dir.list();
        this.sourceDir = "E:/" + folderPath;
        downloadLinks = new HashMap<>();
        filePaths = new HashMap<>();
    }

    @Override
    public String[] getFileNames() {
        return fileNames;
    }

    @Override
    public void saveLink(String fileName, CallbackClientInterface link) throws RemoteException {
        if (!downloadLinks.containsKey(fileName))
            downloadLinks.put(fileName, link);

        filePaths.put(fileName, link.giveMeSourceDirectory());
    }

    @Override
    public Set<String> showAvailableFiles() throws RemoteException {
        return downloadLinks.keySet();
    }

    @Override
    public CallbackClientInterface getDownloadLink(String fileName) throws RemoteException {
        return downloadLinks.get(fileName);
    }

    @Override
    public synchronized void downloadFile(String fileName, String filePath, CallbackClientInterface link, CallbackClientInterface client) throws RemoteException {

        File f1 = new File(filePath + "/" + fileName);
        try {

            FileInputStream in = new FileInputStream(f1);
            byte[] myData = new byte[1024 * 1242];
            int len = in.read(myData);

            while (len > 0) {
                client.consumeData(f1.getName(), myData, len);
                len = in.read(myData);
            }

            downloadLinks.remove(fileName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void consumeData(String fileName, byte[] data, int length) throws RemoteException {
        System.out.println("start transfer!");
        File file = new File(this.sourceDir + "/" + fileName);

        try {
            file.createNewFile();
            FileOutputStream os = new FileOutputStream(file);
            os.write(data, 0, length);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        JOptionPane.showMessageDialog(null, "download complete" + "\n");
    }

    @Override
    public String giveMeSourceDirectory() throws RemoteException {
        return this.sourceDir;
    }

    @Override
    public String getFilePaths(String fileName) throws RemoteException {
        return filePaths.get(fileName);
    }
}
