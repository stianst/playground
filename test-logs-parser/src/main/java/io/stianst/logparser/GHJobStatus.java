package io.stianst.logparser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GHJobStatus(String databaseId, String name, String conclusion, List<GHJob> jobs) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record GHJob(String name, String conclusion, String databaseId, String url, List<GHStep> steps) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record GHStep(String name, String conclusion) {
    }

}
