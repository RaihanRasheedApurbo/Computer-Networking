//mainly testing how lock works on routerStateChanger 
public class test {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("hello world!");
        RouterStateChanger r = new RouterStateChanger();
        while(true)
        {
            System.out.println("sleeping");
            Thread.sleep(10000);
            System.out.println("sleeping completed now locking");
            RouterStateChanger.islocked = true;
            System.out.println("locked");
            Thread.sleep(10000);
            System.out.println("sleeping completed now unlocking");
            RouterStateChanger.islocked = false;
            synchronized (RouterStateChanger.msg){
                RouterStateChanger.msg.notify();
            }
            
            
            break;
        }
        System.out.println("outside while loop");
        try {
            r.thread.join();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("done!");

    }
}