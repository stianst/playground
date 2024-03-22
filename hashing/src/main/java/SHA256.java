import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class SHA256 implements Hash {

    @Override
    public String hash(String key) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA256");
            sha.digest(key.getBytes(StandardCharsets.UTF_8));
            byte[] result = sha.digest();
            return new String(result, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}