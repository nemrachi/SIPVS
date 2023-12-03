package fiit.stulib.sipvsbe.service.impl.util;

import fiit.stulib.sipvsbe.service.exception.Zadanie4CustomException;
import it.svario.xpathapi.jaxp.XPathAPI;
import org.apache.xml.security.c14n.CanonicalizationException;
import org.apache.xml.security.c14n.Canonicalizer;
import org.apache.xml.security.c14n.InvalidCanonicalizerException;
import org.apache.xml.security.parser.XMLParserException;
import org.bouncycastle.util.encoders.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpressionException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.*;

public class SignatureChecker extends Verification {
    private static final Map<String, String> DIGEST_ALG;
    private static final Map<String, String> SIGN_ALG;
    private static final Map<String, String> REFERENCES;

    static {
        DIGEST_ALG = new HashMap<>();
        DIGEST_ALG.put("http://www.w3.org/2000/09/xmldsig#sha1", "SHA-1");
        DIGEST_ALG.put("http://www.w3.org/2001/04/xmldsig-more#sha224", "SHA-224");
        DIGEST_ALG.put("http://www.w3.org/2001/04/xmlenc#sha256", "SHA-256");
        DIGEST_ALG.put("http://www.w3.org/2001/04/xmldsig-more#sha384", "SHA-384");
        DIGEST_ALG.put("http://www.w3.org/2001/04/xmlenc#sha512", "SHA-512");
    }

    static {
        SIGN_ALG = new HashMap<>();
        SIGN_ALG.put("http://www.w3.org/2000/09/xmldsig#dsa-sha1", "SHA1withDSA");
        SIGN_ALG.put("http://www.w3.org/2000/09/xmldsig#rsa-sha1", "SHA1withRSA/ISO9796-2");
        SIGN_ALG.put("http://www.w3.org/2001/04/xmldsig-more#rsa-sha256", "SHA256withRSA");
        SIGN_ALG.put("http://www.w3.org/2001/04/xmldsig-more#rsa-sha384", "SHA384withRSA");
        SIGN_ALG.put("http://www.w3.org/2001/04/xmldsig-more#rsa-sha512", "SHA512withRSA");
    }

    static {
        REFERENCES = new HashMap<>();
        REFERENCES.put("ds:KeyInfo", "http://www.w3.org/2000/09/xmldsig#Object");
        REFERENCES.put("ds:SignatureProperties", "http://www.w3.org/2000/09/xmldsig#SignatureProperties");
        REFERENCES.put("xades:SignedProperties", "http://uri.etsi.org/01903#SignedProperties");
        REFERENCES.put("ds:Manifest", "http://www.w3.org/2000/09/xmldsig#Manifest");
    }

    private final List<String> manifestTransformMethods = new ArrayList<>(Arrays.asList(
            "http://www.w3.org/TR/2001/REC-xml-c14n-20010315",
            "http://www.w3.org/2000/09/xmldsig#base64"));
    private final List<String> digestMethods = new ArrayList<>(Arrays.asList(
            "http://www.w3.org/2000/09/xmldsig#sha1",
            "http://www.w3.org/2001/04/xmldsig-more#sha224",
            "http://www.w3.org/2001/04/xmlenc#sha256",
            "http://www.w3.org/2001/04/xmldsig-more#sha384",
            "http://www.w3.org/2001/04/xmlenc#sha512"));
    private final List<String> transformMethods = new ArrayList<>(Collections.singletonList("http://www.w3.org/TR/2001/REC-xml-c14n-20010315"));


    public SignatureChecker(Document document) {
        super(document);
    }

