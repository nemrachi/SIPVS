package fiit.stulib.sipvsbe.service.impl.util;

import fiit.stulib.sipvsbe.service.exception.Zadanie4CustomException;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.cert.X509CRLHolder;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CRLConverter;
import org.bouncycastle.jce.provider.X509CertificateObject;
import org.bouncycastle.tsp.TimeStampToken;
import org.bouncycastle.util.Store;
import org.bouncycastle.util.encoders.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.security.MessageDigest;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Zadanie4Helper {

    public static String fromElementToString(Element element) throws TransformerException {
        StringWriter stringWriter = new StringWriter();
        StreamResult result = new StreamResult(stringWriter);
        Transformer transformer = TransformerFactory.newInstance().newTransformer();

        transformer.transform(new DOMSource(element), result);
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

        return stringWriter.toString();
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

    public static String getNodeValue(Element parentElement, String nodeName) {
        NodeList childNodes = parentElement.getChildNodes();
        String out = "";
        for (int i = 0; i < childNodes.getLength(); i++) {
            if (childNodes.item(i) instanceof Element) {
                Element childElement = (Element) childNodes.item(i);
                if (childElement.getNodeName().equals(nodeName)) {
                    Node dataNode = childElement.getFirstChild();
                    return dataNode.getTextContent();
                }
                if (childElement.hasChildNodes()) {
                    out = out.concat(getNodeValue(childElement, nodeName));
                }
            }
        }
        return out;
    }

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

    public static X509CertificateObject getCertificate(Document document) throws Exception {

        Element keyInfoElement = (Element) document.getElementsByTagName("ds:KeyInfo").item(0);

        if (keyInfoElement == null) {
            throw new Zadanie4CustomException("[CERTIFICATE-ERROR]: Dokument musi obashovat element ds:KeyInfo");
        }

        Element x509DataElement = (Element) keyInfoElement.getElementsByTagName("ds:X509Data").item(0);

        if (x509DataElement == null) {
            throw new Zadanie4CustomException("[CERTIFICATE-ERROR]: Dokument musi obashovat element ds:X509Data");
        }

        Element x509Certificate = (Element) x509DataElement.getElementsByTagName("ds:X509Certificate").item(0);

        if (x509Certificate == null) {
            throw new Zadanie4CustomException("[CERTIFICATE-ERROR]: Dokument musi obashovat element ds:X509Certificate");
        }

        X509CertificateObject certObject;
        ASN1InputStream inputStream = null;

        try {
            inputStream = new ASN1InputStream(new ByteArrayInputStream(Base64.decode(x509Certificate.getTextContent())));
            ASN1Sequence sequence = (ASN1Sequence) inputStream.readObject();
            certObject = new X509CertificateObject(Certificate.getInstance(sequence));
        } catch (Exception e) {
            throw new Exception("[CERTIFICATE-ERROR]: certifikát nebolo možné načítať", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    throw new Exception(e.getMessage());
                }
            }
        }

        return certObject;
    }

    public static X509CertificateHolder getTimeStampSignatureCertificate(byte[] tsResponse) throws Exception {
        try {
            TimeStampToken token = new TimeStampToken(
                    new org.bouncycastle.cms.CMSSignedData(tsResponse)
            );
            X509CertificateHolder signerCert = null;

            Store<X509CertificateHolder> x509Certs = token.getCertificates();
            List<X509CertificateHolder> certs = new ArrayList<>(x509Certs.getMatches(null));

            // nájdenie podpisového certifikátu tokenu v kolekcii
            for (X509CertificateHolder cert : certs) {
                String cerIssuerName = cert.getIssuer().toString();
                String signerIssuerName = token.getSID().getIssuer().toString();

                // kontrola issuer name a seriového čísla
                if (cerIssuerName.equals(signerIssuerName) &&
                        cert.getSerialNumber().equals(token.getSID().getSerialNumber())) {
                    signerCert = cert;
                    break;
                }
            }

            return signerCert;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public static X509Certificate getCert(String cert) throws Exception {
        try {
            byte[] certificateBytes = java.util.Base64.getDecoder().decode(cert);
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            return (X509Certificate) certFactory.generateCertificate(new ByteArrayInputStream(certificateBytes));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public static X509CRL getCRL(String url) throws Exception {

        ByteArrayInputStream crlData = getDataFromUrl(url);

        if (crlData == null) {
            throw new Exception("Nepodarilo sa stiahnut CRL zo stranky.");
        }

        try {
            X509CRLHolder crlHolder = new X509CRLHolder(crlData);
            return new JcaX509CRLConverter().getCRL(crlHolder);

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }

    public static String checkMessageImprint(byte[] tsResponse, Element root) {
        try {
            TimeStampToken token = new TimeStampToken(
                    new org.bouncycastle.cms.CMSSignedData(tsResponse));

            byte[] MI = token.getTimeStampInfo().getMessageImprintDigest();
            String alghoritm = token.getTimeStampInfo().getHashAlgorithm().getAlgorithm().getId();
            String signatureValue = getNodeValue(root, "ds:SignatureValue");

            if (signatureValue.isEmpty()) {
                return "[TIMESTAMP-ERROR]: SignatureValue nebol najdeny";
            }

            byte[] signatureValueBytes = java.util.Base64.getDecoder().decode(signatureValue.getBytes());

            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

            MessageDigest messageDigest;
            try {
                messageDigest = MessageDigest.getInstance(alghoritm, "BC");
            } catch (Exception e) {
                return "[TIMESTAMP-ERROR]: neznamy algoritmus v MI.";
            }

            if (!Arrays.equals(MI, messageDigest.digest(signatureValueBytes))) {
                return "[TIMESTAMP-ERROR]: MI z casovej peciatky a podpis v SignatureValue sa nezhoduju.";
            }

            return null;

        } catch (Exception e) {
            return e.getMessage();
        }

    }

    private static ByteArrayInputStream getDataFromUrl(String url) throws Exception {

        URL urlHandler;
        try {
            urlHandler = new URL(url);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream is = null;
        try {
            is = urlHandler.openStream();
            byte[] byteChunk = new byte[4096];
            int n;

            while ((n = is.read(byteChunk)) > 0) {
                baos.write(byteChunk, 0, n);
            }
        } catch (Exception e) {
            System.err.printf("Failed while reading bytes from %s: %s", urlHandler.toExternalForm(), e.getMessage());
            return null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    throw new Exception(e.getMessage());
                }
            }
        }

        return new ByteArrayInputStream(baos.toByteArray());
    }


}
