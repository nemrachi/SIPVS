package fiit.stulib.sipvsbe.service.impl;

import fiit.stulib.sipvsbe.service.AppConfig;
import fiit.stulib.sipvsbe.service.ITimestampService;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.tsp.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

@Service
@Slf4j
public class TimestampService implements ITimestampService {

    private String initMatch = "<ds:SignatureValue Id=\"signatureIdSignatureValue\" xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:xzep=\"http://www.ditec.sk/ep/signature_formats/xades_zep/v1.1\">";
    private int initOffset = initMatch.length();
    private String lastMatch = "</ds:SignatureValue>";
    private String funnyMatch = "</xades:SignedProperties>";
    private int funnyOffset = funnyMatch.length();
    private String funnyPre = "<xades:UnsignedProperties><xades:UnsignedSignatureProperties><xades:SignatureTimeStamp Id=\"signatureIdSignatureTimeStamp\"><xades:EncapsulatedTimeStamp>";
    private String funnyPost = "</xades:EncapsulatedTimeStamp></xades:SignatureTimeStamp></xades:UnsignedSignatureProperties></xades:UnsignedProperties>";

    @Override
    public String createTimestamp(String xmlString) {

        //String truststorePath = "C:\\Program Files\\Java\\jre1.8.0_181\\lib\\security\\cacerts";
        //String truststorePassword = "changeit";
        //SSLHelper.addCertificateToTruststore(truststorePath, truststorePassword);

        log.info("createTimestamp: {}", xmlString);

        byte[] signatureDigest;

        if (true) {
            String base64signature = xmlString.substring(xmlString.indexOf(this.initMatch) + this.initOffset, xmlString.indexOf(this.lastMatch));

            Digest digest = new SHA256Digest();
            digest.update(base64signature.getBytes(), 0, base64signature.length());

            signatureDigest = new byte[digest.getDigestSize()];
            int outOff = 0;
            digest.doFinal(signatureDigest, outOff);
        } else if (false) {
            signatureDigest = xmlString.substring(xmlString.indexOf(this.initMatch) + this.initOffset, xmlString.indexOf(this.lastMatch)).getBytes();
        } else {
            Digest digest = new SHA256Digest();
            digest.update(xmlString.getBytes(), 0, xmlString.length());

            signatureDigest = new byte[digest.getDigestSize()];
            int outOff = 0;
            digest.doFinal(signatureDigest, outOff);
        }

        TimeStampRequestGenerator tsRequestGenerator = new TimeStampRequestGenerator();
        tsRequestGenerator.setCertReq(true);

        TimeStampRequest tsRequest = tsRequestGenerator.generate(TSPAlgorithms.SHA256, signatureDigest);


        return createStamped(tsRequest, xmlString);

    }

    @Override
    public String createStamped(TimeStampRequest tsRequest, String xmlString) {
        try {
            byte[] responseBytes = getTimestamp(tsRequest.getEncoded());

            TimeStampResponse tsResponse = new TimeStampResponse(responseBytes);
            // extrahovanie podpisanych dat a casovej peciatky
            String timestampB64 = Base64.getEncoder().encodeToString(tsResponse.getTimeStampToken().getEncoded());

            // pridanie XADES-T do XADES-EPES
            int funny = xmlString.indexOf(this.funnyMatch);
            String updated = xmlString.substring(0, funny + this.funnyOffset);
            updated += this.funnyPre;
            updated += timestampB64;
            updated += this.funnyPost;
            updated += xmlString.substring(funny + this.funnyOffset);

            return updated;
        } catch (IOException | TSPException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private byte[] getTimestamp(byte[] tsRequest) {
        try {
            URL url = new URL(AppConfig.TIMESTAMP_SERVER);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/timestamp-query");
            connection.setDoOutput(true);

            try (OutputStream outputStream = connection.getOutputStream()) {
                outputStream.write(tsRequest);
            }

            if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
                String contentType = connection.getContentType();

                if (contentType != null && contentType.toLowerCase().equals("application/timestamp-reply")) {

                    try (InputStream inputStream = connection.getInputStream(); ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            byteArrayOutputStream.write(buffer, 0, bytesRead);
                        }
                        return byteArrayOutputStream.toByteArray();
                    }
                } else {
                    throw new Exception("Incorrect response mimetype: " + contentType);
                }
            } else {
                throw new Exception("HTTP error code: " + connection.getResponseCode());
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
