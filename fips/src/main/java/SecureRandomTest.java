import org.bouncycastle.crypto.CryptoServicesRegistrar;
import org.bouncycastle.crypto.fips.FipsDRBG;
import org.bouncycastle.crypto.fips.FipsSecureRandom;
import org.bouncycastle.crypto.util.BasicEntropySourceProvider;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;

public class SecureRandomTest {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        FipsSecureRandom build = FipsDRBG.SHA512_HMAC.fromEntropySource(
                        new BasicEntropySourceProvider(new SecureRandom(), true))
                .build(null, true);

        System.out.println("FIPS " + build);

        CryptoServicesRegistrar.setSecureRandom(build);

        System.out.println(CryptoServicesRegistrar.getSecureRandom());


        System.out.println(new SecureRandom().getProvider());


//
//        try {
//            System.out.println(CryptoServicesRegistrar.getSecureRandom().getAlgorithm());
//            System.out.println(CryptoServicesRegistrar.getSecureRandom().getProvider());
//        } catch (Throwable t) {
//            System.out.println(t.getMessage());
//        }
//        try {
//            System.out.println("SecureRandom strong: " + SecureRandom.getInstanceStrong().getAlgorithm());
//        } catch (Throwable t) {
//            System.out.println(t.getMessage());
//        }
//
//        System.out.println("new SecureRandom()");
//        System.out.println(new SecureRandom().getProvider());
//        System.out.println(new SecureRandom().getAlgorithm());
//
//        try {
//            System.out.println(UUID.randomUUID());
//        } catch (Throwable t) {
//            System.out.println(t.getMessage());
//        }
    }

}
