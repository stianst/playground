package io.github.stianst.gh;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

public class IssuesWithDuplicatedKindLabels {

    public static void main(String[] args) throws IOException {
        File file = new File("gh/all-issues.json");
        System.out.println(file.getAbsolutePath());
        ObjectMapper om = new ObjectMapper();
        JsonNode jsonNode = om.readValue(file, JsonNode.class);

        List<Issue> issues = new LinkedList<>();

        Iterator<JsonNode> elements = jsonNode.elements();
        while (elements.hasNext()) {
            JsonNode next = elements.next();
            String url = next.get("html_url").asText();
            String type = !next.get("type").isNull() ? next.get("type").get("name").asText() : null;
            JsonNode labels = next.get("labels");
            List<String> labelStrings = StreamSupport.stream(Spliterators.spliteratorUnknownSize(labels.elements(), Spliterator.ORDERED), false)
                    .map(n -> n.get("name").asText())
                    .filter(k -> k.startsWith("kind/"))
                    .filter(k -> !k.equals("kind/performance"))
                    .filter(k -> !k.equals("kind/weakness"))
                    .filter(k -> !k.equals("kind/cve"))
                    .toList();
            issues.add(new Issue(url, labelStrings, type));
        }

        System.out.println("Issues with multiple kind labels:");
        issues.stream().filter(i -> i.labels().size() > 1).forEach(i -> System.out.println(i.url() + " --> " + i.labels()));

        /* (labelStrings.size() > 1) {
//                System.out.println(url + " --> " + labelStrings);
        } else if (type != null) {
            if (labelStrings.get(0).equals("kind/" + type)) {
                System.out.println(url + " --> " + labelStrings + " == " + type);
            }
        }*/

        System.out.println("");
        System.out.println("Issues with miss-matching type and kind labels:");
        issues.stream().filter(i -> i.type() != null && i.labels().size() == 1 && !i.labels().get(0).equals("kind/" + i.type())).forEach(i -> System.out.println(i.url() + " --> " + i.type() + " != " + i.labels()));
    }

    private record Issue(String url, List<String> labels, String type) {}

}
