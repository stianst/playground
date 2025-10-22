package playground.stianst.github.io;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Security;
import java.security.Signature;
import java.util.Base64;

public class SignatureTest {

    public static final String OUTPUT = "%-15s %-12s %-12s %-10s%n";

    static void main(String[] args) throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        String payload = new String(SignatureTest.class.getClassLoader().getResourceAsStream("payload").readAllBytes(), StandardCharsets.UTF_8);
        int count = 10;

        System.out.printf(OUTPUT, "Algorithm", "Size", "Size (url)", "Time (ms)");
        for (SignatureAlgorithms a : SignatureAlgorithms.values()) {
            KeyPairGenerator keyPairGenerator = a.getProvider() != null ? KeyPairGenerator.getInstance(a.getKeyPairAlgorithm(), a.getProvider()) : KeyPairGenerator.getInstance(a.getKeyPairAlgorithm());
            keyPairGenerator.initialize(a.getSpec());
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            byte[] signedBytes = new byte[0];
            long start = System.currentTimeMillis();
            for (int i = 0; i <= count; i++) {
                Signature signature = a.getProvider() != null ? Signature.getInstance(a.getAlgorithm(), a.getProvider()) : Signature.getInstance(a.getAlgorithm());
                signature.initSign(keyPair.getPrivate());
                signature.update(payload.getBytes());
                signedBytes = signature.sign();
            }

            long time =  System.currentTimeMillis() - start;
            String signatureEncoded = Base64.getUrlEncoder().encodeToString(signedBytes);

            System.out.printf(OUTPUT, a.getName(), signedBytes.length, signatureEncoded.length(), (double) time / count);
        }
    }


}
