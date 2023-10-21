package fiit.stulib.sipvsbe.controller.impl;

import fiit.stulib.sipvsbe.controller.ISignResource;
import fiit.stulib.sipvsbe.service.ISignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/zadanie2")
public class SignResource implements ISignResource {

    @Autowired
    private ISignService signService;

    @GetMapping("/generatePdf")
    @Override
    public ResponseEntity<ByteArrayResource> generatePdf() {
        byte[] pdfData = signService.createPdfFromXml();

        ByteArrayResource resource = new ByteArrayResource(pdfData);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=generated-document.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdfData.length)
                .body(resource);
    }

    // return XML that FE signs with ditec
    @GetMapping(path = "/sign", produces = "application/xml")
    @Override
    public ResponseEntity<String> sign() {
        try {
            return ResponseEntity.ok(signService.sign());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Sign error: " + e.getMessage());
        }
    }
}
