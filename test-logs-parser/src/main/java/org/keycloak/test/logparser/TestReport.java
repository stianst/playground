package org.keycloak.test.logparser;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class TestReport {

    private static final ReportGenerator REPORT_GENERATOR = new TextReportGenerator();
    private static final List<LogParser> LOG_PARSERS = List.of(new SureFireLogParser());

    public static void main(String[] args) throws IOException {
        File logDirectory = new File("/home/st/dev/playground/test-logs-parser/logs");

        List<GitHubRun> runs = loadRuns(logDirectory);
        List<FailedTest> failedTests = loadFailedTests(logDirectory, runs);

        REPORT_GENERATOR.printReport(runs, failedTests);
    }

    private static List<GitHubRun> loadRuns(File logDirectory) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<GitHubRun> runs = new LinkedList<>();
        for (File logFile : logDirectory.listFiles(f -> f.getName().endsWith(".json"))) {
            runs.add(objectMapper.readValue(logFile, GitHubRun.class));
        }
        return runs;
    }

    private static List<FailedTest> loadFailedTests(File logDirectory, List<GitHubRun> runs) throws IOException {
        List<FailedTest> failedTests = new LinkedList<>();

        for (File logFile : logDirectory.listFiles(f -> !f.getName().endsWith(".json"))) {
            List<String> lines = Utils.readLines(logFile);
            Optional<LogParser> logParser = LOG_PARSERS.stream().filter(p -> p.supports(lines)).findFirst();

            if (logParser.isPresent()) {
                for (LogFailure logFailure : logParser.get().parseFailures(lines)) {
                    FailedTest failedTest = failedTests.stream().filter(f -> f.test().equals(logFailure.test())).findFirst().or(() -> {
                        FailedTest ft = new FailedTest(logFailure.test(), new LinkedList<>());
                        failedTests.add(ft);
                        return Optional.of(ft);
                    }).get();

                    String runId = logFile.getName();

                    GitHubRun run = runs.stream().filter(r -> r.databaseId().equals(runId)).findFirst().get();
                    GitHubRun.GitHubRunJob job = run.jobs().stream().filter(j -> j.name().equals(logFailure.job())).findFirst().get();

                    failedTest.details().add(new FailedTest.FailedTestDetails(run.name(), run.databaseId(), job.name(), job.databaseId(), job.url()));
                }
            }
        }

        return failedTests;
    }

}
