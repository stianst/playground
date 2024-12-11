import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.UUID;

public class SecureRandomTest {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        System.out.println(SecureRandom.getInstanceStrong().getAlgorithm());
//        System.out.println(UUID.randomUUID());
    }

}
