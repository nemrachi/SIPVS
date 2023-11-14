package fiit.stulib.sipvsbe.service;

import org.springframework.core.io.FileSystemResource;

public interface ISignService {

    FileSystemResource createPdfFromXml();

    String getXml();

    String getXsd();

    String getXsl();
}
