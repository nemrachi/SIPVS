package fiit.stulib.sipvsbe.controller;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;

public interface ISignResource {

    ResponseEntity<ByteArrayResource> generatePdf();

    ResponseEntity<String> sign();

    String getXml();

    String getXsd();

    String getXsl();

}
