import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Signature;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.NamedParameterSpec;
import java.util.Base64;
import java.util.List;

public class MLDsaExample {

    private static List<Spec> specs = List.of(
            new Spec("ML-DSA", keyPair("ML-DSA", NamedParameterSpec.ML_DSA_87)),
            new Spec("SHA384withECDSA", keyPair("EC", new ECGenParameterSpec("secp384r1"))),
            new Spec("SHA384withRSA", keyPair("RSA", 3072))
    );

    public static void main(String[] args) throws Exception {
        String value = "Hello world!";

        System.out.println("Signature sizes:");
        for (Spec spec : specs) {
            System.out.println(" - " + spec.signAlgorithm + ": " + sign(spec.signAlgorithm, spec.keyPair, value).length + " bytes");
        }
        System.out.println();

        System.out.println("Time for 1000 signatures:");
        for (Spec spec : specs) {
            long s = System.currentTimeMillis();
            for (int i = 0; i < 1000; i++) {
                sign(spec.signAlgorithm, spec.keyPair, value);
            }
            s = System.currentTimeMillis() - s;
            System.out.println(" - " + spec.signAlgorithm + ": " + s + " ms");
        }
    }

    private static byte[] sign(String algorithm, KeyPair keyPair, String value) throws Exception {
        Signature signature = Signature.getInstance(algorithm);
        signature.initSign(keyPair.getPrivate());
        signature.update(value.getBytes(StandardCharsets.UTF_8));
        byte[] signedBytes = signature.sign();
        return Base64.getUrlEncoder().encode(signedBytes);
    }

    private static KeyPair keyPair(String algorithm, Object spec) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
            if (spec instanceof AlgorithmParameterSpec algorithmParameterSpec) {
                keyPairGenerator.initialize(algorithmParameterSpec);
            } else if (spec instanceof Integer keySize) {
                keyPairGenerator.initialize(keySize);
            }
            return keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    private record Spec(String signAlgorithm, KeyPair keyPair) {}

}
