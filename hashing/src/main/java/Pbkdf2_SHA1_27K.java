import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;

public class Pbkdf2_SHA1_27K implements Hash {

    private static final int iterations = 27500;

    @Override
    public String hash(String key) {
        try {
            KeySpec spec = new PBEKeySpec(key.toCharArray(), Salt.generateSalt(), iterations, 32);
            byte[] result = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1", "BC").generateSecret(spec).getEncoded();
            return new String(result, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

}
