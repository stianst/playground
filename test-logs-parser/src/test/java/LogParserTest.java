import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;

public class LogParserTest {

    @Test
    public void testJava() {
        LogParser logParser = new LogParser();
        logParser.parse("java-example", LogParserTest.class.getResourceAsStream("java-example"));
        List<FailingTest> failingTests = logParser.getFailingTests();

        Assertions.assertEquals(1, failingTests.size());

        FailingTest failingTest = failingTests.get(0);

        Assertions.assertEquals("org.keycloak.testsuite.javascript.JavascriptAdapterTest#grantBrowserBasedApp", failingTest.getFailingTest());
        Assertions.assertEquals(1, failingTest.getFailures().size());

        FailingTest.Failure failure = failingTest.getFailures().get(0);
        Assertions.assertEquals("java-example", failure.getRun());
        Assertions.assertEquals("Base IT (5)", failure.getJob());
        Assertions.assertEquals("base tests", failure.getStep());
    }

    @Test
    public void testCypress() {
        LogParser logParser = new LogParser();
        logParser.parse("cypress-example", LogParserTest.class.getResourceAsStream("cypress-example"));
        List<FailingTest> failingTests = logParser.getFailingTests();

        failingTests.sort(Comparator.comparing(FailingTest::getFailingTest));

        Assertions.assertEquals(17, failingTests.size());

        FailingTest failingTest = failingTests.get(0);

        Assertions.assertEquals("authentication_test.spec.ts", failingTest.getFailingTest());
        Assertions.assertEquals(1, failingTest.getFailures().size());

        FailingTest.Failure failure = failingTest.getFailures().get(0);
        Assertions.assertEquals("cypress-example", failure.getRun());
        Assertions.assertEquals("Admin UI E2E (3, chrome)", failure.getJob());
        Assertions.assertEquals("Cypress", failure.getStep());
    }

}
