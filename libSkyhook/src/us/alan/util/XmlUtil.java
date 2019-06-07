package us.alan.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;


public class XmlUtil {
    public static Document createReverseGeoRequestBody(String key, String username, double lat, double lng) {
        DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder icBuilder;
        try {
            icBuilder = icFactory.newDocumentBuilder();
            Document doc = icBuilder.newDocument();
            doc.setXmlStandalone(true);
            Element mainRootElement = doc.createElementNS("http://skyhookwireless.com/wps/2005", "ReverseGeoRQ");
            mainRootElement.setAttribute("version","2.26");
            mainRootElement.setAttribute("street-address-lookup", "full");
            doc.appendChild(mainRootElement);
            mainRootElement.appendChild(getAuthXml(doc, key, username));
            mainRootElement.appendChild(getCoordsXML(doc, lat, lng));

            return doc;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Node getAuthXml(Document doc, String key, String username) {
        final String authVer = "2.2";

        Element eKey = doc.createElement("key");
        eKey.setAttribute("key", key);
        eKey.setAttribute("username", username);

        Element eAuth = doc.createElement("authentication");
        eAuth.setAttribute("version", authVer);
        eAuth.appendChild(eKey);

        return eAuth;
    }

    public static Node getCoordsXML(Document doc, double lat, double lng) {
        Element ePoint = doc.createElement("point");
        Element eLat = doc.createElement("latitude");
        Element eLng = doc.createElement("longitude");
        eLat.appendChild(doc.createTextNode(String.format("%.6f", lat)));
        eLng.appendChild(doc.createTextNode(String.format("%.6f", lng)));

        ePoint.appendChild(eLat);
        ePoint.appendChild(eLng);

        return ePoint;
    }

    public static String convertDocumentToString(Document doc) {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = tf.newTransformer();
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            return writer.getBuffer().toString();
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        return null;
    }
}
