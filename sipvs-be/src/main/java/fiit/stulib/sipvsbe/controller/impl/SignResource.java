package fiit.stulib.sipvsbe.controller.impl;

import fiit.stulib.sipvsbe.controller.ISignResource;
import fiit.stulib.sipvsbe.service.ISignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/zadanie2")
public class SignResource implements ISignResource {

    @Autowired
    private ISignService signService;

    @GetMapping("/generatePdfFromXml")
    @Override
    public ResponseEntity<Resource> generatePdfFromXml() {

        Resource pdfFile = signService.createPdfFromXml();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" + pdfFile.getFilename());
        
        MediaType mediaType = MediaType.APPLICATION_PDF;

        try {
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(pdfFile.contentLength())
                    .contentType(mediaType)
                    .body(pdfFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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
