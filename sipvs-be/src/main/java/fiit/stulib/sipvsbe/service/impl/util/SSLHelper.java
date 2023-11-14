package fiit.stulib.sipvsbe.service.impl.util;

import fiit.stulib.sipvsbe.service.AppConfig;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import java.net.URL;

public class SSLHelper {

    public static HttpsURLConnection createHttpsURLConnection(String url) throws Exception {
        // Set up the SSLContext
        SSLContext sslContext = getSSLContext();

        // Set up the HttpsURLConnection with the custom SSLContext
        URL serverURL = new URL(url);
        HttpsURLConnection connection = (HttpsURLConnection) serverURL.openConnection();
        connection.setSSLSocketFactory(sslContext.getSocketFactory());

        return connection;
    }

    private static SSLContext getSSLContext() throws Exception {
        // Load your custom certificate
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        FileInputStream fis = new FileInputStream(AppConfig.SSL);
        Certificate cert = cf.generateCertificate(fis);

        // Create a KeyStore and add the custom certificate
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null, null);
        keyStore.setCertificateEntry("dtcca", cert);

        // Create a TrustManager that trusts the custom certificate
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(keyStore);

        // Create an SSL context with the custom TrustManager
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);

        return sslContext;
    }

}
