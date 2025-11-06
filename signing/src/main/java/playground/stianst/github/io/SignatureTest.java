package playground.stianst.github.io;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Security;
import java.security.Signature;
import java.util.Base64;

public class SignatureTest {

    public static final int COUNT = 10;
    public static final boolean SKIP_SLH_DSA = true;

    public static final String OUTPUT = "%-15s %-12s %-12s %-12s %-12s %-12s%n";

    public static void main(String[] args) throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        String payload = new String(SignatureTest.class.getClassLoader().getResourceAsStream("payload").readAllBytes(), StandardCharsets.UTF_8);

        System.out.printf(OUTPUT, "Algorithm", "Size", "Size (url)", "Key Size", "Time (ms)", "Provider");
        for (SignatureAlgorithms a : SignatureAlgorithms.values()) {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(a.getKeyPairAlgorithm());
            keyPairGenerator.initialize(a.getSpec());
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            byte[] signedBytes = new byte[0];
            long start = System.currentTimeMillis();

            String provider = null;

            if (SKIP_SLH_DSA && a.getAlgorithm().equals("SLHDSA")) {
                continue;
            }

            for (int i = 0; i <= COUNT; i++) {
                Signature signature = Signature.getInstance(a.getAlgorithm());
                signature.initSign(keyPair.getPrivate());
                signature.update(payload.getBytes());
                signedBytes = signature.sign();
                provider = signature.getProvider().getName();
            }

            long time =  System.currentTimeMillis() - start;
            String signatureEncoded = Base64.getUrlEncoder().encodeToString(signedBytes);

            System.out.printf(OUTPUT, a.getName(), signedBytes.length, signatureEncoded.length(), keyPair.getPublic().getEncoded().length, (double) time / COUNT, provider);
        }

    }


}
