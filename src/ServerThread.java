
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

//import sun.java2d.pipe.SpanShapeRenderer.Simple;

public class ServerThread implements Runnable {

    NetworkUtility networkUtility;
    EndDevice endDevice;

    ServerThread(NetworkUtility networkUtility, EndDevice endDevice) {
        this.networkUtility = networkUtility;
        this.endDevice = endDevice;
        System.out.println("Server Ready for client " + endDevice.getDeviceID());
        //NetworkLayerServer.clientCount++;
        //System.out.println("Server Ready for client " + NetworkLayerServer.clientCount);
        new Thread(this).start();
    }

    @Override
    public void run() {
        networkUtility.write(endDevice);
        networkUtility.write(NetworkLayerServer.endDevices);
        while(true)
        {
            
            Packet p = (Packet) networkUtility.read();
            if(p.getSpecialMessage().equals("STOP"))
            {
                networkUtility.closeConnection();
                break;
            }
            //System.out.println("hey 2");
            boolean sent = deliverPacket(p);
            //System.out.println("hey 3");
            if(sent==true)
            {
                networkUtility.write("transfer successful with "+p.hopcount+" and the msg was: "+p.getMessage());
                networkUtility.write(true);
                networkUtility.write(p.hopcount);
            }
            else
            {
                networkUtility.write("transfer unsuccessful with "+p.hopcount+" and the msg was: "+p.getMessage());
                networkUtility.write(false);
                networkUtility.write(p.hopcount);
            }
        }
        System.out.println("Successfully served client "+endDevice.getDeviceID());
        /**
         * Synchronize actions with client.
         */
        
        /*
        Tasks:
        1. Upon receiving a packet and recipient, call deliverPacket(packet)
        2. If the packet contains "SHOW_ROUTE" request, then fetch the required information
                and send back to client
        3. Either send acknowledgement with number of hops or send failure message back to client
        */
    }


    // public Boolean deliverPacket(Packet p) {
    //     // static int clientCount = 0;
    //     // static ArrayList<Router> routers = new ArrayList<>();
    //     // static RouterStateChanger stateChanger = null;
    //     // static Map<IPAddress,Integer> clientInterfaces = new HashMap<>(); //Each map entry represents number of client end devices connected to the interface
    //     // static Map<IPAddress, EndDevice> endDeviceMap = new HashMap<>();
    //     // static ArrayList<EndDevice> endDevices = new ArrayList<>();
    //     // static Map<Integer, Integer> deviceIDtoRouterID = new HashMap<>();
    //     // static Map<IPAddress, Integer> interfacetoRouterID = new HashMap<>();
    //     // static Map<Integer, Router> routerMap = new HashMap<>();

    //     // private String message;
    //     // private String specialMessage;  //ex: "SHOW_ROUTE" request
    //     // private IPAddress destinationIP;
    //     // private IPAddress sourceIP;
    //     // int hopcount;
    //     Map<IPAddress, EndDevice> endDeviceMap = NetworkLayerServer.endDeviceMap;
    //     Map<Integer, Integer> deviceIDtoRouterID = NetworkLayerServer.deviceIDtoRouterID;
    //     Map<Integer, Router> routerMap = NetworkLayerServer.routerMap;

    //     EndDevice sender = null;
    //     EndDevice receiver = null;
    //     // if(sender==null && receiver==null)
    //     // {
    //     //     System.out.println("both null");
    //     // }
    //     for(Map.Entry<IPAddress, EndDevice> entry : endDeviceMap.entrySet())
    //     {
    //         IPAddress key = entry.getKey();
    //         EndDevice value = entry.getValue();
    //         if(p.getSourceIP().getString().equals(key.getString()))
    //         {
    //             sender = value;
    //         }
    //         if(p.getDestinationIP().getString().equals(key.getString()))
    //         {
    //             receiver = value;
    //         }
            
