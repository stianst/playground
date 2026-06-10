package io.github.stianst.gh;

import io.github.stianst.gh.utils.Client;
import org.kohsuke.github.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class StoreModelTimeOuts {

    public static void main(String[] args) throws IOException {
        GitHub gitHub = Client.getInstance().gitHub();

        PagedIterable<GHWorkflowRun> list = gitHub.getRepository("keycloak/keycloak").queryWorkflowRuns().event(GHEvent.PUSH).conclusion(GHWorkflowRun.Conclusion.CANCELLED).created(">2026-03-01").list();
        PagedIterator<GHWorkflowRun> iterator = list.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            count++;

            GHWorkflowRun next = iterator.next();

            GHWorkflowJob storeModelJob = null;
            int cancelled = 0;
            for (GHWorkflowJob job : next.listJobs()) {
                if (job.getConclusion().equals(GHWorkflowRun.Conclusion.CANCELLED)) {
                    cancelled++;
                    if (job.getName().equals("Store Model Tests")) {
                        storeModelJob = job;
                    }
                }
            }

            if (cancelled == 1 && storeModelJob != null) {
                storeModelJob.downloadLogs(input -> {
                    System.out.println("");
                    System.out.println("Store timeout:  " + next.getHtmlUrl() + ", branch " + next.getHeadBranch());
                    System.out.println("");
                    return true;
                });
            } else {
                System.out.print(" " + next.getId() + "(" + cancelled + "," + (storeModelJob != null) + ")");
            }

            if (count % 10 == 0) {
                System.out.println("");
            }

//            System.exit(1);
        }
    }

    }
