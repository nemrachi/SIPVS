package fiit.stulib.sipvsbe.service.impl;

import fiit.stulib.sipvsbe.service.ISignService;
import lombok.extern.slf4j.Slf4j;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.xmlgraphics.util.MimeConstants;
import org.springframework.stereotype.Service;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
public class SignService implements ISignService {

    @Override
    public byte[] createPdfFromXml() {

        String knownPathToXml = "src/main/resources/out/result.xml"; // Replace with your actual file path
        Path path = Paths.get(knownPathToXml);

        if (!Files.exists(path)) {
            throw new IllegalArgumentException("File does not exist at the specified path");
        }

        try (ByteArrayOutputStream pdfOutStream = new ByteArrayOutputStream()) {
            // Read XML file to String
            String xmlData = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);

            // Set up the XSLT transformer
            TransformerFactory factory = TransformerFactory.newInstance();
            StreamSource xslt = new StreamSource(new File("src/main/resources/templates/libraryLoan.xsl"));
            Transformer transformer = factory.newTransformer(xslt);

            // Transform XML to XSL-FO
            StreamSource xml = new StreamSource(new StringReader(xmlData));
            StringWriter outWriter = new StringWriter();
            StreamResult result = new StreamResult(outWriter);
            transformer.transform(xml, result);
            String xslFo = outWriter.toString(); // Changed from StringBuffer to String

            // Set up Apache FOP
            FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, pdfOutStream); // Fixed variable name

            // Generate the PDF
            Source src = new StreamSource(new StringReader(xslFo));
            Result res = new SAXResult(fop.getDefaultHandler());
            transformer.transform(src, res); // Reusing the transformer

            return pdfOutStream.toByteArray();

        } catch (Exception e) {
            log.error("Error creating PDF from XML", e); // Logging the exception
            throw new RuntimeException("Error creating PDF from XML", e); // Including the cause in the RuntimeException
        }
    }
}
