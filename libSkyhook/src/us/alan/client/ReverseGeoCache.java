package us.alan.client;

import us.alan.model.Address;
import us.alan.model.Point;

// TODO: cache limiting, item expiration and cache wiping.
public interface ReverseGeoCache {
    /**
     * returns the closest address within the search radius
     * @param point - lat/long coords
     * @param dist - search radius in meters
     * @return Address - address object that was closest and within search radius
     */
    public Address get(Point point, double dist);

    /**
     * store address by lat/long coords
     * @param point - lat/long coords
     * @param address - address object to store
     */
    public void put(Point point, Address address);

//    public Object put(double lat, double lng, Address address,  int expEpoch);
//    public void setMaxCacheSize(int size);
//    public void wipeAll();
//    public void expireEntry(String method, String url, String data);
}
