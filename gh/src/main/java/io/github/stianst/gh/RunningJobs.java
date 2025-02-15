package io.github.stianst.gh;

import io.github.stianst.gh.utils.Client;
import org.kohsuke.github.GHWorkflowJob;
import org.kohsuke.github.GHWorkflowRun;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.PagedIterable;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class RunningJobs {

    public static void main(String[] args) throws IOException {

//        String repo = "cncf/keycloak-testing";
        String repo = "keycloak/keycloak";

        GitHub gitHub = Client.getInstance().gitHub();

        System.out.format("%-40s %-30s %-10s %-10s\n", "Date", "Runner", "Running", "Queued");

        while (true) {
            Map<String, Integer> queued = new HashMap<>();
            Map<String, Integer> running = new HashMap<>();

            PagedIterable<GHWorkflowRun> list = gitHub.getRepository(repo).queryWorkflowRuns().created(LocalDate.now().toString()).list();
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

            for (String r : runners) {
                System.out.format("%-40s %-30s %-10s %-10s\n", new Date(), r, running.get(r), queued.get(r));
            }

            try {
                Thread.sleep(TimeUnit.MINUTES.toMillis(1));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
