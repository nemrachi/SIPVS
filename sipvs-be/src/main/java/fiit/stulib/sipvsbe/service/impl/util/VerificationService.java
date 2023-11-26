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

@Slf4j
@Service
public class VerificationService implements IVerificationService {

    @Override
    public VerifyResultDto verify() {
        VerifyResultDto result = new VerifyResultDto();

        for (int i = 1; i < 14; i++) {
            File fileToVerify = getFile(getPath(i));
            Element rootElement = getRootElement(fileToVerify);





        }

        return result;
    }

    private static String getPath(int fileNumber) {
        return AppConfig.XMLS_TO_VERIFY_PATH + String.format("%02d", fileNumber) + AppConfig.SIGNED_FILE;
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

}
