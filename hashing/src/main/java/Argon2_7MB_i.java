import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;

import java.nio.charset.StandardCharsets;

public class Argon2_7MB_i implements Hash {

    private static final int hashLength = 32;
    private static final int parallelism = 1;
    private static final int memory = 7168;
    private static final int iterations = 5;

    @Override
    public String hash(String key) {
        Argon2Parameters.Builder builder = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_i)
                .withSalt(Salt.generateSalt())
                .withParallelism(parallelism)
                .withMemoryAsKB(memory)
                .withIterations(iterations)
                .withVersion(Argon2Parameters.ARGON2_VERSION_13);

        Argon2BytesGenerator argon2BytesGenerator = new Argon2BytesGenerator();
        argon2BytesGenerator.init(builder.build());

        byte[] result = new byte[hashLength];

        argon2BytesGenerator.generateBytes(key.getBytes(StandardCharsets.UTF_8), result);

        return new String(result, StandardCharsets.UTF_8);
    }

}
