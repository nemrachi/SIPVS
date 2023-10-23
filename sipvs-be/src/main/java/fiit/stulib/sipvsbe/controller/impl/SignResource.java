package fiit.stulib.sipvsbe.controller.impl;

import fiit.stulib.sipvsbe.controller.ISignResource;
import fiit.stulib.sipvsbe.service.ISignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Base64;

@Slf4j
@RestController
@RequestMapping("/api/zadanie2")
public class SignResource implements ISignResource {

    @Autowired
    private ISignService signService;

    @GetMapping("/generatePdfFromXml")
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


    @GetMapping(path = "/sign", produces = "application/pdf")
    @Override
    public ResponseEntity<String> sign() {
        try {
            return ResponseEntity.ok(signService.sign());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Sign error: " + e.getMessage());
        }
    }

    @GetMapping(path = "/getxml", produces = "application/xml")
    @Override
    public String getXml() {
        return signService.getXml();
    }

    @GetMapping(path = "/getxsd", produces = "application/xml")
    @Override
    public String getXsd() {
        return signService.getXsd();
    }

    @GetMapping(path = "/getxsl", produces = "application/xml")
    @Override
    public String getXsl() {
        return signService.getXsl();
    }
}
