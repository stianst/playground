import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Signature;
import java.security.spec.AlgorithmParameterSpec;

public class SimpleSign {

    private final BouncyCastleProvider BC = new BouncyCastleProvider();

    private final KeyPair keyPair;
    private final String name;
    private final String signAlgorithm;
    private final String provider;

    public SimpleSign(String name, String algorithm, Object algorithmSpec, String signAlgorithm, String provider) {
        this.name = name;
        this.signAlgorithm = signAlgorithm;
        this.provider = provider;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm, provider);
            if (algorithmSpec instanceof AlgorithmParameterSpec algorithmParameterSpec) {
                keyPairGenerator.initialize(algorithmParameterSpec);
            } else if (algorithmSpec instanceof Integer keySize) {
                keyPairGenerator.initialize(keySize);
            }
            this.keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getName() {
        return name + "-" + provider;
    }

    public byte[] sign(String value) throws Exception {
        Signature signature = Signature.getInstance(signAlgorithm, provider);
        signature.initSign(keyPair.getPrivate());
        signature.update(value.getBytes(StandardCharsets.UTF_8));
        return signature.sign();
    }

}
