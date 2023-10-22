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
import java.nio.charset.StandardCharsets;
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
        // toto este neviem co naco bude
        return null;
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
