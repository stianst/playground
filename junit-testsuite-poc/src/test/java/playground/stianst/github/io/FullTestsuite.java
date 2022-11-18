package playground.stianst.github.io;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AdminTestsuite.class,
        OIDCTestsuite.class
})
public class FullTestsuite {
}
