package us.alan.model;

public class Auth {
    public String key;
    public String username;

    /**
     * required for setting auth in client
     * @param key
     * @param username
     */
    public Auth(String key, String username) {
        this.key = key;
        this.username = username;
    }
}