    /*
     * Kontrola obsahu ds:Transforms a ds:DigestMethod vo všetkých referenciách v ds:SignedInfo
     * Musia obsahovať URI niektorého z podporovaných algoritmov podľa profilu XAdES_ZEP
     */
    public void verifyTransformsAndDigestMethod() throws Zadanie4CustomException {

        NodeList transformsElements;
        try {
            transformsElements = XPathAPI.selectNodeList(document.getDocumentElement(), "//ds:Signature/ds:SignedInfo/ds:Reference/ds:Transforms");

        } catch (XPathException e) {

            throw new Zadanie4CustomException(
                    "Chyba pri kontrole elementu ds:Signature/ds:SignedInfo/ds:Reference/ds:Transforms. Element nebol v dokumente najdeny");
        }

        for (int i = 0; i < transformsElements.getLength(); i++) {

            Element transformsElement = (Element) transformsElements.item(i);
            Element transformElement = (Element) transformsElement.getElementsByTagName("ds:Transform").item(0);

            /*
             * Kontrola obsahu ds:Transforms
             * Musi obsahovať URI niektorého z podporovaných algoritmov
             */
            if (assertElementAttributeValue(transformElement, transformMethods)) {

                throw new Zadanie4CustomException(
                        "Atribut Algorithm elementu ds:Transforms neobsahuje URI niektorého z podporovaných algoritmov");
            }
        }

        NodeList digestMethodElements;
        try {
            digestMethodElements = XPathAPI.selectNodeList(document.getDocumentElement(), "//ds:Signature/ds:SignedInfo/ds:Reference/ds:DigestMethod");

        } catch (XPathException e) {

            throw new Zadanie4CustomException(
                    "Chyba pri kontrole elementu ds:Signature/ds:SignedInfo/ds:Reference/ds:DigestMethod. Element nebol v dokumente najdeny");
        }

        for (int i = 0; i < digestMethodElements.getLength(); i++) {

            Element digestMethodElement = (Element) digestMethodElements.item(i);

            if (assertElementAttributeValue(digestMethodElement, digestMethods)) {

                throw new Zadanie4CustomException(
                        "Atribút Algorithm elementu ds:DigestMethod neobsahuje URI niektorého z podporovaných algoritmov");
            }
        }

    }

    /*
     * Core validation (podľa špecifikácie XML Signature)
     * Dereferencovanie URI, kanonikalizácia referencovaných ds:Manifest elementov
     * a overenie hodnôt odtlačkov ds:DigestValue
     */
    public void verifyCoreReferencesAndDigestValue() throws Zadanie4CustomException {
        org.apache.xml.security.Init.init();

        NodeList referencesElements;
        try {
            referencesElements = XPathAPI.selectNodeList(document.getDocumentElement(), "//ds:Signature/ds:SignedInfo/ds:Reference");

        } catch (XPathException e) {

            throw new Zadanie4CustomException(
                    "Chyba pri ziskavani elementu ds:Signature/ds:SignedInfo/ds:Reference. Element nebol v dokumente najdeny");
        }

        for (int i = 0; i < referencesElements.getLength(); i++) {

            Element referenceElement = (Element) referencesElements.item(i);
            String uri = referenceElement.getAttribute("URI").substring(1);

            Element manifestElement = Zadanie4Helper.findByAttributeValue(document, "ds:Manifest", "Id", uri);

            if (manifestElement == null) {
                continue;
            }

            Element digestValueElement = (Element) referenceElement.getElementsByTagName("ds:DigestValue").item(0);
            String expectedDigestValue = digestValueElement.getTextContent();

            Element digestMethodElement = (Element) referenceElement.getElementsByTagName("ds:DigestMethod").item(0);

            if (assertElementAttributeValue(digestMethodElement, digestMethods)) {

                throw new Zadanie4CustomException(
                        "Atribút Algorithm elementu ds:DigestMethod (" + digestMethodElement.getAttribute("Algorithm") +
                                ") neobsahuje URI niektorého z podporovaných algoritmov");
            }

            String digestMethod = digestMethodElement.getAttribute("Algorithm");
            digestMethod = DIGEST_ALG.get(digestMethod);


            byte[] manifestElementBytes;
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();


            try {
                manifestElementBytes = Zadanie4Helper.fromElementToString(manifestElement).getBytes();

            } catch (TransformerException e) {

                throw new Zadanie4CustomException(
                        "Core validation zlyhala. Chyba pri tranformacii z Element do String");
            }

            NodeList transformsElements = manifestElement.getElementsByTagName("ds:Transforms");

            for (int j = 0; j < transformsElements.getLength(); j++) {

                Element transformsElement = (Element) transformsElements.item(j);
                Element transformElement = (Element) transformsElement.getElementsByTagName("ds:Transform").item(0);
                String transformMethod = transformElement.getAttribute("Algorithm");

                manifestElementBytes = getBytes(manifestElementBytes, outputStream, transformMethod);
            }

            MessageDigest messageDigest;

            try {
                messageDigest = MessageDigest.getInstance(digestMethod);

            } catch (NoSuchAlgorithmException e) {

                throw new Zadanie4CustomException(
                        "Core validation zlyhala. Neznamy algoritmus " + digestMethod);
            }
            String actualDigestValue = new String(Base64.encode(messageDigest.digest(manifestElementBytes)));


            if (!expectedDigestValue.equals(actualDigestValue)) {

                throw new Zadanie4CustomException(
                        "Core validation zlyhala. " +
                                "Hodnota ds:DigestValue elementu ds:Reference sa nezhoduje s hash hodnotou elementu ds:Manifest");
            }

        }

    }

