import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;

public class CallbackServer {

    public static void main(String[] args) {


        InputStreamReader is = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(is);
        String portNum, registryURL;

        try {

            System.out.println("Enter the RMI registry port number: ");
            portNum = (br.readLine()).trim();
            int RMIPortNum = Integer.parseInt(portNum);
            startRegistry(RMIPortNum);

            CallbackServerImpl exportedObj = new CallbackServerImpl();
            registryURL = "rmi://localhost:" + portNum + "/callback";
            Naming.rebind(registryURL, exportedObj);
            System.out.println("Callback Server ready.");

        } catch (Exception e) {
            System.out.println("Exception in HelloServer.main: " + e);
        }
    }

    private static void startRegistry(int RMIPortNum) throws RemoteException {

        try {
            Registry registry = LocateRegistry.getRegistry(RMIPortNum);
            System.out.println(Arrays.toString(registry.list()));
        }
        catch (RemoteException e) {

            Registry registry = LocateRegistry.createRegistry(RMIPortNum);
        }
    }
}
