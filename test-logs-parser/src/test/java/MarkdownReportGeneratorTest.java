import org.junit.jupiter.api.Test;

import java.util.List;

public class MarkdownReportGeneratorTest {

    @Test
    public void testMarkdownReport() {
        LogParser logParser = new LogParser();
        logParser.parse("cypress-example", LogParserTest.class.getResourceAsStream("cypress-example"));
        logParser.parse("java-example", LogParserTest.class.getResourceAsStream("java-example"));
        List<FailingTest> failingTests = logParser.getFailingTests();

        MarkdownReport markdownReport = new MarkdownReport(null);
        markdownReport.print(failingTests);
    }

}
