import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class CallbackServerImpl extends UnicastRemoteObject implements CallbackServerInterface {

    private final Map<CallbackClientInterface, String[]> clientFiles;

    public CallbackServerImpl() throws RemoteException {
        super();
        clientFiles = new HashMap<>();
    }

    @Override
    public String sayHello() throws RemoteException {
        return "Hello";
    }

    @Override
    public synchronized void registerForCallback(CallbackClientInterface callbackClientObj, String[] fileNames) throws RemoteException {
        if (!(clientFiles.containsKey(callbackClientObj))) {

            clientFiles.put(callbackClientObj, fileNames);
            System.out.println("Registered new client ");

        }
    }

    @Override
    public synchronized void unregisterForCallback(CallbackClientInterface callbackClientObj) throws RemoteException {
        if (clientFiles.remove(callbackClientObj, clientFiles.get(callbackClientObj))) {
            System.out.println("Unregistered client!");
        } else {
            System.out.println(
                    "unregister: client wasn't registered.");
        }
    }

    //TODO implement searchFiles
    @Override
    public CallbackClientInterface searchFile(String fileName) throws RemoteException {
        //for each key value in client files
        //search value for fileName
        //if fileNAme was found in value -> return key

        System.out.println("**********************************\n"
                + "Searching For File ----");

        Predicate<String> predicate = x -> x.equals(fileName);

        for (Map.Entry<CallbackClientInterface, String[]> entry : clientFiles.entrySet()) {
            String[] currentFiles = entry.getValue();
            if (Arrays.stream(currentFiles).anyMatch(predicate)) {
                return entry.getKey();
            }
        }
        System.out.println("number of registered clients: " + clientFiles.size());

        return null;
    }
}
