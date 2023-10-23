package fiit.stulib.sipvsbe.controller;

import org.springframework.http.ResponseEntity;

public interface ISignResource {

    String generatePdfFromXml();

    ResponseEntity<String> sign();

    String getXml();

    String getXsd();

    String getXsl();

}
