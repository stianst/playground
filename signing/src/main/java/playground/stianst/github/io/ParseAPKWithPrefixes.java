package playground.stianst.github.io;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URL;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Map;

public class ParseAPKWithPrefixes {

    private static final Map<String, byte[]> PREFIXES = Map.of(
            "ML-DSA-44", new byte[] { 48, -126, 5, 50, 48, 11, 6, 9, 96, -122, 72, 1, 101, 3, 4, 3, 17, 3, -126, 5, 33, 0, },
            "ML-DSA-65", new byte[] { 48, -126, 7, -78, 48, 11, 6, 9, 96, -122, 72, 1, 101, 3, 4, 3, 18, 3, -126, 7, -95, 0, },
            "ML-DSA-87", new byte[] { 48, -126, 10, 50, 48, 11, 6, 9, 96, -122, 72, 1, 101, 3, 4, 3, 19, 3, -126, 10, 33, 0, }
    );

    public static void main(String[] args) throws Exception {
        for (String a : PREFIXES.keySet()) {
            URL url = new URL("https://raw.githubusercontent.com/cose-wg/draft-ietf-cose-dilithium/refs/heads/main/examples/jose/examples/" + a.replace('-', '_') + ".jose.json");
            ObjectMapper om = new ObjectMapper();
            JsonNode jwk = om.readValue(url.openStream(), JsonNode.class).get("jwk");

            String alg = jwk.get("alg").asText();
            KeyFactory keyFactory = KeyFactory.getInstance(alg);

            String keyIn = jwk.get("pub").asText();

            byte[] prefix = PREFIXES.get(alg);

            byte[] keyInWithPadding = combine(prefix, Base64.getUrlDecoder().decode(keyIn));

            EncodedKeySpec keySpec = new X509EncodedKeySpec(keyInWithPadding, alg);
            PublicKey key = keyFactory.generatePublic(keySpec);

            byte[] keyOutWithoutPadding = removePadding(key.getEncoded(), prefix.length);

            String keyOut = Base64.getUrlEncoder().withoutPadding().encodeToString(keyOutWithoutPadding);

            System.out.println("Alg:  " + alg);
            System.out.println("- In:  " + keyIn.length() + " " + keyIn);
            System.out.println("- Out: " + keyOut.length() + " " + keyOut);
            System.out.println("- Mathces: " + keyOut.equals(keyIn));
            System.out.println();
        }
    }

    static byte[] combine(byte[] first, byte[] second) {
        byte[] c = new byte[first.length + second.length];
        System.arraycopy(first, 0, c, 0, first.length);
        System.arraycopy(second, 0, c, first.length, second.length);
        return c;
    }

    static byte[] removePadding(byte[] bytes, int length) {
        byte[] b = new byte[bytes.length - length];
        System.arraycopy(bytes, length, b, 0, bytes.length - length);
        return b;
    }

}
