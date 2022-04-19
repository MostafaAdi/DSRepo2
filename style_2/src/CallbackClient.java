import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.NoSuchElementException;

public class CallbackClient {


    public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {

        try {

            InputStreamReader is = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(is);


            System.out.println("enter rmi registry port number");
            String portNum = br.readLine();


            String registryURL = "rmi://localhost:" + portNum + "/callback";

            CallbackServerInterface h = (CallbackServerInterface) Naming.lookup(registryURL);

            System.out.println("Lookup completed");
            System.out.println("Server said " + h.sayHello());

            System.out.println("enter folder name you want to share, Please?(Folder must be in E drive)");
            String path = br.readLine();
            CallbackClientInterface callbackObj = new CallbackClientImpl(path);
            h.registerForCallback(callbackObj, callbackObj.getFileNames());
            System.out.println("Registered for callback.");

            boolean quit;
            do {
                String[] options = {"Find File", "Download File", "Exit"};
                int choice = JOptionPane.showOptionDialog(null, "Choose an action", "Option dialog", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

                switch (choice) {
                    case 0 -> {
                        String fileName = JOptionPane.showInputDialog("Type the name of the file you want to find.");
                        try {
                            CallbackClientInterface response = h.searchFile(fileName);
                            if (response == null) {
                                JOptionPane.showMessageDialog(null, "No Such File" + "\n");
                                break;
                            }

                            callbackObj.saveLink(fileName, response);
                            JOptionPane.showMessageDialog(null, "Found file, saving download link---" + "\n");

                        } catch (NoSuchElementException ex) {
                            JOptionPane.showMessageDialog(null, "Not found");
                        }
                    }
                    case 1 -> {

                        //first we display available files to user
                        String[] availableFiles = callbackObj.showAvailableFiles().toArray(new String[0]);
                        if (availableFiles.length == 0) {
                            JOptionPane.showMessageDialog(null, "Not download links found");
                            break;
                        }
                        int choose = JOptionPane.showOptionDialog(null, "Choose a file", "Option dialog", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, availableFiles, availableFiles[0]);

                        //now that the user chose the file, we need to get that file's path

                        callbackObj.downloadFile(availableFiles[choose] //file to download
                                , callbackObj.getFilePaths(availableFiles[choose]) // file path
                                , callbackObj.getDownloadLink(availableFiles[choose]), //returns remote reference of client who has file
                                callbackObj // we send our reference to receive data
                        );
                    }
                    default -> {
                        h.unregisterForCallback(callbackObj);
                        System.out.println("Unregistered for callback.");
                        System.exit(0);
                    }
                }
                quit = (JOptionPane.showConfirmDialog(null, "Do you want to exit?", "Exit", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION);
                if (!quit) {
                    h.unregisterForCallback(callbackObj);
                    System.out.println("Unregistered for callback.");
                    System.exit(0);
                }
            } while (true);

        } catch (Exception e) {

            System.out.println("Exception in CallbackClient: " + e);
        }



    }
}
