package io.github.stianst.gh;

import io.github.stianst.gh.rep.GHWorkflowRun;
import io.github.stianst.gh.rep.GHWorkflowRuns;
import io.github.stianst.gh.utils.GHCli;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class WorkflowRunTime {

    public static void main(String[] args) throws IOException, InterruptedException {
        GHCli ghCli = new GHCli();

        String status = "success";
        String branch = "main";
        String created = "18-09-2024";


//        List<GHWorkflowRuns> ghWorkflowRuns = ghCli.apiGet(GHWorkflowRuns.class, "repos/keycloak/keycloak/actions/workflows/ci.yml/runs", "--paginate", "-f", "status=" + status, "-f", "branch=" + branch, "-f", "created=" + created);
        List<GHWorkflowRuns> ghWorkflowRuns = ghCli.apiGet(GHWorkflowRuns.class, "repos/keycloak/keycloak/actions/workflows/ci.yml/runs", "--paginate", "-f", "created=>=2024-09-01", "-f", "event=workflow_dispatch");

        List<GHWorkflowRun> list = ghWorkflowRuns.stream().flatMap(r -> r.workflowRuns().stream()).toList();
        System.out.println(list.size());

        for (GHWorkflowRun r : list) {
            long time = TimeUnit.MINUTES.convert(r.updatedAt().getTime() - r.createdAt().getTime(), TimeUnit.MILLISECONDS);
            System.out.println(r.id() + " " + time);
        }

    }

}
