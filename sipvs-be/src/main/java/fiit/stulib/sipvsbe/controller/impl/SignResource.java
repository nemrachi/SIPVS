package fiit.stulib.sipvsbe.controller.impl;

import fiit.stulib.sipvsbe.controller.ISignResource;
import fiit.stulib.sipvsbe.service.ISignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Base64;

@Slf4j
@RestController
public class SignResource implements ISignResource {

    @Autowired
    private ISignService signService;

    @Override
    public String generatePdfFromXml() {
        Resource pdfFile = signService.createPdfFromXml();

        byte[] pdfBytes = null;
        try {
            pdfBytes = StreamUtils.copyToByteArray(pdfFile.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Base64.getEncoder().encodeToString(pdfBytes);
    }

    @Override
    public String getXml() {
        return signService.getXml();
    }

    @Override
    public String getXsd() {
        return signService.getXsd();
    }

    @Override
    public String getXsl() {
        return signService.getXsl();
    }
}
