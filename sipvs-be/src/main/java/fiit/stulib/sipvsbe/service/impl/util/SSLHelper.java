package fiit.stulib.sipvsbe.service.impl.util;

import fiit.stulib.sipvsbe.service.AppConfig;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

public class SSLHelper {

    public static void addCertificateToTruststore(String truststorePath, String truststorePassword) {
        try {
            // Load the existing truststore
            KeyStore truststore = KeyStore.getInstance(KeyStore.getDefaultType());
            try (InputStream truststoreStream = new FileInputStream(truststorePath)) {
                truststore.load(truststoreStream, truststorePassword.toCharArray());
            }

            // Load the certificate
            Certificate certificate;
            try (InputStream certificateStream = new FileInputStream(AppConfig.SSL)) {
                certificate = CertificateFactory.getInstance("X.509").generateCertificate(certificateStream);
            }

            // Add the certificate to the truststore
            truststore.setCertificateEntry("sipvs_inuqe", certificate);

            // Save the updated truststore
            try (OutputStream truststoreOutputStream = new FileOutputStream(truststorePath)) {
                truststore.store(truststoreOutputStream, truststorePassword.toCharArray());
            }

            System.out.println("Certificate added to the truststore successfully.");
        } catch (Exception e) {
            throw new RuntimeException("Error adding certificate to the truststore", e);
        }
    }
}
