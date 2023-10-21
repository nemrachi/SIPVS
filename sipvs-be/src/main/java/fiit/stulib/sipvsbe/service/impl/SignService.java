package fiit.stulib.sipvsbe.service.impl;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import fiit.stulib.sipvsbe.service.ISignService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;


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
        String outputPdf = "src/main/resources/out/result.pdf";
        File inputHTML = new File("src/main/resources/out/result.html");

        Document document = null;
        try {
            document = Jsoup.parse(inputHTML, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);

        try (OutputStream os = Files.newOutputStream(Paths.get(outputPdf))) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withUri(outputPdf);
            builder.toStream(os);
            builder.withW3cDocument(new W3CDom().fromJsoup(document), "/");
            builder.run();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new byte[0];
    }
}
