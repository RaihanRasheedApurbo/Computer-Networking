import java.util.ArrayList;
import java.util.Random;

//import java.util.ArrayList;

//Work needed
public class Client {
    public static void main(String[] args) throws InterruptedException {
        NetworkUtility networkUtility = new NetworkUtility("127.0.0.1", 4444);
        System.out.println("Connected to server");
        /**
         * Tasks
         */
        
        /*
        1. Receive EndDevice configuration from server
        2. Receive active client list from server
        3. for(int i=0;i<100;i++)
        4. {
        5.      Generate a random message
        6.      Assign a random receiver from active client list
        7.      if(i==20)
        8.      {
        9.            Send the message and recipient IP address to server and a special request "SHOW_ROUTE"
        10.           Display routing path, hop count and routing table of each router [You need to receive
                            all the required info from the server in response to "SHOW_ROUTE" request]
        11.     }
        12.     else
        13.     {
        14.           Simply send the message and recipient IP address to server.
        15.     }
        16.     If server can successfully send the message, client will get an acknowledgement along with hop count
                    Otherwise, client will get a failure message [dropped packet]
        17. }
        18. Report average number of hops and drop rate
        */


        EndDevice thisDevice = (EndDevice) networkUtility.read();
        ArrayList<EndDevice> endDevices = (ArrayList<EndDevice>) networkUtility.read();
        System.out.println("This is client "+thisDevice.getDeviceID());
        System.out.println(endDevices.size());
        int totalDrop = 0;
        int totalHops = 0;
        for(EndDevice t: endDevices)
        {
            System.out.println("client: "+t.getDeviceID()+" Gateway: "+t.getGateway().getString()+" IP Adress: "+t.getIpAddress().getString());
        }
        Random random = new Random(System.currentTimeMillis());
        
        for(int i=0;i<100;i++)
        {
            String msg = "sending msg no "+i+1+" from "+thisDevice.getDeviceID();
            int id = random.nextInt(endDevices.size());
            String specialMsg = "";
            if(i==20)
            {
                specialMsg="SHOW_ROUTE";
            }
            Packet p = new Packet(msg,specialMsg, thisDevice.getIpAddress(), endDevices.get(id).getIpAddress());
            networkUtility.write(p);
            String response = (String) networkUtility.read();
            System.out.println("client "+thisDevice.getDeviceID()+": "+response);
        // private String message;
        // private String specialMessage;  //ex: "SHOW_ROUTE" request
        // private IPAddress destinationIP;
        // private IPAddress sourceIP;
        // int hopcount;


        }
        networkUtility.closeConnection();
        
    }
}
