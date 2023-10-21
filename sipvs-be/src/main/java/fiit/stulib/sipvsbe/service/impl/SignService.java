package fiit.stulib.sipvsbe.service.impl;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import fiit.stulib.sipvsbe.service.AppConfig;
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
        File inputHTML = new File(AppConfig.HTML);

        Document document = null;
        try {
            document = Jsoup.parse(inputHTML, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);

        try (OutputStream os = Files.newOutputStream(Paths.get(AppConfig.PDF))) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withUri(AppConfig.PDF);
            builder.toStream(os);
            builder.withW3cDocument(new W3CDom().fromJsoup(document), "/");
            builder.run();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new byte[0];
    }

    @Override
    public String sign() {
        try {
            return new String(Files.readAllBytes(Paths.get(AppConfig.XML)));
        } catch (IOException e) {
            System.err.println("Sign error: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        } finally {
            System.out.println("XML send for signature.");
        }
    }


}
