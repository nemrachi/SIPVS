package fiit.stulib.sipvsbe.service.impl;

import fiit.stulib.sipvsbe.controller.dto.VerifyResultDto;
import fiit.stulib.sipvsbe.service.AppConfig;
import fiit.stulib.sipvsbe.service.IVerificationService;
import fiit.stulib.sipvsbe.service.impl.util.SignatureChecker;
import fiit.stulib.sipvsbe.service.impl.util.Zadanie4Helper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class VerificationService implements IVerificationService {

    private static String getPath(int fileNumber) {
        return AppConfig.XMLS_TO_VERIFY_PATH + buildFilename(fileNumber);
    }

    private static String buildFilename(int fileNumber) {
        return String.format("%02d", fileNumber) + AppConfig.SIGNED_FILE;
    }

    private static File getFile(String filePath) {
        Path path = Paths.get(filePath);
        return path.toFile();
    }

    private static Document getRootDocument(File fileToVerify) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        Document document = null;

        try {
            builder = factory.newDocumentBuilder();
            document = builder.parse(fileToVerify);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        return document;
    }

    // 1. OVERENIE DATOVEJ OBALKY
    private static String checkDataEnvelop(Element rootElement) {
        if (Zadanie4Helper.checkNamespace(rootElement)) {
            return null;
        } else {
            return "overenie datovej obalky: nespravny namespace";
        }
    }

    /*
    // --- VERIFYING FUNCTIONS ---
     */

    // 2. OVERENIE XML SIGNATURE
    private static String checkXMLSignature(Element rootElement) {
        String signatureMethod = Zadanie4Helper.getAttributeValue(rootElement, "ds:SignatureMethod", "Algorithm");
        String canonicalizationMethod = Zadanie4Helper.getAttributeValue(rootElement, "ds:CanonicalizationMethod", "Algorithm");

        List<String> allowedAlghoritms = new ArrayList<>();
        allowedAlghoritms.add("http://www.w3.org/2000/09/xmldsig#dsa-sha1");
        allowedAlghoritms.add("http://www.w3.org/2000/09/xmldsig#rsa-sha1");
        allowedAlghoritms.add("http://www.w3.org/2001/04/xmldsig-more#rsa-sha256");
        allowedAlghoritms.add("http://www.w3.org/2001/04/xmldsig-more#rsa-sha384");
        allowedAlghoritms.add("http://www.w3.org/2001/04/xmldsig-more#rsa-sha512");
        allowedAlghoritms.add("http://www.w3.org/2000/09/xmldsig#sha1");
        allowedAlghoritms.add("http://www.w3.org/2001/04/xmldsig");
        allowedAlghoritms.add("http://www.w3.org/2001/04/xmlenc#sha256");
        allowedAlghoritms.add("http://www.w3.org/2001/04/xmldsig-more#sha384");
        allowedAlghoritms.add("http://www.w3.org/2001/04/xmlenc#sha512");

        if (!allowedAlghoritms.contains(signatureMethod) ||
                !canonicalizationMethod.equals("http://www.w3.org/TR/2001/REC-xml-c14n-20010315")) {
            return "overenie xml signature: nespravny SignatureMethod alebo CanonicalizationMethod";
        }

        return null;
    }

    @Override
    public List<VerifyResultDto> verify() {
        List<VerifyResultDto> results = new ArrayList<>();

        for (int i = 1; i < 14; i++) {
            VerifyResultDto result = new VerifyResultDto();
            result.setFilename(buildFilename(i));

            File fileToVerify = getFile(getPath(i));
            Document document = getRootDocument(fileToVerify);
            Element rootElement = document.getDocumentElement();


            // data envelope check
            String checkDataEnvelopResult = checkDataEnvelop(rootElement);
            if (checkDataEnvelopResult != null) {
                result.setErrorMsg(checkDataEnvelopResult);
                results.add(result);
                continue;
            }

            // signatureMethod and canonicalizationMethod check
            String checkXMLSignatureResult = checkXMLSignature(rootElement);
            if (checkXMLSignatureResult != null) {
                result.setErrorMsg(checkXMLSignatureResult);
                results.add(result);
                continue;
            }

            // signature check
            SignatureChecker signatureChecker = new SignatureChecker(document);
            try {
                //Elementy ds:Transforms a ds:DigestMethod
                signatureChecker.verifyTransformsAndDigestMethod();

                //Core validation
                signatureChecker.verifyCoreReferencesAndDigestValue();
                signatureChecker.verifyCoreSignatureValue();

                //Element ds:Signature:
                signatureChecker.verifySignature();

                //Element ds:SignatureValue
                signatureChecker.verifySignatureValueId();

                //Referencie v Signedinfo
                signatureChecker.verifySignedInfoReferencesAndAttributeValues();

                //Element ds:KeyInfo
                signatureChecker.verifyKeyInfoContent();

                //Element ds:SignatureProperties
                signatureChecker.verifySignaturePropertiesContent();

                //Elementy ds:Manifest
                signatureChecker.verifyManifestElements();

                //Referencie ds:Manifest elementov
                signatureChecker.verifyManifestElementsReferences();

            } catch (Exception e) {
                result.setErrorMsg(e.getMessage());
                results.add(result);
                continue;
            }

            // timestamp check


            results.add(result);
        }

        return results;
    }


}
