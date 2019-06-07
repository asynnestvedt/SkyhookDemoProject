package us.alan.client;


import us.alan.model.Address;
import us.alan.model.Auth;
import us.alan.model.Point;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

// TODO: instead of passing around a threadpool, we should have an httpclient pool like PoolingClientConnectionManager
// TODO: block methods that require auth object when one is not provided

public class SHAsyncHttpClient {

    private ReverseGeoCache cache = null;
    private Auth _auth = null;
    private ExecutorService executor = null;

    public SHAsyncHttpClient(int maxThreads, ReverseGeoCache cache) {
        this(maxThreads);
        this.cache = cache;
    }
    public SHAsyncHttpClient(int maxThreads) {
        this.executor = Executors.newFixedThreadPool(maxThreads);
    }
    public SHAsyncHttpClient() {
        this(Config.DEFAULT_MAX_THREADS);
    }


    public void done() {
        executor.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!executor.awaitTermination(Config.THREADPOOL_SHUTDOWN_WAIT_SEC, TimeUnit.SECONDS)) {
                executor.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!executor.awaitTermination(Config.THREADPOOL_SHUTDOWN_WAIT_SEC, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    /**
     * shared mutable data methods
     */
    synchronized public void setAuth(Auth auth) {
        this._auth = auth;
    }

    synchronized public Auth getAuth() {
        return _auth;
    }


    public CompletableFuture<Address> reverseGeo(Point point) {

        Address result;

        /**
         * 1. check cache
         */
        if (this.cache != null) {
            System.out.println("checking cache");
            result = this.cache.get(point, 200);
            if (result != null) {
                System.out.println("found in cache");
                return CompletableFuture.completedFuture(result);
            }
        }

        /**
         * 2. no cache or cache miss, hit remote api
         */
        return SHHttpRequestReverseGeo.Post(
                Config.BASE_API_URL,
                this.getAuth(),
                point,
                cache,
                this.executor);
    }

}
