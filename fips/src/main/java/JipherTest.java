import com.oracle.jipher.provider.JipherJCE;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;

public class JipherTest {

    public static void main(String[] args) throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException {
        Security.insertProviderAt(new JipherJCE(), 0);

        Cipher.getInstance("AES", "JipherJCE");
    }

}
