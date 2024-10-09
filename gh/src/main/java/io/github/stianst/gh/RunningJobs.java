package io.github.stianst.gh;

import io.github.stianst.gh.utils.Client;
import org.kohsuke.github.GHWorkflowJob;
import org.kohsuke.github.GHWorkflowRun;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.PagedIterable;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class RunningJobs {

    public static void main(String[] args) throws IOException {

            Map<String, Integer> queued = new HashMap<>();
            Map<String, Integer> running = new HashMap<>();

            String repo = "cncf/keycloak-testing";
//        String repo = "keycloak/keycloak";

            GitHub gitHub = Client.getInstance().gitHub();
            PagedIterable<GHWorkflowRun> list = gitHub.getRepository(repo).queryWorkflowRuns().created(">=2024-09-20").list();
            for (GHWorkflowRun r : list) {
                if (r.getStatus().equals(GHWorkflowRun.Status.IN_PROGRESS) || r.getStatus().equals(GHWorkflowRun.Status.QUEUED)) {
                    for (GHWorkflowJob j : r.listJobs()) {
                        String label = !j.getLabels().isEmpty() ? j.getLabels().get(0) : null;
                        if (label != null) {
                            if (!queued.containsKey(label)) {
                                queued.put(label, 0);
                            }
                            if (!running.containsKey(label)) {
                                running.put(label, 0);
                            }
                        }

                        if (j.getStatus().equals(GHWorkflowRun.Status.QUEUED)) {
                            queued.put(label, queued.get(label) + 1);
                        } else if (j.getStatus().equals(GHWorkflowRun.Status.IN_PROGRESS)) {
                            running.put(label, running.get(label) + 1);
                        }
                    }
                }
            }

            Set<String> runners = new HashSet<>();
            runners.addAll(queued.keySet());
            runners.addAll(running.keySet());

            System.out.format("%-30s %-10s %-10s\n", "Runner", "Running", "Queued");
            for (String r : runners) {
                System.out.format("%-30s %-10s %-10s\n", r, running.get(r), queued.get(r));
            }
            System.out.println();

            try {
                Thread.sleep(TimeUnit.MINUTES.toMillis(5));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
    }

}
