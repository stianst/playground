package io.github.stianst.gh.rep;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GHWorkflowRun(
    Long id,
    String name,
    String status,
    String conclusion,
    @JsonProperty("head_branch")
    String headBranch,
    @JsonProperty("created_at")
    Date createdAt,
    @JsonProperty("updated_at")
    Date updatedAt,
    String event,
    @JsonProperty("run_attempt")
    int runAttempt,
    String path) {
}
