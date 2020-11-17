import java.util.Random;
//testing behaviour of random function
public class test2 {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("hello world!");
        Random random = new Random(System.currentTimeMillis());
        for(int i=0;i<100;i++)
        {
            System.out.println(random.nextInt(11));
        }
    }

    
}
