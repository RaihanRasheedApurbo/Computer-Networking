//Work needed
import java.util.ArrayList;
//import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Router {
    private int routerId;
    private int numberOfInterfaces;
    private ArrayList<IPAddress> interfaceAddresses;//list of IP address of all interfaces of the router
    private ArrayList<RoutingTableEntry> routingTable;//used to implement DVR
    private ArrayList<Integer> neighborRouterIDs;//Contains both "UP" and "DOWN" state routers
    private Boolean state;//true represents "UP" state and false is for "DOWN" state
    private Map<Integer, IPAddress> gatewayIDtoIP;
    public Router() {
        interfaceAddresses = new ArrayList<>();
        routingTable = new ArrayList<>();
        neighborRouterIDs = new ArrayList<>();

        /**
         * 80% Probability that the router is up
         */
        Random random = new Random();
        double p = random.nextDouble();
        if(p < 0.80) state = true;
        else state = false;

        numberOfInterfaces = 0;
    }

    public Router(int routerId, ArrayList<Integer> neighborRouters, ArrayList<IPAddress> interfaceAddresses, Map<Integer, IPAddress> gatewayIDtoIP) {
        this.routerId = routerId;
        this.interfaceAddresses = interfaceAddresses;
        this.neighborRouterIDs = neighborRouters;
        this.gatewayIDtoIP = gatewayIDtoIP;
        routingTable = new ArrayList<>();



        /**
         * 80% Probability that the router is up
         */
        Random random = new Random();
        double p = random.nextDouble();
        if(p < 0.80) state = true;
        else state = false;

        numberOfInterfaces = interfaceAddresses.size();
    }

    @Override
    public String toString() {
        String string = "";
        string += "Router ID: " + routerId + "\n" + "Interfaces: \n";
        for (int i = 0; i < numberOfInterfaces; i++) {
            string += interfaceAddresses.get(i).getString() + "\t";
        }
        string += "\n" + "Neighbors: \n";
        for(int i = 0; i < neighborRouterIDs.size(); i++) {
            string += neighborRouterIDs.get(i) + "\t";
        }
        return string;
    }



    /**
     * Initialize the distance(hop count) for each router.
     * for itself, distance=0; for any connected router with state=true, distance=1; otherwise distance=Constants.INFTY;
     */
    public void initiateRoutingTable() {
    //     private int routerId;
    // private int numberOfInterfaces;
    // private ArrayList<IPAddress> interfaceAddresses;//list of IP address of all interfaces of the router
    // private ArrayList<RoutingTableEntry> routingTable;//used to implement DVR
    // private ArrayList<Integer> neighborRouterIDs;//Contains both "UP" and "DOWN" state routers
    // private Boolean state;//true represents "UP" state and false is for "DOWN" state
    // private Map<Integer, IPAddress> gatewayIDtoIP;
        ArrayList<Router> routers = NetworkLayerServer.routers;
        int totalRouterCount = routers.size();
        for(int i=1;i<=totalRouterCount;i++)
        {   

            if(i==this.routerId)
            {
                RoutingTableEntry temp = new RoutingTableEntry(i, 0, 0);
                this.routingTable.add(temp);
            }
            else
            {
                RoutingTableEntry temp = new RoutingTableEntry(i, Constants.INFINITY, -5);
                this.routingTable.add(temp);
            }
        }
        Map<Integer, Router> routerMap = NetworkLayerServer.routerMap;
        for(int id: this.neighborRouterIDs)
        {
            if(routerMap.get(id).state==true)
            {
                this.routingTable.get(id-1).setDistance(1);
                this.routingTable.get(id-1).setGatewayRouterId(id);;
                
            }
        }
        
    }

    /**
     * Delete all the routingTableEntry
     */
    public void clearRoutingTable() {
        routingTable.clear();

    }

    /**
     * Update the routing table for this router using the entries of Router neighbor
     * @param neighbor
     */
    public boolean updateRoutingTable(Router neighbor) {
        boolean isNeighbor = false; // check whether neighbor is truly neighbor
        boolean change = false; // will return this value
        for(int i:this.neighborRouterIDs)
        {
            if(i==neighbor.routerId)
            {
                isNeighbor = true;
                break;
            }
        }
        
        
        if(isNeighbor==false)
        {
            return change;
        }
        
        if(neighbor.state==false)
        {
            this.routingTable.get(neighbor.routerId-1).setDistance(Constants.INFINITY);
            this.routingTable.get(neighbor.routerId-1).setGatewayRouterId(-5);
            for(RoutingTableEntry r: this.routingTable)
            {
                if(r.getGatewayRouterId()==neighbor.routerId)
                {
                    r.setDistance(Constants.INFINITY);
                    r.setGatewayRouterId(-5);
                }
            }
            change = true;
        }
        else
        {
            for(int i=0;i<this.routingTable.size();i++)
            {
                RoutingTableEntry thisRow = this.routingTable.get(i);
                double dxy = thisRow.getDistance();
                double dyz = neighbor.routingTable.get(i).getDistance();
                double dxz = 1;
                if(dyz+dxz<dxy)
                {
                    thisRow.setDistance(dyz+dxz);
                    thisRow.setGatewayRouterId(neighbor.routerId);
                    change = true;
                }
            }

        }
        return change;
    }

    public boolean sfupdateRoutingTable(Router neighbor) {
        return true;
    }

    /**
     * If the state was up, down it; if state was down, up it
     */
    public void revertState() {
        state = !state;
        if(state) { initiateRoutingTable(); }
        else { clearRoutingTable(); }
    }

    public int getRouterId() {
        return routerId;
    }

    public void setRouterId(int routerId) {
        this.routerId = routerId;
    }

    public int getNumberOfInterfaces() {
        return numberOfInterfaces;
    }

    public void setNumberOfInterfaces(int numberOfInterfaces) {
        this.numberOfInterfaces = numberOfInterfaces;
    }

    public ArrayList<IPAddress> getInterfaceAddresses() {
        return interfaceAddresses;
    }

    public void setInterfaceAddresses(ArrayList<IPAddress> interfaceAddresses) {
        this.interfaceAddresses = interfaceAddresses;
        numberOfInterfaces = interfaceAddresses.size();
    }

    public ArrayList<RoutingTableEntry> getRoutingTable() {
        return routingTable;
    }

    public void addRoutingTableEntry(RoutingTableEntry entry) {
        this.routingTable.add(entry);
    }

    public ArrayList<Integer> getNeighborRouterIDs() {
        return neighborRouterIDs;
    }

    public void setNeighborRouterIDs(ArrayList<Integer> neighborRouterIDs) { this.neighborRouterIDs = neighborRouterIDs; }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public Map<Integer, IPAddress> getGatewayIDtoIP() { return gatewayIDtoIP; }

    public void printRoutingTable() {
        System.out.println("Router " + routerId);
        System.out.println("DestID Distance Nexthop");
        for (RoutingTableEntry routingTableEntry : routingTable) {
            System.out.println(routingTableEntry.getRouterId() + " " + routingTableEntry.getDistance() + " " + routingTableEntry.getGatewayRouterId());
        }
        System.out.println("-----------------------");
    }
    
    public String strRoutingTable() {
        String string = "Router" + routerId + "\n";
        string += "DestID Distance Nexthop\n";
        for (RoutingTableEntry routingTableEntry : routingTable) {
            string += routingTableEntry.getRouterId() + " " + routingTableEntry.getDistance() + " " + routingTableEntry.getGatewayRouterId() + "\n";
        }

        string += "-----------------------\n";
        return string;
    }

}