    private byte[] getBytes(byte[] manifestElementBytes, ByteArrayOutputStream outputStream, String transformMethod) throws Zadanie4CustomException {
        if ("http://www.w3.org/TR/2001/REC-xml-c14n-20010315".equals(transformMethod)) {

            manifestElementBytes = getBytes2(manifestElementBytes, outputStream, transformMethod);
        }
        return manifestElementBytes;
    }

    private byte[] getBytes2(byte[] manifestElementBytes, ByteArrayOutputStream outputStream, String transformMethod) throws Zadanie4CustomException {
        try {
            Canonicalizer canonicalizer = Canonicalizer.getInstance(transformMethod);
            canonicalizer.canonicalize(manifestElementBytes, outputStream, false);
            manifestElementBytes = outputStream.toByteArray();

        } catch (InvalidCanonicalizerException | CanonicalizationException |
                 IOException e) {

            throw new Zadanie4CustomException("Core validation zlyhala. Chyba pri kanonikalizacii");
        } catch (XMLParserException e) {
            throw new RuntimeException(e);
        }
        return manifestElementBytes;
    }

    /*
     * Core validation (podľa špecifikácie XML Signature)
     * Kanonikalizácia ds:SignedInfo a overenie hodnoty ds:SignatureValue
     * pomocou pripojeného podpisového certifikátu v ds:KeyInfo
     */
    public void verifyCoreSignatureValue() throws Zadanie4CustomException {
        org.apache.xml.security.Init.init();

        Element signatureElement = (Element) document.getElementsByTagName("ds:Signature").item(0);

        Element signedInfoElement = (Element) signatureElement.getElementsByTagName("ds:SignedInfo").item(0);
        Element canonicalizationMethodElement = (Element) signedInfoElement.getElementsByTagName("ds:CanonicalizationMethod").item(0);
        Element signatureMethodElement = (Element) signedInfoElement.getElementsByTagName("ds:SignatureMethod").item(0);
        Element signatureValueElement = (Element) signatureElement.getElementsByTagName("ds:SignatureValue").item(0);


        byte[] signedInfoElementBytes;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            signedInfoElementBytes = Zadanie4Helper.fromElementToString(signedInfoElement).getBytes();
        } catch (TransformerException e) {

            throw new Zadanie4CustomException(
                    "Core validation zlyhala. Chyba pri tranformacii z Element do String");
        }

        String canonicalizationMethod = canonicalizationMethodElement.getAttribute("Algorithm");

        signedInfoElementBytes = getBytes2(signedInfoElementBytes, outputStream, canonicalizationMethod);

