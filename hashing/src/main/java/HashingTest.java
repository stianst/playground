import org.bouncycastle.crypto.params.Argon2Parameters;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class HashingTest {

    private static final int ITERATIONS = 1000;

    public static void main(String[] args) throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        String password = "asfdsadfsadf3498hraisduf";

        List<Hash> hashes = List.of(new Pbkdf2_SHA1_27K(), new Pbkdf2_SHA512_210K(), new SCrypt(), new BCrypt(), new Argon2_12MB_i(), new Argon2_7MB_i(), new MD5(), new SHA256());

        System.out.println("Processors: " + Runtime.getRuntime().availableProcessors());
        System.out.println();

        System.out.println(String.format("%1$-25s", "Algorithm") +
                String.format("%1$-12s", "Total (ms)") +
                String.format("%1$-12s", "Per-hash (ms)"));

        for (Hash hash : hashes) {
            ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            long start = System.currentTimeMillis();
            for (int i = 0; i < ITERATIONS; i++) {
                executor.execute(() -> hash.hash(password));
            }
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.MINUTES);

            long executionTime = System.currentTimeMillis() - start;
            double perHash = (double) executionTime / ITERATIONS;

            System.out.println(String.format("%1$-25s", hash.getClass().getSimpleName()) +
                    String.format("%1$-12s", executionTime) +
                    String.format("%1$-12s", perHash));
        }

    }

}