    //     }
    //     if(sender==null && receiver==null)
    //     {
    //         System.out.println("still both null");
    //     }

        
    //     // System.out.println("hey 3");
    //     // if(sender==null)
    //     // {
    //     //     System.out.println("hey 4");
    //     // }
    //     // if(receiver==null)
    //     // {
    //     //     System.out.println("hey 5");
    //     // }
    //     //System.out.println(sender.getDeviceID());
    //     // for(Map.Entry<Integer, Integer> entry : deviceIDtoRouterID.entrySet())
    //     // {
    //     //     int key = entry.getKey();
    //     //     int value = entry.getValue();
    //     //     System.out.println(key+" "+value);
    //     //     if(deviceIDtoRouterID.get(1)==null)
    //     //     {
    //     //         System.out.println("kill meh please!");
    //     //     }
    //     // }

    //     int senderRouterID = deviceIDtoRouterID.get(sender.getDeviceID());
    //     int receiverRouterID = deviceIDtoRouterID.get(receiver.getDeviceID());
    //     Router senderRouter = routerMap.get(senderRouterID);
    //     Router receiverRouter = routerMap.get(receiverRouterID);

    //     if(senderRouter.getState()==false)
    //     {
    //         return false;
    //     }
    //     //Router prevRouter = null;
    //     Router currentRouter = senderRouter;
    //     while(currentRouter != receiverRouter)
    //     {
    //         ArrayList<RoutingTableEntry> routingTable = currentRouter.getRoutingTable();
    //         // if(prevRouter != null)
    //         // {
    //         //     int prevRouterID = prevRouter.getRouterId();
    //         //     if(routingTable.get(prevRouterID-1).getDistance() == Constants.INFINITY)
    //         //     {
    //         //         RouterStateChanger.islocked = true;
    //         //         RoutingTableEntry newEntry = new RoutingTableEntry(prevRouterID, 1, prevRouterID);
    //         //         routingTable.set(prevRouterID-1, newEntry);
    //         //         NetworkLayerServer.simpleDVR(currentRouter.getRouterId());
    //         //     }
                
    //         // }
    //         ArrayList<Integer> openedRouter = new ArrayList<>();
    //         for(int t: currentRouter.getNeighborRouterIDs())
    //         {
    //             Router r = routerMap.get(t);
                
    //             if(routingTable.get(t-1).getDistance() == Constants.INFINITY && r.getState() == true )
    //             {
    //                 openedRouter.add(t);
    //             }
    //         }
    //         if(openedRouter.size()>0)
    //         {
    //             RouterStateChanger.islocked = true;
    //             for(int t: openedRouter)
    //             {
    //                 Router t1 = routerMap.get(t);
    //                 if(t1.getState()==true)
    //                 {
    //                     RoutingTableEntry newEntry = new RoutingTableEntry(t,1,t);
    //                     routingTable.set(t-1, newEntry);
    //                 }
    //             }
    //             NetworkLayerServer.simpleDVR(currentRouter.getRouterId());
    //         }
    //         RoutingTableEntry nextHopEntry= routingTable.get(receiverRouterID-1);
    //         int nextHopRouterID = nextHopEntry.getGatewayRouterId();
    //         if( nextHopRouterID < 0)
    //         {
    //             return false;
    //         }
    //         Router nextRouter = routerMap.get(nextHopRouterID);
    //         if(nextRouter.getState() == false && nextHopEntry.getDistance() < Constants.INFINITY)
    //         {
    //             RouterStateChanger.islocked = true;
    //             if(nextRouter.getState()==false)
    //             {
    //                 for(int i=0;i<routerMap.size();i++)
    //                 {
    //                     if(routingTable.get(i).getGatewayRouterId() == nextHopRouterID)
    //                     {
    //                         RoutingTableEntry newEntry = new RoutingTableEntry(i,Constants.INFINITY,-5);
    //                         routingTable.set(i,newEntry);
    //                     }
    //                 }
    //                 NetworkLayerServer.simpleDVR(currentRouter.getRouterId());
    //             }
    //             return false;
    //         }
    //         currentRouter = nextRouter;
    //         p.hopcount++;

            

