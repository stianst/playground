package org.keycloak.test.logparser;

import java.util.List;

public record FailedTest(String test, List<FailedTestDetails> details) {

    public record FailedTestDetails(String runName, String runId, String jobName, String jobId, String url) {}

}
