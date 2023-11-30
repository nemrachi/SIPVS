package fiit.stulib.sipvsbe.service.impl.util;

import fiit.stulib.sipvsbe.controller.dto.VerifyResultDto;
import fiit.stulib.sipvsbe.service.AppConfig;
import fiit.stulib.sipvsbe.service.IVerificationService;
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

    @Override
    public List<VerifyResultDto> verify() {
        List<VerifyResultDto> results = new ArrayList<>();


        for (int i = 1; i < 14; i++) {
            VerifyResultDto result = new VerifyResultDto();
            result.setFilename(buildFilename(i));

            File fileToVerify = getFile(getPath(i));
            Element rootElement = getRootElement(fileToVerify);


            // data envelope check
            String checkDataEnvelopResult = checkDataEnvelop(rootElement);
            if (checkDataEnvelopResult != null) {
                result.setErrorMsg(checkDataEnvelopResult);
                continue;
            }

            // signatureMethod check
            String checkXMLSignatureResult = checkXMLSignature(rootElement);
            if (checkXMLSignatureResult != null) {
                result.setErrorMsg(checkXMLSignatureResult);
                continue;
            }

            // canonicalizationMethod check

            // timestamp check


            results.add(result);
        }

        return results;
    }

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

    private static Element getRootElement(File fileToVerify) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        Document document = null;

        try {
            builder = factory.newDocumentBuilder();
            document = builder.parse(fileToVerify);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        return document.getDocumentElement();
    }

    // VERIFYING FUNCTIONS

    // 1. OVERENIE DATOVEJ OBALKY
    private static String checkDataEnvelop(Element rootElement) {
        if (checkNamespace(rootElement)){
            return null;
        } else {
            return "overenie datovej obalky: nespravny namespace";
        }
    }

    private static boolean checkNamespace(Element rootElement) {
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

    // 2. OVERENIE XML SIGNATURE
    private static boolean checkURI(Element rootElement) {
        final String signatureMethod = "ds:SignatureMethod";
        final String canonizationMethod = "ds:CanonicalizationMethod";

        // todo
        boolean hasCorrectMethod = false;

        return hasCorrectMethod;
    }

    private static String checkXMLSignature(Element rootElement) {
        if (checkURI(rootElement)) {
            return null;
        } else {
            return "overenie XML podpisu: URI neobsahuje povolenu metodu";
        }

        //return null;
    }



}
