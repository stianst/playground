package io.github.stianst.gh;

import io.github.stianst.gh.utils.Client;

import javax.json.JsonObject;
import javax.json.JsonValue;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

public class GetIssuesLinkedToPrs {

    static final int PAGE_SIZE = 100;

    public static void main(String[] args) throws Exception {
        try(Client client = Client.getInstance()) {
            String query = new String(GetIssuesLinkedToPrs.class.getResourceAsStream("/issues-linked-to-prs").readAllBytes(), StandardCharsets.UTF_8);
            query = query.replace("@REPO@", "stianst/keycloak-bot-testing");

            boolean hasNextPage = true;
            String endCursor = null;
            while (hasNextPage) {
                String page;
                if (endCursor != null) {
                    page = "first:" + PAGE_SIZE + ", after:\"" + endCursor + "\"";
                } else {
                    page = "first:" + PAGE_SIZE;
                }

                System.out.println(page);

                JsonObject data = client.graphQL().executeSync(query.replace("@PAGE@", page)).getData();
                processResults(data);

                JsonObject pageInfo = data.getJsonObject("search").getJsonObject("pageInfo");
                hasNextPage = pageInfo.getBoolean("hasNextPage");
                endCursor = pageInfo.getString("endCursor");
            }
        }
    }

    private static void processResults(JsonObject data) {
        Iterator<JsonValue> prItr = data.getJsonObject("search").getJsonArray("edges").iterator();
        while (prItr.hasNext()) {
            JsonObject pr = prItr.next().asJsonObject().getJsonObject("node");

            int prNumber = pr.getInt("number");
            System.out.println("PR: " + prNumber);

            if (pr.containsKey("closingIssuesReferences")) {
                Iterator<JsonValue> issueItr = pr.getJsonObject("closingIssuesReferences").getJsonArray("edges").iterator();
                while (issueItr.hasNext()) {
                    JsonObject issue = issueItr.next().asJsonObject().getJsonObject("node");
                    int issueNumber = issue.getInt("number");

                    Set<String> labels = issue.getJsonObject("labels").getJsonArray("edges").stream().map(o -> o.asJsonObject().getJsonObject("node").getString("name")).collect(Collectors.toSet());

                    System.out.println(" --> " + issueNumber + " " + String.join(" ", labels));
                }
            }
        }
    }

}
