import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class MD5 implements Hash {

    private static final int strength = 10;

    @Override
    public String hash(String key) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.digest(key.getBytes(StandardCharsets.UTF_8));
            byte[] result = md5.digest();
            return new String(result, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}