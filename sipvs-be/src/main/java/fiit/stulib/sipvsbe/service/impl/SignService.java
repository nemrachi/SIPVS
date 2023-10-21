package fiit.stulib.sipvsbe.service.impl;

import fiit.stulib.sipvsbe.service.ISignService;
import lombok.extern.slf4j.Slf4j;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.xmlgraphics.util.MimeConstants;
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
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@Service
public class SignService implements ISignService {

    @Override
    public byte[] createPdfFromXml() {

        File xsltFile = new File("src/main/resources/templates/libraryLoan.xslt");
        StreamSource xmlSource = new StreamSource(new File("src/main/resources/out/result.xml"));
        FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();

        try (OutputStream out = Files.newOutputStream(Paths.get("src/main/resources/out/result.pdf"))) {

            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);

            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(xsltFile));

            Result res = new SAXResult(fop.getDefaultHandler());

            transformer.transform(xmlSource, res);
        } catch (IOException | TransformerException | SAXException e) {
            throw new RuntimeException(e.getMessage());
        }

        return new byte[0];
    }

}
