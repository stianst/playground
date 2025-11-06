package playground.stianst.github.io;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Security;
import java.security.Signature;
import java.util.Base64;

public class SignatureTest {

    public static final String OUTPUT = "%-15s %-12s %-12s %-12s %-12s%n";

    public static void main(String[] args) throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        String payload = new String(SignatureTest.class.getClassLoader().getResourceAsStream("payload").readAllBytes(), StandardCharsets.UTF_8);
        int countFast = 100;
        int countSlow = 1;

        System.out.printf(OUTPUT, "Algorithm", "Size", "Size (url)", "Key Size", "Time (ms)");
        for (SignatureAlgorithms a : SignatureAlgorithms.values()) {
            KeyPairGenerator keyPairGenerator = a.getProvider() != null ? KeyPairGenerator.getInstance(a.getKeyPairAlgorithm(), a.getProvider()) : KeyPairGenerator.getInstance(a.getKeyPairAlgorithm());
            keyPairGenerator.initialize(a.getSpec());
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            byte[] signedBytes = new byte[0];
            long start = System.currentTimeMillis();

            int count = a.getName().startsWith("SLH_DSA") ? countSlow : countFast;
            for (int i = 0; i <= count; i++) {
                Signature signature = a.getProvider() != null ? Signature.getInstance(a.getAlgorithm(), a.getProvider()) : Signature.getInstance(a.getAlgorithm());
                signature.initSign(keyPair.getPrivate());
                signature.update(payload.getBytes());
                signedBytes = signature.sign();
            }

            long time =  System.currentTimeMillis() - start;
            String signatureEncoded = Base64.getUrlEncoder().encodeToString(signedBytes);

            System.out.printf(OUTPUT, a.getName(), signedBytes.length, signatureEncoded.length(), keyPair.getPublic().getEncoded().length, (double) time / count);
        }

    }


}
