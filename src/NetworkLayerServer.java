

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;



//Work needed
public class NetworkLayerServer {

    static int clientCount = 0;
    static ArrayList<Router> routers = new ArrayList<>();
    static RouterStateChanger stateChanger = null;
    static Map<IPAddress,Integer> clientInterfaces = new HashMap<>(); //Each map entry represents number of client end devices connected to the interface
    static Map<IPAddress, EndDevice> endDeviceMap = new HashMap<>();
    static Map<String, EndDevice> endDeviceMapWithString = new HashMap<>();

    static ArrayList<EndDevice> endDevices = new ArrayList<>();
    static Map<Integer, Integer> deviceIDtoRouterID = new HashMap<>();
    static Map<IPAddress, Integer> interfacetoRouterID = new HashMap<>();
    static Map<Integer, Router> routerMap = new HashMap<>();
    static boolean complexDVR = false;
    static int numberOfPacket = 0;
    static int totalHop = 0;
    static int packetDrop = 0;

    public static void main(String[] args) {

        //Task: Maintain an active client list

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(4444);
        } catch (IOException ex) {
            Logger.getLogger(NetworkLayerServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Server Ready: " + serverSocket.getInetAddress().getHostAddress());
        System.out.println("Creating router topology");

        readTopology();
        //System.out.println("topology reading finished");
        //printRouters();
        //System.out.println("finished printing");

        initRoutingTables(); //Initialize routing tables for all routers

        //DVR(1); //Update routing table using distance vector routing until convergence
        // for(Router r: routers)
        // {
        //     System.out.println("routerID: "+r.getRouterId()+" routerState: "+r.getState());
        // }
        if(complexDVR)
        {
            DVR(1);
        }
        else
        {
            simpleDVR(1);
        }
        
        for (Router router : routers) {
            //router.initiateRoutingTable();
            System.out.println("Router Status: "+router.getState());
            router.printRoutingTable();
            
        }
        
        stateChanger = new RouterStateChanger();//Starts a new thread which turns on/off routers randomly depending on parameter Constants.LAMBDA

        while(true) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("Client" + (clientCount + 1) + " attempted to connect");
                
                EndDevice endDevice = getClientDeviceSetup();
                clientCount++;

                endDevices.add(endDevice);
                endDeviceMap.put(endDevice.getIpAddress(),endDevice);
                endDeviceMapWithString.put(endDevice.getIpAddress().getString(),endDevice);
                new ServerThread(new NetworkUtility(socket), endDevice);
            } catch (IOException ex) {
                Logger.getLogger(NetworkLayerServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void initRoutingTables() {
        for (Router router : routers) {
            router.initiateRoutingTable();
            //router.printRoutingTable();
        }
    }

    public static synchronized void DVR(int startingRouterId) {
        /**
         * pseudocode
         */

        /*
        while(convergence)
        {
            //convergence means no change in any routingTable before and after executing the following for loop
            for each router r <starting from the router with routerId = startingRouterId, in any order>
            {
                1. T <- getRoutingTable of the router r
                2. N <- find routers which are the active neighbors of the current router r
                3. Update routingTable of each router t in N using the
                   routing table of r [Hint: Use t.updateRoutingTable(r)]
            }
        }
        */
        RouterStateChanger.islocked = true;
        Router firstRouter = routers.get(startingRouterId-1);
        /// if first router is off then we have to 
        /// find another router from array of routers that is active 
        for(Router r: routers)   
        {
            if(firstRouter.getState()==true)
            {
                break;
            }
            if(r.getState()==true)
            {
                firstRouter = r;
            }
        }
        if(firstRouter.getState()==false)
        {
            RouterStateChanger.islocked = false;
            synchronized(RouterStateChanger.msg)
            {
                RouterStateChanger.msg.notify();
            }
            return;
        }
        
        boolean convergence = false;
        //System.out.println("inside convergence loop!");
        while(convergence==false)
        {
            convergence = true;
            boolean visited[] = new boolean[routers.size()];
            for(int i=0;i<visited.length;i++)
            {
                visited[i] = false;
            }
            
            visited[startingRouterId-1] = true;
            Queue<Router> q = new LinkedList<>(); 
            q.add(firstRouter);
            // for (Router router : routers) {
            //     //router.initiateRoutingTable();
            //     System.out.println("Router Status: "+router.getState());
            //     router.printRoutingTable();
                
            // }
            while(!q.isEmpty())
            {
                Router currentRouter = q.remove();
                //System.out.println("currently in "+currentRouter.getRouterId());
                // if(currentRouter.getRouterId()==3)
                // {
                //     for(int t: currentRouter.getNeighborRouterIDs())
                //     {
                //         System.out.println(t);
                //     }
                // }
                for(int t: currentRouter.getNeighborRouterIDs())
                {
                    Router neighborRouter = routers.get(t-1);
                    if(neighborRouter.getState()==false)
                    {
                        //System.out.println("this is off "+t);
                        continue;
                    }
                    boolean changed = neighborRouter.sfupdateRoutingTable(currentRouter);
                    if(changed == true)
                    {
                        convergence = false;
                    }
                    if(visited[t-1]==false)
                    {
                        //System.out.println("putting router no. "+t);
                        q.add(neighborRouter);
                        visited[t-1] = true;
                    }
                    
                    
                    

                }
            }
        }
        //System.out.println("outside convergence loop!");
        RouterStateChanger.islocked = false;
        synchronized(RouterStateChanger.msg)
        {
            RouterStateChanger.msg.notify();
        }
    }

    public static synchronized void simpleDVR(int startingRouterId) {
        RouterStateChanger.islocked = true;
        Router firstRouter = routers.get(startingRouterId-1);
        /// if first router is off then we have to 
        /// find another router from array of routers that is active 
        for(Router r: routers)   
        {
            if(firstRouter.getState()==true)
            {
                break;
            }
            if(r.getState()==true)
            {
                firstRouter = r;
            }
        }
        if(firstRouter.getState()==false)
        {
            RouterStateChanger.islocked = false;
            synchronized(RouterStateChanger.msg)
            {
                RouterStateChanger.msg.notify();
            }
            return;
        }
        
        boolean convergence = false;
        //System.out.println("inside convergence loop!");
        while(convergence==false)
        {
            convergence = true;
            boolean visited[] = new boolean[routers.size()];
            for(int i=0;i<visited.length;i++)
            {
                visited[i] = false;
            }
            
            visited[startingRouterId-1] = true;
            Queue<Router> q = new LinkedList<>(); 
            q.add(firstRouter);
            
            while(!q.isEmpty())
            {
                Router currentRouter = q.remove();
                //System.out.println("currently in "+currentRouter.getRouterId());
                // if(currentRouter.getRouterId()==3)
                // {
                //     for(int t: currentRouter.getNeighborRouterIDs())
                //     {
                //         System.out.println(t);
                //     }
                // }
                for(int t: currentRouter.getNeighborRouterIDs())
                {
                    Router neighborRouter = routers.get(t-1);
                    if(neighborRouter.getState()==false)
                    {
                        //System.out.println("this is off "+t);
                        continue;
                    }
                    boolean changed = neighborRouter.updateRoutingTable(currentRouter);
                    if(changed == true)
                    {
                        convergence = false;
                    }
                    if(visited[t-1]==false)
                    {
                        //System.out.println("putting router no. "+t);
                        q.add(neighborRouter);
                        visited[t-1] = true;
                    }
                    
                    
                    

                }
            }
        }
        //System.out.println("outside convergence loop!");
        RouterStateChanger.islocked = false;
        synchronized(RouterStateChanger.msg)
        {
            RouterStateChanger.msg.notify();
        }
        

    }

    public static EndDevice getClientDeviceSetup() {
        Random random = new Random(System.currentTimeMillis());
        int r = Math.abs(random.nextInt(clientInterfaces.size()));

        System.out.println("Size: " + clientInterfaces.size() + "\n" + r);

        IPAddress ip = null;
        IPAddress gateway = null;

        int i = 0;
        for (Map.Entry<IPAddress, Integer> entry : clientInterfaces.entrySet()) {
            IPAddress key = entry.getKey();
            Integer value = entry.getValue();
            if(i == r) {
                gateway = key;
                ip = new IPAddress(gateway.getBytes()[0] + "." + gateway.getBytes()[1] + "." + gateway.getBytes()[2] + "." + (value+2));
                value++;
                clientInterfaces.put(key, value);
                deviceIDtoRouterID.put(endDevices.size()+1, interfacetoRouterID.get(key));
                break;
            }
            i++;
        }

        EndDevice device = new EndDevice(ip, gateway, endDevices.size()+1);

        System.out.println("Device : " + ip + ":::: Gateway:" + gateway);
        return device;
    }

    public static void printRouters() {
        for(int i = 0; i < routers.size(); i++) {
            System.out.println("------------------\n" + routers.get(i));
        }
    }

    public static String strrouters() {
        String string = "";
        for (int i = 0; i < routers.size(); i++) {
            string += "\n------------------\n" + routers.get(i).strRoutingTable();
        }
        string += "\n\n";
        return string;
    }

    public static void readTopology() {
        Scanner inputFile = null;
        try {
            inputFile = new Scanner(new File("topology.txt"));
            //skip first 27 lines
            int skipLines = 27;
            for(int i = 0; i < skipLines; i++) {
                inputFile.nextLine();
            }

            //start reading contents
            while(inputFile.hasNext()) {
                inputFile.nextLine();
                int routerId;
                ArrayList<Integer> neighborRouters = new ArrayList<>();
                ArrayList<IPAddress> interfaceAddrs = new ArrayList<>();
                Map<Integer, IPAddress> interfaceIDtoIP = new HashMap<>();

                routerId = inputFile.nextInt();

                int count = inputFile.nextInt();
                for(int i = 0; i < count; i++) {
                    neighborRouters.add(inputFile.nextInt());
                }
                count = inputFile.nextInt();
                inputFile.nextLine();

                for(int i = 0; i < count; i++) {
                    String string = inputFile.nextLine();
                    IPAddress ipAddress = new IPAddress(string);
                    interfaceAddrs.add(ipAddress);
                    interfacetoRouterID.put(ipAddress, routerId);

                    /**
                     * First interface is always client interface
                     */
                    if(i == 0) {
                        //client interface is not connected to any end device yet
                        clientInterfaces.put(ipAddress, 0);
                    }
                    else {
                        interfaceIDtoIP.put(neighborRouters.get(i - 1), ipAddress);
                    }
                }
                Router router = new Router(routerId, neighborRouters, interfaceAddrs, interfaceIDtoIP);
                routers.add(router);
                routerMap.put(routerId, router);
            }


        } catch (FileNotFoundException ex) {
            Logger.getLogger(NetworkLayerServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
