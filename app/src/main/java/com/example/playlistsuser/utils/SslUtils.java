package com.example.playlistsuser.utils;

import android.content.Context;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class SslUtils {
    public static SSLContext getSslContextForCertificateFile(Context context, String certificateAssetFileName) {
        try {
            // Carrega o certificado da pasta assets
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            InputStream inputStream = context.getAssets().open(certificateAssetFileName);
            X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(inputStream);
            inputStream.close();

            // Cria um KeyStore e adiciona o certificado
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", certificate);

            // Cria um TrustManager que usa o KeyStore
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            // Inicializa o SSLContext com o TrustManager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

            return sslContext;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao configurar SSLContext com o certificado: " + certificateAssetFileName, e);
        }
    }
}
