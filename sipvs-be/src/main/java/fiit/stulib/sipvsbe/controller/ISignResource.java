package fiit.stulib.sipvsbe.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

public interface ISignResource {

    ResponseEntity<Resource> generatePdfFromXml();

    ResponseEntity<String> sign();

    String getXml();

    String getXsd();

    String getXsl();

}