    //     }

    //     return true;
        
    //     /*
    //     1. Find the router s which has an interface
    //             such that the interface and source end device have same network address.
    //     2. Find the router d which has an interface
    //             such that the interface and destination end device have same network address.
    //     3. Implement forwarding, i.e., s forwards to its gateway router x considering d as the destination.
    //             similarly, x forwards to the next gateway router y considering d as the destination,
    //             and eventually the packet reaches to destination router d.

    //         3(a) If, while forwarding, any gateway x, found from routingTable of router r is in down state[x.state==FALSE]
    //                 (i) Drop packet
    //                 (ii) Update the entry with distance Constants.INFTY
    //                 (iii) Block NetworkLayerServer.stateChanger.t
    //                 (iv) Apply DVR starting from router r.
    //                 (v) Resume NetworkLayerServer.stateChanger.t

    //         3(b) If, while forwarding, a router x receives the packet from router y,
    //                 but routingTableEntry shows Constants.INFTY distance from x to y,
    //                 (i) Update the entry with distance 1
    //                 (ii) Block NetworkLayerServer.stateChanger.t
    //                 (iii) Apply DVR starting from router x.
    //                 (iv) Resume NetworkLayerServer.stateChanger.t

    //     4. If 3(a) occurs at any stage, packet will be dropped,
    //         otherwise successfully sent to the destination router
    //     */

    // }

    public Boolean deliverPacket(Packet p) 
    {
        Map<String, EndDevice> endDeviceMapWithString = NetworkLayerServer.endDeviceMapWithString;
        //Map<IPAddress, EndDevice> endDeviceMap = NetworkLayerServer.endDeviceMap;
        Map<Integer, Integer> deviceIDtoRouterID = NetworkLayerServer.deviceIDtoRouterID;
        Map<Integer, Router> routerMap = NetworkLayerServer.routerMap;
        
        EndDevice sender = endDeviceMapWithString.get(p.getSourceIP().getString());
        EndDevice receiver = endDeviceMapWithString.get(p.getDestinationIP().getString());

        int senderRouterID = deviceIDtoRouterID.get(sender.getDeviceID());
        int recieverRouterID = deviceIDtoRouterID.get(receiver.getDeviceID());

        Router senderRouter = routerMap.get(senderRouterID);
        Router receiverRouter = routerMap.get(recieverRouterID);

        if(senderRouter.getState()==false)
        {
            return false;
        }

        Router prevRouter = null;
        Router currentRouter = senderRouter;

        while(currentRouter!=receiverRouter)
        {
            if(p.hopcount >= Constants.INFINITY)
            {
                return false;
            }


            ArrayList<RoutingTableEntry> routingTable = currentRouter.getRoutingTable();
            
            if(prevRouter != null)
            {
                int prevRouterID = prevRouter.getRouterId();
                if(routingTable.get(prevRouterID-1).getDistance()>1 && prevRouter.getState()==true)
                {
                    routingTable.set(prevRouterID-1,new RoutingTableEntry(prevRouterID,1,prevRouterID));
                    NetworkLayerServer.simpleDVR(currentRouter.getRouterId());
                }
            }
            

            int nextHopRouterID = routingTable.get(recieverRouterID-1).getGatewayRouterId();
            if(nextHopRouterID == -5)
            {
                return false;
            }
            Router nextHopRouter = routerMap.get(nextHopRouterID);
            if(nextHopRouter.getState() == false)
            {
                routingTable.set(nextHopRouterID-1, new RoutingTableEntry(nextHopRouterID,Constants.INFINITY,-5));
                NetworkLayerServer.simpleDVR(currentRouter.getRouterId());
                return false;
            }
            


            p.hopcount++;
            prevRouter = currentRouter;
            currentRouter = nextHopRouter;
        }

        return true;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj); //To change body of generated methods, choose Tools | Templates.
    }
}
