import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class LogParser {

    private static final Set<TestErrorParser> TEST_ERROR_PARSERS = Set.of(new JavaTestErrorParser(), new CypressTestErrorParser());

    private final List<FailingTest> failingTests = new LinkedList<>();

    public static void main(String[] args) throws IOException {
//        File source = new File(args[0]);
        File source = new File("/home/st/dev/playground/test-logs-parser/logs");
        String repository = args.length > 1 ? args[1] : null;

        LogParser logParser = new LogParser();
        if (source.isDirectory()) {
            for (File f : source.listFiles()) {
                logParser.parse(f.getName(), new FileInputStream(f));
            }
        } else {
            logParser.parse(source.getName(), new FileInputStream(source));
        }

        MarkdownReport markdownReport = new MarkdownReport(repository);
        markdownReport.print(logParser.getFailingTests());
    }

    public void parse(String run, InputStream inputStream) {
        List<LogEntry> logEntries = toLogEntries(inputStream);

        for (LogEntry logEntry : logEntries) {
            for (TestErrorParser testErrorParser : TEST_ERROR_PARSERS) {
                String failingTestString = testErrorParser.findFailingTest(logEntry.getText());
                if (failingTestString != null) {
                    FailingTest failingTest = failingTests.stream().filter(f -> f.getFailingTest().equals(failingTestString)).findFirst().orElseGet(() -> {
                        FailingTest t = new FailingTest(failingTestString);
                        failingTests.add(t);
                        return t;
                    });
                    failingTest.addFailure(run, logEntry.getJob(), logEntry.getStep());
                }
            }
        }
    }

    public List<FailingTest> getFailingTests() {
        return failingTests;
    }

    private List<LogEntry> toLogEntries(InputStream inputStream) {
        List<LogEntry> logEntries = new LinkedList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            for (String l = br.readLine(); l != null; l = br.readLine()) {
                LogEntry logEntry = LogEntry.parse(l);
                if (logEntry != null) {
                    logEntries.add(logEntry);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return logEntries;
    }

}
