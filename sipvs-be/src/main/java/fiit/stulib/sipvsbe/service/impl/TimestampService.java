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

    private static final String initSignatureMatch = "<ds:SignatureValue Id=\"signatureIdSignatureValue\" xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:xzep=\"http://www.ditec.sk/ep/signature_formats/xades_zep/v1.1\">";
    private static final String lastSignatureMatch = "</ds:SignatureValue>";
    private static final String magicMatch = "</xades:SignedProperties>";
    private static final String magicPrefix = "<xades:UnsignedProperties><xades:UnsignedSignatureProperties><xades:SignatureTimeStamp Id=\"signatureIdSignatureTimeStamp\"><xades:EncapsulatedTimeStamp>";
    private static final String magicPostfix = "</xades:EncapsulatedTimeStamp></xades:SignatureTimeStamp></xades:UnsignedSignatureProperties></xades:UnsignedProperties>";
    private static final int initSignatureOffset = initSignatureMatch.length();
    private static final int magicOffset = magicMatch.length();

    private static byte[] getTimestamp(byte[] tsRequest) {
        try {
            URL url = new URL(AppConfig.TIMESTAMP_SERVER);
            HttpURLConnection connection = openConnection(url);

            sendTimestampRequest(connection, tsRequest);

            handleResponse(connection);

            return readResponseBody(connection);
        } catch (IOException e) {
            throw new RuntimeException("Error while getting timestamp: " + e.getMessage(), e);
        }
    }

    private static HttpURLConnection openConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/timestamp-query");
        connection.setDoOutput(true);
        return connection;
    }

    private static void sendTimestampRequest(HttpURLConnection connection, byte[] tsRequest) throws IOException {
        try (OutputStream outputStream = connection.getOutputStream()) {
            outputStream.write(tsRequest);
        }
    }

    private static void handleResponse(HttpURLConnection connection) throws IOException {
        if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
            String contentType = connection.getContentType();

            if (contentType != null && contentType.equalsIgnoreCase("application/timestamp-reply")) {
                return;
            } else {
                throw new IOException("Incorrect response: " + contentType);
            }
        } else {
            throw new IOException("HTTP error code: " + connection.getResponseCode());
        }
    }

    private static byte[] readResponseBody(HttpURLConnection connection) throws IOException {
        try (InputStream inputStream = connection.getInputStream(); ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            return byteArrayOutputStream.toByteArray();
        }
    }

    // vždy som si chcel vyskúšať takéto retardovane pomenovanie
    private static String doMagic(String xmlString, String timestampB64) {
        int magicIndex = xmlString.indexOf(magicMatch);
        if (magicIndex != -1) {
            return xmlString.substring(0, magicIndex + magicOffset) +
                    magicPrefix +
                    timestampB64 +
                    magicPostfix +
                    xmlString.substring(magicIndex + magicOffset);
        } else {
            log.error("Error: magicMatch not found in the XML string.");
        }
        return null;
    }

    @Override
    public String createTimestamp(String xmlString) {
        //String truststorePath = "C:\\Program Files\\Java\\jre1.8.0_181\\lib\\security\\cacerts";
        //String truststorePassword = "changeit";
        //SSLHelper.addCertificateToTruststore(truststorePath, truststorePassword);

        byte[] signatureDigest;

        String base64signature = xmlString.substring(xmlString.indexOf(initSignatureMatch) + initSignatureOffset, xmlString.indexOf(lastSignatureMatch));

        Digest digest = new SHA256Digest();
        digest.update(base64signature.getBytes(), 0, base64signature.length());
        signatureDigest = new byte[digest.getDigestSize()];
        digest.doFinal(signatureDigest, 0);

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
            String timestampB64 = Base64.getEncoder().encodeToString(tsResponse.getTimeStampToken().getEncoded());

            // Adding T to XADES-EPES -> creates XADES-T
            String updatedXml = doMagic(xmlString, timestampB64);
            if (updatedXml != null) return updatedXml;

        } catch (IOException | TSPException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

}
