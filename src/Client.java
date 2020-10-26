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
        int numberOfPacket = 10;
        for(int i=0;i<numberOfPacket;i++)
        {
            Thread.sleep(1000);
            int id = random.nextInt(endDevices.size());
            String msg = "sending msg no "+(i+1)+" from "+thisDevice.getDeviceID()+" to "+(id+1);
            String specialMsg = "";
            if(i==20)
            {
                specialMsg="SHOW_ROUTE";
            }
            Packet p = new Packet(msg,specialMsg, thisDevice.getIpAddress(), endDevices.get(id).getIpAddress());
            networkUtility.write(p);
            //System.out.println("hey");
            String response = (String) networkUtility.read();
            Boolean success = (Boolean) networkUtility.read();
            int hopCount = (Integer) networkUtility.read();
            if(success==true)
            {
                totalHops+=hopCount;
            }
            else
            {
                totalDrop++;
            }
            System.out.println("client "+thisDevice.getDeviceID()+": \n"+response);
        // private String message;
        // private String specialMessage;  //ex: "SHOW_ROUTE" request
        // private IPAddress destinationIP;
        // private IPAddress sourceIP;
        // int hopcount;


        }
        System.out.println("Total packet drop: "+(totalDrop));
        if(totalDrop==numberOfPacket)
        {
            System.out.println("Average hop: 0");
        }
        else
        {
            System.out.println("Average hop: "+(totalHops/(numberOfPacket-totalDrop)));
        }
        
        networkUtility.write(new Packet("","STOP",null,null));
        networkUtility.closeConnection();
        
    }
}
