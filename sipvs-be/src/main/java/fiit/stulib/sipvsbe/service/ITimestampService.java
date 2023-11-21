package fiit.stulib.sipvsbe.service;

import org.bouncycastle.tsp.TimeStampRequest;

public interface ITimestampService {

    String createTimestamp(String signedXML);

    String createStamped(TimeStampRequest tsRequest, String xmlString);
}
