package hr.corvuspay.ssl;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.Enumeration;

import static hr.corvuspay.exceptions.CorvusPayRuntimeExceptions.KeyStoreCreationException;

public class CorvusPaySslManager {

    public static SSLContext getSslContext(String pkcs12Name, String pkcs12Password) {
        try {
            String jksPassword = "JksPassword";
            KeyStore jksKeyStore = getKeyStore("JKS", null, jksPassword);
            KeyStore pkcsKeyStore = getKeyStore("PKCS12", pkcs12Name, pkcs12Password);

            Enumeration<String> aliases = pkcsKeyStore.aliases();

            while (aliases.hasMoreElements()) {
                String strAlias = aliases.nextElement();
                if (pkcsKeyStore.isKeyEntry(strAlias)) {
                    Key key = pkcsKeyStore.getKey(strAlias, pkcs12Password.toCharArray());
                    Certificate[] chain = pkcsKeyStore.getCertificateChain(strAlias);
                    jksKeyStore.setKeyEntry(strAlias, key, jksPassword.toCharArray(), chain);
                }
            }

            jksKeyStore.store(new FileOutputStream("JksName"), jksPassword.toCharArray());
            return provideSSLContext(jksKeyStore, jksPassword.toCharArray());

        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException | UnrecoverableKeyException | KeyManagementException e) {
            throw new KeyStoreCreationException(e);
        }
    }

    private static KeyStore getKeyStore(String keyStoreType, String keyStoreName, String keyStorePassword) throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
        KeyStore jksKeyStore = KeyStore.getInstance(keyStoreType);
        FileInputStream jksIs = keyStoreName != null ? new FileInputStream(keyStoreName) : null;
        jksKeyStore.load(jksIs, keyStorePassword.toCharArray());
        return jksKeyStore;
    }

    private static SSLContext provideSSLContext(KeyStore keystore, char[] password) throws NoSuchAlgorithmException, KeyManagementException, UnrecoverableKeyException, KeyStoreException {
        String defaultAlgorithm = KeyManagerFactory.getDefaultAlgorithm();
        X509KeyManager customKeyManager = getKeyManager("SunX509", keystore, password);
        X509KeyManager jvmKeyManager = getKeyManager(defaultAlgorithm, null, null);
        X509TrustManager customTrustManager = getTrustManager("SunX509", keystore);
        X509TrustManager jvmTrustManager = getTrustManager(defaultAlgorithm, null);

        KeyManager[] keyManagers = {new CorvusPayX509KeyManager(Arrays.asList(jvmKeyManager, customKeyManager))};
        TrustManager[] trustManagers = {new CorvusPayX509TrustManager(Arrays.asList(jvmTrustManager, customTrustManager))};

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(keyManagers, trustManagers, null);
        return context;
    }

    private static X509KeyManager getKeyManager(String algorithm, KeyStore keystore, char[] password) throws NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException {
        KeyManagerFactory factory = KeyManagerFactory.getInstance(algorithm);
        factory.init(keystore, password);
        return (X509KeyManager) Arrays.asList(factory.getKeyManagers()).get(0);
    }

    private static X509TrustManager getTrustManager(String algorithm, KeyStore keystore) throws NoSuchAlgorithmException, KeyStoreException {
        TrustManagerFactory factory = TrustManagerFactory.getInstance(algorithm);
        factory.init(keystore);
        return (X509TrustManager) Arrays.asList(factory.getTrustManagers()).get(0);
    }
}
