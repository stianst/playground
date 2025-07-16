import com.oracle.jipher.provider.JipherJCE;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.NamedParameterSpec;
import java.util.Base64;
import java.util.List;

public class SignatureComparison {

    private static final String PAYLOAD = "Hello world!";
    private static final int ITERATIONS = 100;

    static {
        System.setProperty("jipher.fips.enforcement", "FIPS_STRICT");
        System.setProperty("jipher.fips.deactivateSecurityPatches", "true");

        Security.addProvider(new BouncyCastleProvider());
        Security.addProvider(new JipherJCE());
    }

    private static List<SimpleSign> signers = List.of(
            new SimpleSign("ML-DSA-44", "ML-DSA", NamedParameterSpec.ML_DSA_44, "ML-DSA", "SUN"),
            new SimpleSign("ML-DSA-65", "ML-DSA", NamedParameterSpec.ML_DSA_65, "ML-DSA", "SUN"),
            new SimpleSign("ML-DSA-87", "ML-DSA", NamedParameterSpec.ML_DSA_87, "ML-DSA", "SUN"),
            new SimpleSign("ML-DSA-87", "ML-DSA", NamedParameterSpec.ML_DSA_87, "ML-DSA", "BC"),
            new SimpleSign("LMS", "LMS", null, "LMS", "BC"),
            new SimpleSign("FALCON", "FALCON", null, "FALCON", "BC"),
            new SimpleSign("DILITHIUM", "DILITHIUM", null, "DILITHIUM", "BC"),
//            new SimpleSign("SLH-DSA", "SLH-DSA", null, "SLH-DSA-SHA2-128F", "SUN"),
            new SimpleSign("ES256", "EC", new ECGenParameterSpec("secp256r1"), "SHA256withECDSA", "SunEC"),
            new SimpleSign("ES384", "EC", new ECGenParameterSpec("secp384r1"), "SHA384withECDSA", "SunEC"),
            new SimpleSign("ES512", "EC", new ECGenParameterSpec("secp521r1"), "SHA512withECDSA", "SunEC"),
            new SimpleSign("ES512", "EC", new ECGenParameterSpec("secp521r1"), "SHA512withECDSA", "BC"),
            new SimpleSign("ES512", "EC", new ECGenParameterSpec("secp521r1"), "SHA512withECDSA", "JipherJCE"),
            new SimpleSign("RS256", "RSA", 2048, "SHA256withRSA", "SunRsaSign"),
            new SimpleSign("RS384", "RSA", 3072, "SHA384withRSA", "SunRsaSign"),
            new SimpleSign("RS512", "RSA", 4096, "SHA512withRSA", "SunRsaSign"),
            new SimpleSign("RS512", "RSA", 4096, "SHA512withRSA", "BC"),
            new SimpleSign("RS512", "RSA", 4096, "SHA512withRSA", "JipherJCE")
    );

    public static void main(String[] args) throws Exception {

        System.out.println("Signature sizes:");
        for (SimpleSign signer : signers) {
            byte[] signature = signer.sign(PAYLOAD);
            System.out.println(" - " + signer.getName() + ": " + signature.length + " bytes (" + encode(signature).length + " bytes encoded)");
        }
        System.out.println();

        System.out.println("Time for " + ITERATIONS + " signatures:");
        for (SimpleSign signer : signers) {
            long s = System.currentTimeMillis();
            for (int i = 0; i < ITERATIONS; i++) {
                signer.sign(PAYLOAD);
            }
            s = System.currentTimeMillis() - s;
            System.out.println(" - " + signer.getName() + ": " + s + " ms");
        }
    }

    public static byte[] encode(byte[] bytes) {
        return Base64.getUrlEncoder().encode(bytes);
    }

}
