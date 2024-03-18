import java.nio.charset.StandardCharsets;

public class BCrypt implements Hash {

    private static final int strength = 10;

    @Override
    public String hash(String key) {
        byte[] result = org.bouncycastle.crypto.generators.BCrypt.generate(
                key.getBytes(StandardCharsets.UTF_8),
                Salt.generateSalt(),
                strength);
        return new String(result, StandardCharsets.UTF_8);
    }
}