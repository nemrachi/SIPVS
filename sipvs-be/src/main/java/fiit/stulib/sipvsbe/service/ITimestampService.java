package fiit.stulib.sipvsbe.service;

public interface ITimestampService {

    String createTimestamp(String signedXML);

    String createStamped(String xml, String stamp);
}
