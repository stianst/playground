package playground.stianst.github.io;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.net.URL;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Map;

public class ParseAPK2Test {

    // Values from https://github.com/faceless2/json/blob/44aa505bd731cc4e7e56d046fa22c3c42d9cdafb/src/main/com/bfo/json/JWK.java#L1486-L1488
    // Should be possible to create these from NISTObjectIdentifiers.id_ml_dsa_44.getEncoded() once we figure out the start/end of the byte arrays
    private static final Map<String, AlgorithmIdentifier> IDENTIFIERS = Map.of(
            "ML-DSA-44", new AlgorithmIdentifier(NISTObjectIdentifiers.id_ml_dsa_44),
            "ML-DSA-65", new AlgorithmIdentifier(NISTObjectIdentifiers.id_ml_dsa_65),
            "ML-DSA-87", new AlgorithmIdentifier(NISTObjectIdentifiers.id_ml_dsa_87)
    );

    public static void main(String[] args) throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        for (String a : IDENTIFIERS.keySet()) {
            URL url = new URL("https://raw.githubusercontent.com/cose-wg/draft-ietf-cose-dilithium/refs/heads/main/examples/jose/examples/" + a.replace('-', '_') + ".jose.json");
            ObjectMapper om = new ObjectMapper();
            JsonNode jwk = om.readValue(url.openStream(), JsonNode.class).get("jwk");

            String alg = jwk.get("alg").asText();
            KeyFactory keyFactory = KeyFactory.getInstance(alg);

            String keyIn = jwk.get("pub").asText();

            AlgorithmIdentifier algorithmIdentifier = IDENTIFIERS.get(alg);

            SubjectPublicKeyInfo subjectPublicKeyInfoIn = new SubjectPublicKeyInfo(algorithmIdentifier, Base64.getUrlDecoder().decode(keyIn));
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(subjectPublicKeyInfoIn.getEncoded());
            PublicKey key = keyFactory.generatePublic(keySpec);

            SubjectPublicKeyInfo subjectPublicKeyInfoOut = new SubjectPublicKeyInfo(algorithmIdentifier, key.getEncoded());
            String keyOut = Base64.getUrlEncoder().withoutPadding().encodeToString(subjectPublicKeyInfoOut.getEncoded());

            System.out.println("Alg:  " + alg);
            System.out.println("- In:  " + keyIn.length() + " " + keyIn);
            System.out.println("- Out: " + keyOut.length() + " " + keyOut);
            System.out.println("- Mathces: " + keyOut.equals(keyIn));
            System.out.println();
        }
    }

}
