import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

import java.nio.charset.StandardCharsets;

public class SCrypt implements Hash {

    private static final int cpuMemoryCost = (int) Math.round(Math.pow(2, 17));
    private static final int blockSize = 8;
    private static final int parallelization = 1;
    private static final int keyLength = 32;

    @Override
    public String hash(String key) {
        byte[] result = org.bouncycastle.crypto.generators.SCrypt.generate(
                key.getBytes(StandardCharsets.UTF_8),
                Salt.generateSalt(),
                cpuMemoryCost,
                blockSize,
                parallelization,
                keyLength
        );
        return new String(result, StandardCharsets.UTF_8);
    }
}