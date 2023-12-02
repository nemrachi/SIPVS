package fiit.stulib.sipvsbe.service.impl.util;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class VerificationHelper {

    public static boolean checkNamespace(Element rootElement) {
        final String xzep = "xmlns:xzep";
        final String ds = "xmlns:ds";
        final String xzepNs = "http://www.ditec.sk/ep/signature_formats/xades_zep/v1.0";
        final String dsNs = "http://www.w3.org/2000/09/xmldsig#";

        boolean hasCorrectAttributes = rootElement.hasAttribute(xzep) &&
                rootElement.hasAttribute(ds);

        boolean hasCorrectNamespaces = xzepNs.equals(rootElement.getAttribute(xzep)) &&
                dsNs.equals(rootElement.getAttribute(ds));

        return hasCorrectAttributes && hasCorrectNamespaces;
    }

    public static String getAttributeValue(Element parentElement, String nodeName, String attributeName) {
        NodeList childNodes = parentElement.getChildNodes();
        String out = "";

        for (int i = 0; i < childNodes.getLength(); i++) {

            if (childNodes.item(i) instanceof Element) {
                Element childElement = (Element) childNodes.item(i);

                if (childElement.getNodeName().equals(nodeName)) {
                    return childElement.getAttribute(attributeName);
                }

                if (childElement.hasChildNodes()) {
                    out = out.concat(getAttributeValue(childElement, nodeName, attributeName));
                }
            }
        }

        return out;
    }


}
