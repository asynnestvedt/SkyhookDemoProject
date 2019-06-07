package us.alan.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import us.alan.util.SHXmlParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

public class Address {
    String streetNumber;
    String addressLine;
    String metro1;
    String metro2;
    String postalCode;
    String county;
    String state;
    String stateCode;
    String country;
    String countryCode;

    // TODO: move sample to a testing and/or data mock class
    public static String __sample__() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ReverseGeoRS xmlns=\"http://skyhookwireless.com/wps/2005\" version=\"2.26\"><street-address distanceToPoint=\"28.27771331\"><street-number>64</street-number><address-line>Farnsworth St</address-line><metro1>Boston</metro1><metro2>Boston</metro2><postal-code>02210</postal-code><county>Suffolk</county><state code=\"MA\">Massachusetts</state><country code=\"US\">United States</country></street-address></ReverseGeoRS>";
    }

    @Override
    public String toString() {
        return "" +
        "\nstreetNumber: "+streetNumber+
        "\naddressLine: "+addressLine+
        "\nmetro1: "+metro1+
        "\nmetro2: "+metro2+
        "\npostalCode: "+postalCode+
        "\ncounty: "+county+
        "\nstate: "+state+
        "\nstateCode: "+stateCode+
        "\ncountry: "+country+
        "\ncountryCode: "+countryCode;
    }

    // TODO: move parser into it's own class once we have more models. (some models may use multiple shared parsers)
    // TODO: swtch to an XML parser that is more memory efficient and handles streams... or one that is plays well with an existing http client
    public static Address addressFromXML(String xmlStr) throws SHXmlParseException {
        Address addr = new Address();

        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlStr)));

            doc.getDocumentElement().normalize();
            Element eAddr = (Element) doc
                    .getDocumentElement()
                    .getElementsByTagName("street-address")
                    .item(0);

            addr.streetNumber = eAddr.getElementsByTagName("street-number").item(0).getTextContent();
            addr.addressLine = eAddr.getElementsByTagName("address-line").item(0).getTextContent();
            Node cntry = eAddr.getElementsByTagName("country").item(0);
            addr.country = cntry.getTextContent();
            addr.countryCode = cntry.getAttributes().getNamedItem("code").getTextContent();
            addr.county = eAddr.getElementsByTagName("county").item(0).getTextContent();
            addr.metro1 = eAddr.getElementsByTagName("metro1").item(0).getTextContent();
            addr.metro2 = eAddr.getElementsByTagName("metro2").item(0).getTextContent();
            addr.postalCode = eAddr.getElementsByTagName("postal-code").item(0).getTextContent();
            Node state = eAddr.getElementsByTagName("state").item(0);
            addr.state = state.getTextContent();
            addr.stateCode = state.getAttributes().getNamedItem("code").getTextContent();

        } catch (Exception e) {
            e.printStackTrace();
            throw new SHXmlParseException(xmlStr, e);
        }

        return addr;
    }
}