        X509Certificate certificate;
        try {
            certificate = Zadanie4Helper.getCertificate(document);

        } catch (XPathExpressionException | RuntimeException | Zadanie4CustomException e) {

            throw new Zadanie4CustomException("X509 certifikát sa v dokumente nepodarilo nájsť");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String signatureMethod = signatureMethodElement.getAttribute("Algorithm");
        signatureMethod = SIGN_ALG.get(signatureMethod);

        Signature signer;
        try {
            signer = Signature.getInstance(signatureMethod);
            signer.initVerify(certificate.getPublicKey());
            signer.update(signedInfoElementBytes);

        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {

            throw new Zadanie4CustomException(
                    "Core validation zlyhala. Chyba pri inicializacii prace s digitalnym podpisom");
        }

        String signatureValueBytes = signatureValueElement.getTextContent();
        byte[] decodedSignatureValueBytes = java.util.Base64.getDecoder().decode(signatureValueBytes);

        boolean verificationResult;

        try {
            verificationResult = signer.verify(decodedSignatureValueBytes);

        } catch (SignatureException e) {

            throw new Zadanie4CustomException(
                    "Core validation zlyhala. Chyba pri verifikacii digitalneho podpisu");
        }

        if (!verificationResult) {

            throw new Zadanie4CustomException(
                    "Podpisana hodnota ds:SignedInfo sa nezhoduje s hodnotou v elemente ds:SignatureValue");
        }

    }

    /*
     Element ds:Signature:
     - musí mať Id atribut,
     - musí mať špecifikovaný namespace xmlns:ds
     */
    public void verifySignature() throws Zadanie4CustomException {
        Element signatureElement = (Element) document.getElementsByTagName("ds:Signature").item(0);
        if (signatureElement == null) {
            throw new Zadanie4CustomException(
                    "Element ds:Signature sa nenašiel");
        }
        if (!signatureElement.hasAttribute("Id")) {
            throw new Zadanie4CustomException(
                    "Element ds:Signature neobsahuje atribut Id");
        }
        if (!assertElementAttributeValue(signatureElement, "xmlns:ds", "http://www.w3.org/2000/09/xmldsig#")) {
            throw new Zadanie4CustomException(
                    "Element ds:Signature nemá nastavený namespace xmlns:ds");
        }
    }

    /*
     * Element ds:SignatureValue
     * 	– musí mať Id atribut
     */
    public void verifySignatureValueId() throws Zadanie4CustomException {
        Element signatureValueElement = (Element) document.getElementsByTagName("ds:SignatureValue").item(0);

        if (signatureValueElement == null) {
            throw new Zadanie4CustomException("Element ds:SignatureValue sa nenašiel");
        }
        if (!signatureValueElement.hasAttribute("Id")) {
            throw new Zadanie4CustomException("Element ds:SignatureValue neobsahuje atribut Id");
        }

    }

    public void verifyKeyInfoContent() throws Zadanie4CustomException {
        Element keyInfoElement = (Element) document.getElementsByTagName("ds:KeyInfo").item(0);

        if (keyInfoElement == null) {
            throw new Zadanie4CustomException("Element ds:Signature sa nenašiel");
        }

        if (!keyInfoElement.hasAttribute("Id")) {
            throw new Zadanie4CustomException("Element ds:Signature neobsahuje atribut Id");
        }

        if (assertElementAttributeValue(keyInfoElement)) {
            throw new Zadanie4CustomException("Atribút Id elementu ds:Signature neobsahuje žiadnu hodnotu");
        }

        Element xDataElement = (Element) keyInfoElement.getElementsByTagName("ds:X509Data").item(0);
        if (xDataElement == null) {
            throw new Zadanie4CustomException("Element ds:KeyInfo neobsahuje element ds:X509Data");
        }

        Element xCertificateElement = (Element) xDataElement.getElementsByTagName("ds:X509Certificate").item(0);
        Element xIssuerSerialElement = (Element) xDataElement.getElementsByTagName("ds:X509IssuerSerial").item(0);
        Element xSubjectNameElement = (Element) xDataElement.getElementsByTagName("ds:X509SubjectName").item(0);

        if (xCertificateElement == null) {
            throw new Zadanie4CustomException("Element ds:X509Data neobsahuje element ds:X509Certificate");
        }

        if (xIssuerSerialElement == null) {
            throw new Zadanie4CustomException("Element ds:X509Data neobsahuje element ds:X509IssuerSerial");
        }

        if (xSubjectNameElement == null) {
            throw new Zadanie4CustomException("Element ds:X509Data neobsahuje element ds:X509SubjectName");
        }

        Element xIssuerNameElement = (Element) xIssuerSerialElement.getElementsByTagName("ds:X509IssuerName").item(0);
        Element xSerialNumberElement = (Element) xIssuerSerialElement.getElementsByTagName("ds:X509SerialNumber").item(0);

        if (xIssuerNameElement == null) {
            throw new Zadanie4CustomException("Element ds:X509IssuerSerial neobsahuje element ds:X509IssuerName");
        }

        if (xSerialNumberElement == null) {
            throw new Zadanie4CustomException("Element ds:X509IssuerSerial neobsahuje element ds:X509SerialNumber");
        }

        X509Certificate certificate;

        try {
            certificate = Zadanie4Helper.getCertificate(document);
        } catch (Exception e) {
            throw new Zadanie4CustomException(
                    "X509 certifikát sa v dokumente nepodarilo nájsť");
        }

        String certifIssuerName = certificate.getIssuerX500Principal().toString().replaceAll("ST=", "S=");
        String certifSerialNumber = certificate.getSerialNumber().toString();
        String certifSubjectName = certificate.getSubjectX500Principal().toString();

        if (!xIssuerNameElement.getTextContent().equals(certifIssuerName)) {
            throw new Zadanie4CustomException("Element ds:X509IssuerName sa nezhoduje s hodnotou na certifikáte");
        }
        if (!xSerialNumberElement.getTextContent().equals(certifSerialNumber))
            throw new Zadanie4CustomException("Element ds:X509SerialNumber sa nezhoduje s hodnotou na certifikáte");

        if (!xSubjectNameElement.getTextContent().equals(certifSubjectName)) {
            throw new Zadanie4CustomException("Element ds:X509SubjectName neobsahuje element ds:X509SerialNumber");
        }

    }

    /*
     * Overenie obsahu ds:SignatureProperties:
     * 	- musí mať Id atribut,
     * 	- musí obsahovať dva elementy ds:SignatureProperty pre xzep:SignatureVersion a xzep:ProductInfos,
     * 	- obidva ds:SignatureProperty musia mať atribut Target nastavený na ds:Signature
     */
    public void verifySignaturePropertiesContent() throws Zadanie4CustomException {
        Element signaturePropertiesElement = (Element) document.getElementsByTagName("ds:SignatureProperties").item(0);

        if (signaturePropertiesElement == null) {
            throw new Zadanie4CustomException("Element ds:SignatureProperties sa nenašiel");
        }

        if (!signaturePropertiesElement.hasAttribute("Id")) {
            throw new Zadanie4CustomException("Element ds:SignatureProperties neobsahuje atribut Id");
        }

        if (assertElementAttributeValue(signaturePropertiesElement)) {
            throw new Zadanie4CustomException("Atribút Id elementu ds:SignatureProperties neobsahuje žiadnu hodnotu");
        }

        Element signatureVersionElement = null;
        Element productInfosElement = null;

        /*
        It iterates over the elements obtained from getElementsByTagName("ds:SignatureProperty"),
         for each element, it looks for child elements with the tags "xzep:SignatureVersion" and
         "xzep:ProductInfos". Depending on whether "xzep:SignatureVersion" is found or not, it updates
         either signatureVersionElement or productInfosElement accordingly.
         */
        NodeList signatureProperties = signaturePropertiesElement.getElementsByTagName("ds:SignatureProperty");
        for (int i = 0; i < signatureProperties.getLength(); i++) {
            Element tempElement = (Element) signatureProperties.item(i);
            Element tempElement2 = (Element) tempElement.getElementsByTagName("xzep:SignatureVersion").item(0);

            if (tempElement2 == null) {
                tempElement2 = (Element) tempElement.getElementsByTagName("xzep:ProductInfos").item(0);
                productInfosElement = tempElement2;
            } else {
                signatureVersionElement = tempElement2;
            }
        }

        if (signatureVersionElement == null) {
            throw new Zadanie4CustomException("ds:SignatureProperties neobsahuje taký element ds:SignatureProperty, ktorý by obsahoval element xzep:SignatureVersion");
        }

        if (productInfosElement == null) {
            throw new Zadanie4CustomException("ds:SignatureProperties neobsahuje taký element ds:SignatureProperty, ktorý by obsahoval element xzep:ProductInfos");
        }

        Element signature = (Element) document.getElementsByTagName("ds:Signature").item(0);

        if (signature == null) {
            throw new Zadanie4CustomException("Element ds:Signature sa nenašiel");
        }

        String signatureId = signature.getAttribute("Id");

        Element sigVerParentElement = (Element) signatureVersionElement.getParentNode();
        Element pInfoParentElement = (Element) productInfosElement.getParentNode();

        String targetSigVer = sigVerParentElement.getAttribute("Target");
        String targetPInfo = pInfoParentElement.getAttribute("Target");

        if (!targetSigVer.equals("#" + signatureId)) {
            throw new Zadanie4CustomException("Atribút Target elementu xzep:SignatureVersion sa neodkazuje na daný ds:Signature");
        }

        if (!targetPInfo.equals("#" + signatureId)) {
            throw new Zadanie4CustomException("Atribút Target elementu xzep:ProductInfos sa neodkazuje na daný ds:Signature");
        }

    }

    /*
     * Overenie existencie referencií v ds:SignedInfo a hodnôt atributov Id a Type voči profilu XAdES_ZEP pre:
     * 	- ds:KeyInfo element,
     * 	- ds:SignatureProperties element,
     * 	- xades:SignedProperties element,
     * 	- všetky ostatné referencie v rámci ds:SignedInfo musia byť referenciami na ds:Manifest elementy
     */
    public void verifySignedInfoReferencesAndAttributeValues() throws Zadanie4CustomException {

        NodeList referencesElements;
        try {
            referencesElements = XPathAPI.selectNodeList(document.getDocumentElement(), "//ds:Signature/ds:SignedInfo/ds:Reference");
        } catch (XPathException e) {
            throw new Zadanie4CustomException(
                    "Chyba pri ziskavani elementu ds:Signature/ds:SignedInfo/ds:Reference. Element nebol v dokumente najdeny");
        }

        for (int i = 0; i < referencesElements.getLength(); i++) {
            Element referenceElement = (Element) referencesElements.item(i);
            String uri = referenceElement.getAttribute("URI").substring(1);
            String actualType = referenceElement.getAttribute("Type");

            Element referencedElement;
            try {
                referencedElement = (Element) XPathAPI.selectSingleNode(this.document.getDocumentElement(),
                        String.format("//ds:Signature//*[@Id='%s']", uri));

            } catch (XPathException e) {
                throw new Zadanie4CustomException(
                        "Chyba pri overeni existencie referencií v ds:SignedInfo. Chyba pri ziskavani elementu s Id " + uri);
            }

            if (referencedElement == null) {
                throw new Zadanie4CustomException(
                        "Chyba pri overeni existencie referencií v ds:SignedInfo. Neexistuje element s Id: " + uri);
            }

            String referencedElementName = referencedElement.getNodeName();

            if (!REFERENCES.containsKey(referencedElementName)) {
                throw new Zadanie4CustomException(
                        "Chyba pri overeni existencie referencií v ds:SignedInfo. Neznama referencia " + referencedElementName);
            }

            String expectedReferenceType = REFERENCES.get(referencedElementName);

            if (!actualType.equals(expectedReferenceType)) {
                throw new Zadanie4CustomException(
                        "Chyba pri overeni zhody referencií v ds:SignedInfo. " + actualType + " sa nezhoduje s " + expectedReferenceType);
            }


            Element keyInfoReferenceElement;
            try {
                keyInfoReferenceElement = (Element) XPathAPI.selectSingleNode(this.document.getDocumentElement(),
                        "//ds:Signature/ds:SignedInfo/ds:Reference[@Type='http://www.w3.org/2000/09/xmldsig#Object']");

            } catch (XPathException e) {
                throw new Zadanie4CustomException(
                        "Chyba pri overeni existencie referencií v ds:SignedInfo. Chyba pri ziskavani elementu s Type http://www.w3.org/2000/09/xmldsig#Object");
            }

            if (keyInfoReferenceElement == null) {
                throw new Zadanie4CustomException(
                        "Neexistuje referencia na ds:KeyInfo element v elemente ds:Reference");
            }


            Element signaturePropertieReferenceElement;
            try {
                signaturePropertieReferenceElement = (Element) XPathAPI.selectSingleNode(this.document.getDocumentElement(),
                        "//ds:Signature/ds:SignedInfo/ds:Reference[@Type='http://www.w3.org/2000/09/xmldsig#SignatureProperties']");

            } catch (XPathException e) {
                throw new Zadanie4CustomException(
                        "Chyba pri overeni existencie referencií v ds:SignedInfo." +
                                "Chyba pri ziskavani elementu s Type http://www.w3.org/2000/09/xmldsig#SignatureProperties");
            }

            if (signaturePropertieReferenceElement == null) {
                throw new Zadanie4CustomException(
                        "Neexistuje referencia na ds:SignatureProperties element v elemente ds:Reference");
            }


            Element signedInfoReferenceElement;
            try {
                signedInfoReferenceElement = (Element) XPathAPI.selectSingleNode(this.document.getDocumentElement(),
                        "//ds:Signature/ds:SignedInfo/ds:Reference[@Type='http://uri.etsi.org/01903#SignedProperties']");

            } catch (XPathException e) {
                throw new Zadanie4CustomException(
                        "Chyba pri overeni existencie referencií v ds:SignedInfo." +
                                "Chyba pri ziskavani elementu s Type http://uri.etsi.org/01903#SignedProperties");
            }

            if (signedInfoReferenceElement == null) {
                throw new Zadanie4CustomException(
                        "Neexistuje referencia na xades:SignedProperties element v elemente ds:Reference");
            }
        }

    }

    /*
     * Overenie ds:Manifest elementov:
     * 	- každý ds:Manifest element musí mať Id atribut,
     * 	- ds:Transforms musí byť z množiny podporovaných algoritmov pre daný element podľa profilu XAdES_ZEP,
     * 	- ds:DigestMethod – musí obsahovať URI niektorého z podporovaných algoritmov podľa profilu XAdES_ZEP,
     * 	- overenie hodnoty Type atributu voči profilu XAdES_ZEP,
     * 	- každý ds:Manifest element musí obsahovať práve jednu referenciu na ds:Object
     */
    public void verifyManifestElements() throws Zadanie4CustomException {
        NodeList manifestElements;
        try {
            manifestElements = XPathAPI.selectNodeList(document.getDocumentElement(), "//ds:Signature/ds:Object/ds:Manifest");

        } catch (XPathException e) {
            throw new Zadanie4CustomException("Chyba pri hladanie ds:Manifest elementov v dokumente");
        }

        for (int i = 0; i < manifestElements.getLength(); i++) {
            Element manifestElement = (Element) manifestElements.item(i);

            if (!manifestElement.hasAttribute("Id")) {
                throw new Zadanie4CustomException("Element ds:Manifest nema atribut Id");
            }

            try {
                NodeList referenceElements = XPathAPI.selectNodeList(manifestElement, "ds:Reference");

                if (referenceElements.getLength() != 1) {
                    throw new Zadanie4CustomException("ds:Manifest element neobsahuje prave jednu referenciu na objekt");
                }
            } catch (XPathException e) {
                throw new Zadanie4CustomException("Chyba pri hladanii ds:Reference elementov v ds:Manifest elemente");
            }
        }

        NodeList referenceElements;
        try {
            referenceElements = XPathAPI.selectNodeList(document.getDocumentElement(), "//ds:Signature/ds:Object/ds:Manifest/ds:Reference");
        } catch (XPathException e) {

            throw new Zadanie4CustomException("Chyba pri hladanii ds:Reference elementov v dokumente");
        }

        for (int i = 0; i < referenceElements.getLength(); i++) {
            Element referenceElement = (Element) referenceElements.item(i);

            try {
                NodeList transformsElements = XPathAPI.selectNodeList(referenceElement, "ds:Transforms/ds:Transform");

                for (int j = 0; j < transformsElements.getLength(); j++) {
                    Element transformElement = (Element) transformsElements.item(j);
                    if (!manifestTransformMethods.contains(transformElement.getAttribute("Algorithm"))) {
                        throw new Zadanie4CustomException("Element ds:Transform obsahuje nepovoleny typ algoritmu");
                    }
                }

                Element digestMethodElement = (Element) XPathAPI.selectSingleNode(referenceElement, "ds:DigestMethod");
                if (!digestMethods.contains(digestMethodElement.getAttribute("Algorithm"))) {
                    throw new Zadanie4CustomException("Atribút Algorithm elementu ds:DigestMethod neobsahuje URI niektorého z podporovaných algoritmov");
                }

                if (!"http://www.w3.org/2000/09/xmldsig#Object".equals(referenceElement.getAttribute("Type"))) {
                    throw new Zadanie4CustomException("Atribút Type elementu ds:Reference neobsahuje hodnotu http://www.w3.org/2000/09/xmldsig#Object");
                }
            } catch (XPathException e) {
                throw new Zadanie4CustomException("Chyba pri hladanie elementov v dokumente");
            }
        }
    }

    /*
     * Overenie referencií v elementoch ds:Manifest:
     * 	- dereferencovanie URI, aplikovanie príslušnej ds:Transforms transformácie (pri base64 decode),
     * 	- overenie hodnoty ds:DigestValue
     */
    public void verifyManifestElementsReferences() throws Zadanie4CustomException {

        NodeList referenceElements;
        try {
            referenceElements = XPathAPI.selectNodeList(document.getDocumentElement(), "//ds:Signature/ds:Object/ds:Manifest/ds:Reference");

        } catch (XPathException e) {

            throw new Zadanie4CustomException("Chyba pri hladanii ds:Reference elementov v dokumente");
        }

        for (int i = 0; i < referenceElements.getLength(); i++) {

            Element referenceElement = (Element) referenceElements.item(i);
            String uri = referenceElement.getAttribute("URI").substring(1);

            Element objectElement = Zadanie4Helper.findByAttributeValue(document, "ds:Object", "Id", uri);

            Element digestValueElement = (Element) referenceElement.getElementsByTagName("ds:DigestValue").item(0);
            Element digestMethodlement = (Element) referenceElement.getElementsByTagName("ds:DigestMethod").item(0);

            String digestMethod = digestMethodlement.getAttribute("Algorithm");
            digestMethod = DIGEST_ALG.get(digestMethod);

            NodeList transformsElements = referenceElement.getElementsByTagName("ds:Transforms");

            for (int j = 0; j < transformsElements.getLength(); j++) {

                Element transformsElement = (Element) transformsElements.item(j);
                Element transformElement = (Element) transformsElement.getElementsByTagName("ds:Transform").item(j);

                String transformMethod = transformElement.getAttribute("Algorithm");

                byte[] objectElementBytes;
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                try {
                    objectElementBytes = Zadanie4Helper.fromElementToString(objectElement).getBytes();

                } catch (TransformerException e) {

                    throw new Zadanie4CustomException(
                            "Overenie referencií v elementoch ds:Manifest zlyhalo. Chyba pri tranformacii z Element do String");
                }

                objectElementBytes = getBytes(objectElementBytes, outputStream, transformMethod);

                if ("http://www.w3.org/2000/09/xmldsig#base64".equals(transformMethod)) {

                    objectElementBytes = Base64.decode(objectElementBytes);
                }

                MessageDigest messageDigest;
                try {
                    messageDigest = MessageDigest.getInstance(digestMethod);

                } catch (NoSuchAlgorithmException e) {

                    throw new Zadanie4CustomException(
                            "Core validation zlyhala. Neznamy algoritmus " + digestMethod);
                }

                String actualDigestValue = new String(Base64.encode(messageDigest.digest(objectElementBytes)));
                String expectedDigestValue = digestValueElement.getTextContent();

                if (!expectedDigestValue.equals(actualDigestValue)) {

                    throw new Zadanie4CustomException(
                            "Hodnota ds:DigestValue elementu ds:Reference sa nezhoduje s hash hodnotou elementu ds:Manifest.");
                }
            }
        }

    }

}
