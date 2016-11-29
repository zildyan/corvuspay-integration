package hr.corvuspay.ssl;

import javax.net.ssl.X509KeyManager;
import java.net.Socket;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CorvusPayX509KeyManager implements X509KeyManager {

    private final List<X509KeyManager> keyManagers;

    public CorvusPayX509KeyManager(List<X509KeyManager> keyManagers) {
        this.keyManagers = keyManagers;
    }

    @Override
    public String chooseClientAlias(String[] keyType, Principal[] issuers, Socket socket) {
        for (X509KeyManager keyManager : keyManagers) {
            String alias = keyManager.chooseClientAlias(keyType, issuers, socket);
            if (alias != null) {
                return alias;
            }
        }
        return null;
    }

    @Override
    public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket) {
        for (X509KeyManager keyManager : keyManagers) {
            String alias = keyManager.chooseServerAlias(keyType, issuers, socket);
            if (alias != null) {
                return alias;
            }
        }
        return null;
    }

    @Override
    public PrivateKey getPrivateKey(String alias) {
        for (X509KeyManager keyManager : keyManagers) {
            PrivateKey privateKey = keyManager.getPrivateKey(alias);
            if (privateKey != null)
                return privateKey;
        }
        return null;
    }

    @Override
    public X509Certificate[] getCertificateChain(String alias) {
        for (X509KeyManager keyManager : keyManagers) {
            X509Certificate[] chain = keyManager.getCertificateChain(alias);
            if (chain != null && chain.length > 0)
                return chain;
        }
        return null;
    }

    @Override
    public String[] getClientAliases(String keyType, Principal[] issuers) {
        List<String> aliases = new ArrayList<String>();
        for (X509KeyManager keyManager : keyManagers) {
            aliases.addAll(Arrays.asList(keyManager.getClientAliases(keyType, issuers)));
        }
        return emptyToNull(aliases.toArray(new String[aliases.size()]));
    }

    @Override
    public String[] getServerAliases(String keyType, Principal[] issuers) {
        List<String> aliases = new ArrayList<String>();
        for (X509KeyManager keyManager : keyManagers) {
            aliases.addAll(Arrays.asList(keyManager.getServerAliases(keyType, issuers)));
        }
        return emptyToNull(aliases.toArray(new String[aliases.size()]));
    }

    private <T> T[] emptyToNull(T[] arr) {
        return (arr.length == 0) ? null : arr;
    }

}
