package org.keycloak.test.logparser;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TextReportGenerator implements ReportGenerator {

    public void printReport(List<GitHubRun> runs, List<FailedTest> failedTests) {
        printRunSummary(runs);
        System.out.println();
        printFailedTests(failedTests);
    }

    private void printRunSummary(List<GitHubRun> runs) {
        List<FailedRun> failedRuns = new LinkedList<>();
        for (GitHubRun run : runs) {
            for (GitHubRun.GitHubRunJob job : run.jobs()) {
                if (job.conclusion().equals("failure")) {
                    for (GitHubRun.GitHubRunJobStep step : job.steps()) {
                        if (step.conclusion().equals("failure")) {
                            String fullName = run.name() + " / " + job.name() + " / " + step.name();
                            FailedRun failedRun = failedRuns.stream().filter(f -> f.run().equals(fullName)).findFirst().or(() -> {
                                FailedRun fr = new FailedRun(fullName, new LinkedList<>());
                                failedRuns.add(fr);
                                return Optional.of(fr);
                            }).get();

                            failedRun.details().add(new FailedRun.FailedRunDetails(run.name(), run.databaseId(), job.name(), job.databaseId(), job.url()));
                        }
                    }
                }
            }
        }

        System.out.println("Failed steps:");
        for (FailedRun run : failedRuns) {
            System.out.println(run.details().size() + "\t" + run.run());
            for (FailedRun.FailedRunDetails details : run.details()) {
                System.out.println("\t\t - " + details.url());
            }
        }
    }

    public void printFailedTests(List<FailedTest> failedTests) {
        System.out.println("Failed tests:");
        for (FailedTest failedTest : failedTests) {
            System.out.println(failedTest.details().size() + "\t" + failedTest.test());
            for (FailedTest.FailedTestDetails details : failedTest.details()) {
                System.out.println("\t\t - " + details.url());
            }
        }
    }

}
