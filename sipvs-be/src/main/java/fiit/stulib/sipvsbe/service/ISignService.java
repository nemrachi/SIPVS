package fiit.stulib.sipvsbe.service;

public interface ISignService {

    byte[] createPdfFromXml();

    String sign();

    String getXml();

    String getXsd();

    String getXsl();
}
