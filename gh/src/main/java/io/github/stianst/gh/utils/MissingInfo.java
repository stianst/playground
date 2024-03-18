package io.github.stianst.gh.utils;

import org.jboss.logging.Logger;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueComment;
import org.kohsuke.github.GHIssueStateReason;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.PagedIterator;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class MissingInfo {

    private static final Logger logger = Logger.getLogger(MissingInfo.class);

    private final LastChecked lastChecked = new LastChecked();


    public static void main(String[] args) throws IOException {
        new MissingInfo();
    }

    public MissingInfo() throws IOException {
    GHRepository repository = Client.getInstance().gitHub().getRepository("keycloak/keycloak");
        PagedIterator<GHIssue> missingInfoItr = repository.queryIssues().label("status/missing-information").label("status/auto-expire").list().iterator();



        while (missingInfoItr.hasNext()) {
            GHIssue issue = missingInfoItr.next();

            if (lastChecked.shouldCheck(issue)) {
                GHIssueComment lastBotComment = null;

                for (GHIssueComment c : issue.getComments()) {
                    if (c.getUser().getLogin().equals("keycloak-github-bot[bot]")) {
                        if (lastBotComment == null || lastBotComment.getUpdatedAt().before(c.getUpdatedAt())) {
                            lastBotComment = c;
                        }
                    }
                }

                if (lastBotComment == null) {
                    logger.warnv("Bot comment not found: issue={0}", issue.getNumber());
                } else {
                    lastChecked.checked(issue, lastBotComment);
                }
            }

                long lastBotComment = lastChecked.getLastBotComment(issue).getTime();
                long expires = lastBotComment + TimeUnit.DAYS.toMillis(13);

//                System.out.println(issue.getNumber());
//                System.out.println(new Date(lastBotComment));
//                System.out.println(new Date(expires));

                if (System.currentTimeMillis() > expires) {

                    logger.infov("Expired: issue={0}", issue.getNumber());
                }
        }

        lastChecked.clean();

    }



    public class LastChecked {

        Map<Integer, Date> lastBotComment = new HashMap<>();

        Set<Integer> visited = new HashSet<>();

        public boolean shouldCheck(GHIssue issue) {
            int issueNumber = issue.getNumber();
            visited.add(issueNumber);
            return !lastBotComment.containsKey(issueNumber);
        }

        public void checked(GHIssue issue, GHIssueComment issueLastBotComment) throws IOException {
            int issueNumber = issue.getNumber();
            visited.add(issueNumber);
            lastBotComment.put(issueNumber, issueLastBotComment.getUpdatedAt());
        }

        public void remove(GHIssue issue) {
            visited.remove(issue.getNumber());
            lastBotComment.remove(issue.getNumber());
        }

        public Date getLastBotComment(GHIssue issue) {
            return lastBotComment.get(issue.getNumber());
        }

        public void clean() {
            lastBotComment.keySet().removeIf(integer -> !visited.contains(integer));
            visited.clear();

            if (!lastBotComment.isEmpty()) {
                logger.infov("Monitoring: issues={0}", lastBotComment.keySet().stream().map(i -> Integer.toString(i)).collect(Collectors.joining(",")));
            }
        }

    }
}
