package fiit.stulib.sipvsbe.service.impl;

import fiit.stulib.sipvsbe.service.AppConfig;
import fiit.stulib.sipvsbe.service.ISignService;
import lombok.extern.slf4j.Slf4j;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@Service
public class SignService implements ISignService {

    @Override
    public FileSystemResource createPdfFromXml() {
        File xsltFile = new File(AppConfig.XSLT);
        StreamSource xmlSource = new StreamSource(new File(AppConfig.XML));
        FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();

        try (OutputStream out = Files.newOutputStream(Paths.get(AppConfig.PDF))) {

            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);

            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(xsltFile));

            Result res = new SAXResult(fop.getDefaultHandler());

            transformer.transform(xmlSource, res);
        } catch (IOException | TransformerException | SAXException e) {
            throw new RuntimeException(e.getMessage());
        }

        return new FileSystemResource(AppConfig.PDF);
    }

    @Override
    public String getXml() {
        return readFile(AppConfig.XML);
    }

    @Override
    public String getXsd() {
        return readFile(AppConfig.XSD);
    }

    @Override
    public String getXsl() {
        return readFile(AppConfig.XSL);
    }

    private String readFile(String filename) {
        byte[] bytes = new byte[0];
        try {
            bytes = Files.readAllBytes(Paths.get(filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }


}
