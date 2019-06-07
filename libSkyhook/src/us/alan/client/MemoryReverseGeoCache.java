package us.alan.client;

import us.alan.model.Address;
import us.alan.model.Point;
import us.alan.util.GeoUtil;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;


/**
 * let's split our cache into 4 hemisheres. we could use more granular tiles to improve performance but this is just for demo.
 *
 * TODO: if we sized our tiles to be 200% of our search radius we could make our
 * maximum search index = 4 tiles (maps) and avoid false cache misses at
 * tile boundaries.
 *
 * TODO: a much improved solution would use some sort of geocoding standard
 */

public class MemoryReverseGeoCache implements ReverseGeoCache {

    private ConcurrentHashMap nw = new ConcurrentHashMap();
    private ConcurrentHashMap ne = new ConcurrentHashMap();
    private ConcurrentHashMap sw = new ConcurrentHashMap();
    private ConcurrentHashMap se = new ConcurrentHashMap();

    public MemoryReverseGeoCache() {}

    @Override
    public Address get(Point point, double dist) {
        ConcurrentHashMap cache = getMapByHemisphere(point);

        double min = dist+1;
        Address cachedAddress = null;

        /**
         * check for exact location
         */
        cachedAddress = (Address) cache.get(makeKey(point));

        /**
         * check for closest location in search range
         */
        if (cachedAddress == null) {
            Iterator i = cache.entrySet().iterator();
            while(i.hasNext()) {
                ConcurrentHashMap.Entry<String, Address> pair = (ConcurrentHashMap.Entry<String, Address>) i.next();
                String[] key = pair.getKey().split("_");
                Address testAddr = pair.getValue();
                double testDist = GeoUtil.distance(point, new Point(Double.parseDouble(key[0]), Double.parseDouble(key[1])));
                if (testDist <= dist && testDist < min) {
                    cachedAddress = testAddr;
                }
            }
        }

        return cachedAddress;
    }

    @Override
    public void put(Point point, Address address) {
        ConcurrentHashMap cache = getMapByHemisphere(point);
        cache.putIfAbsent(makeKey(point), address);
    }

    private static String makeKey(Point point) {
        return String.format ("%.6f_%.6f", point.latitude, point.longitude);
    }

    private ConcurrentHashMap getMapByHemisphere(Point point) {
        if (point.latitude <= 0) {
            return point.longitude <= 0 ? this.sw : this.se;
        }

        return point.longitude <= 0 ? this.nw : this.ne;
    }


}
