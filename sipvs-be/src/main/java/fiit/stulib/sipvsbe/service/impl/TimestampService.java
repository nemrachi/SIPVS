package fiit.stulib.sipvsbe.service.impl;

import fiit.stulib.sipvsbe.service.AppConfig;
import fiit.stulib.sipvsbe.service.ITimestampService;
import fiit.stulib.sipvsbe.service.impl.util.SSLHelper;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.tsp.TSPException;
import org.bouncycastle.tsp.TimeStampResponse;
import org.bouncycastle.tsp.TimeStampToken;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;

@Service
@Slf4j
public class TimestampService implements ITimestampService {

    @Override
    public String createTimestamp(String signedXML) {
        String timestamp = null;

        try {
            timestamp = getTimestampFromRemote(signedXML);
        } catch (IOException | TSPException e) {
            throw new RuntimeException(e);
        }

        if (timestamp == null) {
            throw new RuntimeException("Nepodarilo sa pridať časovú pečiatku");
        } else {
            log.info(timestamp);
            return timestamp;
        }

    }

    @Override
    public String createStamped(String xml, String stamp) {
        String stamped = "";
        int i = xml.lastIndexOf("</xades:SignedProperties>");
        stamped = xml.substring(0, i + 25);
        stamped += "<xades:UnsignedProperties>";
        stamped += "<xades:UnsignedSignatureProperties>";
        stamped += "<xades:SignatureTimeStamp Id=\"signatureIdSignatureTimeStamp\">";
        stamped += "<xades:EncapsulatedTimeStamp>";
        stamped += stamp;
        stamped += "</xades:EncapsulatedTimeStamp>";
        stamped += "</xades:SignatureTimeStamp>";
        stamped += "</xades:UnsignedSignatureProperties>";
        stamped += "</xades:UnsignedProperties>";
        stamped += xml.substring(i + 25);
        return stamped;
    }

    private static String getTimestampFromRemote(String signWrapper) throws IOException, TSPException {

        String startMarker = "<ds:SignatureValue Id=\"signatureIdSignatureValue\">";
        String endMarker = "</ds:SignatureValue>";

        int startIndex = signWrapper.indexOf(startMarker) + startMarker.length();
        int endIndex = signWrapper.indexOf(endMarker, startIndex);

        if (startIndex >= 0 && endIndex >= 0) {
            signWrapper = signWrapper.substring(startIndex, endIndex);
        } else {
            throw new RuntimeException("Nie je možné čítať podpis");
        }


        try {
            HttpsURLConnection tsConnection = SSLHelper.createHttpsURLConnection(AppConfig.TIMESTAMP_SERVER);

            tsConnection.setDoOutput(true);
            tsConnection.setDoInput(true);
            tsConnection.setRequestMethod("POST");
            tsConnection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            tsConnection.setRequestProperty("SOAPAction", "http://www.ditec.sk/GetTimestamp");

            OutputStream out = tsConnection.getOutputStream();
            Writer wout = new OutputStreamWriter(out);
            wout.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                    "  <soap:Body>\n" +
                    "    <GetTimestamp xmlns=\"http://www.ditec.sk/\">\n" +
                    "      <dataB64>" + Base64.toBase64String(signWrapper.getBytes()) + "</dataB64>\n" +
                    "    </GetTimestamp>\n" +
                    "  </soap:Body>\n" +
                    "</soap:Envelope>"
            );

            wout.flush();
            wout.close();

            String response = getResponse(tsConnection);
            tsConnection.disconnect();

            try {
                TimeStampResponse timeStampResponse = new TimeStampResponse(Base64.decode(response));
                TimeStampToken timestampToken = timeStampResponse.getTimeStampToken();
                return Base64Coder.encodeString(Base64Coder.encodeLines(timestampToken.getEncoded()));
            } catch (TSPException e) {
                log.error("TSPException: ", e);
                throw e;
            }

        } catch (IOException | TSPException e) {
            log.error("IOException: ", e);
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String getResponse(HttpsURLConnection tsConnection) throws IOException {
        InputStream inputStream = tsConnection.getInputStream();

        int c;
        StringBuilder responseBuilder = new StringBuilder();
        while ((c = inputStream.read()) != -1) {
            responseBuilder.append((char) c);
        }

        inputStream.close();
        String response = responseBuilder.toString();

        int i = response.indexOf("<GetTimestampResult>");
        response = response.substring(i + 20);
        i = response.indexOf("</GetTimestampResult>");
        response = response.substring(0, i);
        return response;
    }

}
