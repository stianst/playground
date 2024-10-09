package io.github.stianst.gh.rep;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GHWorkflowRuns(
    @JsonProperty("workflow_runs")
    List<GHWorkflowRun> workflowRuns) {
}