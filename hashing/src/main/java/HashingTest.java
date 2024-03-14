import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

import java.util.List;

public class HashingTest {

    private static final int SALT_LENGTH = 16;

    public static void main(String[] args) {
        String password = "asfdsadfsadf3498hraisduf";

        List<Hash> hashes = List.of(new Pbkdf2(), new SCrypt(), new BCrypt(), new Argon2());

        // Make sure it's warmed-up (not sure this is needed, but hey why not)
        for (Hash hash : hashes) {
            hash.hash(password);
        }

        for (Hash hash : hashes) {
            long start = System.currentTimeMillis();
            for (int i = 0; i < 100; i++) {
                hash.hash(password);
            }
            System.out.println(hash.getClass().getSimpleName() + ":\t" + (System.currentTimeMillis() - start));
        }

    }

    public interface Hash {

        String hash(String key);

    }

    public static class Pbkdf2 implements Hash {

        private final Pbkdf2PasswordEncoder encoder;

        public Pbkdf2() {
            int iterations = 210000;
            Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm algorithm = Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA512;
            encoder = new Pbkdf2PasswordEncoder("", SALT_LENGTH, iterations, algorithm);
        }

        @Override
        public String hash(String key) {
            return encoder.encode(key);
        }
    }

    public static class SCrypt implements Hash {

        private final SCryptPasswordEncoder encoder;

        public SCrypt() {
            int cpuCost = (int) Math.round(Math.pow(2, 17));
            int memoryCost = 8;
            int parallelization = 1;
            int keyLength = 32;

            encoder = new SCryptPasswordEncoder(cpuCost, memoryCost, parallelization, keyLength, SALT_LENGTH);
        }

        @Override
        public String hash(String key) {
            return encoder.encode(key);
        }
    }

    public static class BCrypt implements Hash {

        private final BCryptPasswordEncoder encoder;

        public BCrypt() {
            int strenght = 10;

            encoder = new BCryptPasswordEncoder(strenght);
        }

        @Override
        public String hash(String key) {
            return encoder.encode(key);
        }
    }

    public static class Argon2 implements Hash {

        private final Argon2PasswordEncoder encoder;

        public Argon2() {
            int hashLength = 32;
            int parallelism = 1;
            int memory = 1 << 12;
            int iterations = 3;

            encoder = new Argon2PasswordEncoder(SALT_LENGTH, hashLength, parallelism, memory, iterations);
        }

        @Override
        public String hash(String key) {
            return encoder.encode(key);
        }
    }
}
