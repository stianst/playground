package playground.stianst.github.io;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Provider;
import java.security.Security;

public class ListAvailableSignatures {

    static void main() {
        Security.addProvider(new BouncyCastleProvider());
        for (Provider p : Security.getProviders()) {
            System.out.println(p.getName());
            for (Provider.Service s : p.getServices()) {
                if (s.getType().equals("Signature")) {
                    System.out.println(" - " + s.getAlgorithm() + " " + s.getType());
                }
            }
        }
    }
}
