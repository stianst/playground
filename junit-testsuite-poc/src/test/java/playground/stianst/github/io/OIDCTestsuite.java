package playground.stianst.github.io;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import playground.stianst.github.io.oidc.LoginTest;
import playground.stianst.github.io.oidc.TokenTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        LoginTest.class,
        TokenTest.class
})
public class OIDCTestsuite {
}
