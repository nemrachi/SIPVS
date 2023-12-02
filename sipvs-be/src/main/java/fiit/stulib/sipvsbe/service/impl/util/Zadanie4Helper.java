package fiit.stulib.sipvsbe.service.impl.util;

import fiit.stulib.sipvsbe.service.exception.Zadanie4CustomException;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.jce.provider.X509CertificateObject;
import org.bouncycastle.util.encoders.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;

public class Zadanie4Helper {

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

    public static String fromElementToString(Element element) throws TransformerException {
        StreamResult result = new StreamResult(new StringWriter());
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(new DOMSource(element), result);
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        return result.getWriter().toString();
    }

    public static Element findByAttributeValue(Document document, String elementType, String attributeName, String attributeValue) {
        NodeList elements = document.getElementsByTagName(elementType);
        for (int i = 0; i < elements.getLength(); i++) {
            Element element = (Element) elements.item(i);
            if (element.hasAttribute(attributeName) && element.getAttribute(attributeName).equals(attributeValue)) {
                return element;
            }
        }
        return null;
    }

    public static X509CertificateObject getCertificate(Document document) throws XPathExpressionException, Zadanie4CustomException {

        Element keyInfoElement = (Element) document.getElementsByTagName("ds:KeyInfo").item(0);

        if (keyInfoElement == null) {
            throw new Zadanie4CustomException("Chyba pri ziskavani certifikatu: Dokument neobsahuje element ds:KeyInfo");
        }

        Element x509DataElement = (Element) keyInfoElement.getElementsByTagName("ds:X509Data").item(0);

        if (x509DataElement == null) {
            throw new Zadanie4CustomException("Chyba pri ziskavani certifikatu: Dokument neobsahuje element ds:X509Data");
        }

        Element x509Certificate = (Element) x509DataElement.getElementsByTagName("ds:X509Certificate").item(0);

        if (x509Certificate == null) {
            throw new Zadanie4CustomException("Chyba pri ziskavani certifikatu: Dokument neobsahuje element ds:X509Certificate");
        }

        X509CertificateObject certObject = null;
        ASN1InputStream inputStream = null;

        try {
            inputStream = new ASN1InputStream(new ByteArrayInputStream(Base64.decode(x509Certificate.getTextContent())));
            ASN1Sequence sequence = (ASN1Sequence) inputStream.readObject();
            certObject = new X509CertificateObject(Certificate.getInstance(sequence));

        } catch (IOException | java.security.cert.CertificateParsingException e) {

            throw new Zadanie4CustomException("Certifikát nebolo možné načítať", e);

        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        }

        return certObject;
    }


}
