package us.alan.client;

public class Config {

    /**
     * Web API URL
     */
    public static final String BASE_API_URL = "https://api.skyhookwireless.com/wps2";
//    public static final String BASE_API_URL = "http://127.0.0.1:8000";

    /**
     * default # of threads if user doesn't override
     */
    public static final int DEFAULT_MAX_THREADS = 5;

    /**
     * set graceful threadpool shutdown wait period
     */
    public static final int THREADPOOL_SHUTDOWN_WAIT_SEC = 4;
}
