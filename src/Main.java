import us.alan.client.MemoryReverseGeoCache;
import us.alan.client.SHAsyncHttpClient;
import us.alan.model.Address;
import us.alan.model.Auth;
import us.alan.model.Point;

import java.util.concurrent.CompletableFuture;

public class Main {

    public static void main(String[] args) {

        /**
         * create client w/ cache & thread limit
         */
        SHAsyncHttpClient asyncClient =
                new SHAsyncHttpClient(2, new MemoryReverseGeoCache());
        /**
         * set http auth info
         */
        Auth auth = new Auth (
                "eJwVwUsKACAIBcB1hxH88LS2il0quns0I0P4g08bx3gVHEWFDZJIUKYLmXrAWoub7wMPMgr_",
                "interview-candidate");
        asyncClient.setAuth(auth);

        /**
         * do some queries
         */
        Point query1 = new Point(42.352016, -71.048387);
        Point query2 = new Point(40.109121, -75.213252);
        Point query3 = new Point(42.352000, -71.048397);


        class fetch implements Runnable{
            private Point point;

            public fetch(Point point) {
                this.point = point;
            }
            public void run() {
                try {
                    CompletableFuture address = asyncClient.reverseGeo(this.point);
                    Address addr = (Address) address.get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        Thread t1 = new Thread(new fetch(query1));
        Thread t2 = new Thread(new fetch(query2));
        Thread t3 = new Thread(new fetch(query3));

        t1.start();
        t2.start();

        /**
         * wait for cache to fill before trying a nearby query
         */
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t3.start();


        /**
         * shutdown threadpool
         * TODO: could keep track of threads in the client and shutdown pool automatically
         */
        asyncClient.done();
    }


}
