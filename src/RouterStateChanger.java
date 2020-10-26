//Done!
import java.util.Random;

public class RouterStateChanger implements Runnable {

    public Thread thread = null;
    public static boolean islocked = false;
    public static Boolean msg = true;

    public RouterStateChanger() {
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        Random random = new Random(System.currentTimeMillis());
        while (true) {
            if (islocked) {
                try {
                    System.out.println("inside try catch!");
                    synchronized (msg) {
                        msg.wait();
                    }
                    System.out.println("outside try catch!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            double d = random.nextDouble();
            //System.out.println(d);
            if (d < Constants.LAMBDA) {
                revertRandomRouter();
                //System.out.println("kill meh please "+d);
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void revertRandomRouter() {
        /**
         * Randomly select a router and revert its state
         */
        Random random = new Random(System.currentTimeMillis());
        int id = random.nextInt(NetworkLayerServer.routers.size());
        NetworkLayerServer.routers.get(id).revertState();
        
        System.out.println("State Changed; Router ID: "+NetworkLayerServer.routers.get(id).getRouterId());
        System.out.println("Currently turned off routers are: ");
        for(Router r: NetworkLayerServer.routers)
        {
            if(r.getState()==false)
            System.out.print(r.getRouterId()+" ");
        }
        System.out.println("\n");
    }
}
