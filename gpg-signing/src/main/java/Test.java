import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Provider;
import java.security.Security;
import java.util.LinkedList;
import java.util.List;

public class Test {

    public static void main(String[] args) throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        List<String> providers = new LinkedList<>();

        for (Provider p : Security.getProviders()) {
            p.getServices().stream()
                    .filter(s -> s.getType().equals("KeyPairGenerator") || s.getType().equals("Signature"))
                    .forEach(s -> providers.add(s.getAlgorithm() + ", " + s.getType() + ", " + p.getName()));
        }

        providers.stream().sorted().forEach(System.out::println);
    }

}
