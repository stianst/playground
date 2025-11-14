package playground.stianst.github.io;

import java.io.ByteArrayOutputStream;
import java.security.KeyPairGenerator;
import java.security.spec.NamedParameterSpec;
import java.util.List;

public class GeneratePrefixForAPK {

    public static void main(String[] args) throws Exception {
        for (String algorithm : List.of("ML-DSA-44", "ML-DSA-65", "ML-DSA-87")) {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
            keyPairGenerator.initialize(new NamedParameterSpec(algorithm));

            byte[] bytes1 = keyPairGenerator.generateKeyPair().getPublic().getEncoded();
            byte[] bytes2 = keyPairGenerator.generateKeyPair().getPublic().getEncoded();
            byte[] bytes3 = keyPairGenerator.generateKeyPair().getPublic().getEncoded();

            byte[] prefix1 = findMatch(bytes1, bytes2);
            byte[] prefix2 = findMatch(bytes1, bytes3);

            System.out.println(algorithm);
            print(prefix1);
            print(prefix2);
        }
    }

    private static void print(byte[] b) {
        System.out.print("new byte[] { ");
        for (int i = 0; i < b.length; i++) {
            System.out.print(b[i] + ", ");
        }
        System.out.println("}");
    }

    private static byte[] findMatch(byte[] a, byte[] b) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        for (int i = 0; i < a.length && a[i] == b[i]; i++) {
            bos.write(a[i]);
        }
        return bos.toByteArray();
    }

}
