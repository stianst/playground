import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;

public class Pbkdf2_SHA512_210K implements Hash {

    private static final int iterations = 210000;

    @Override
    public String hash(String key) {
        try {
            KeySpec spec = new PBEKeySpec(key.toCharArray(), Salt.generateSalt(), iterations, 32);
            byte[] result = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512", "BC").generateSecret(spec).getEncoded();
            return new String(result, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}