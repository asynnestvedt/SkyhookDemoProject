package us.alan.client;


import org.w3c.dom.Document;
import us.alan.model.Address;
import us.alan.model.Auth;
import us.alan.model.Point;
import us.alan.util.XmlUtil;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class SHHttpRequestReverseGeo {

    public static CompletableFuture<Address> Post(String baseUrl, Auth auth, Point point, ReverseGeoCache cache, Executor executor) {

        CompletableFuture<Address> future = CompletableFuture.supplyAsync(() -> {
            try {
                Document body = XmlUtil.createReverseGeoRequestBody(
                        auth.key,
                        auth.username,
                        point.latitude,
                        point.longitude);

                String bodyStr = XmlUtil.convertDocumentToString(body);

                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(baseUrl + "/reverse-geo"))
                        .header("Content-Type", "text/xml")
                        .POST(HttpRequest.BodyPublishers.ofString(bodyStr))
                        .build();

                System.out.println(String.format("Reverse geo request initiated for (%.6f, %.6f)",point.latitude,point.longitude));
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                int code = response.statusCode();

                if (code < 200 || code >= 300) {
                    System.out.println(String.format("HTTP error code (%d) for request with coords (%.6f, %.6f)",response.statusCode(),point.latitude,point.longitude));
                    // TODO: throw exception instead
                    return null;
                }

                Address address = Address.addressFromXML(response.body());

                if (cache != null) {
                    System.out.println(String.format("caching result for coords (%.6f, %.6f)",point.latitude,point.longitude));
                    cache.put(point, address);
                }


                return address;

            } catch (Exception e) {
//                e.printStackTrace();
                return null;
//                throw new SHHttpException("ReverseGeo http request failed", e);
            }
        }, executor);


        return future;
    }


}
