package fiit.stulib.sipvsbe.service;

public interface ISignService {

    byte[] createPdfFromXml();

    byte[] createPdfFromHtml();

    String sign();

    String getXml();

    String getXsd();

    String getXsl();
}
