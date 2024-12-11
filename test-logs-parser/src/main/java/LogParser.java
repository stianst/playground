import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LogParser {

    private static Pattern testNamePattern = Pattern.compile("[a-z0-9_.]*\\.ts");

    private Map<String, Map<String, List<String>>> failingTests = new HashMap<>();

    public static void main(String[] args) throws IOException {
        LogParser logParser = new LogParser();
        logParser.parse(new File("/home/st/dev/playground/js-test-stability/clustering-logs"));
        logParser.printFailingTests();
    }

    public void parse(File file) throws IOException {

        File[] files = file.isFile() ? new File[]{file} : file.listFiles();

        for (File f : files) {
            BufferedReader br = new BufferedReader(new FileReader(f, StandardCharsets.UTF_8));

            for (String l = br.readLine(); l != null; l = br.readLine()) {
                String runId = f.getName();
                String job = l.substring(0, l.indexOf('\t'));

                String failingTest = null;
                if (job.startsWith("Admin UI E2E")) {
                    failingTest = parseAdminUI(l);
                } else if (job.startsWith("Base IT")) {
                    failingTest = parseJava(l);
                } else if (job.startsWith("Clustering IT")) {
                    failingTest = parseJava(l);
                }

                if (failingTest != null) {
                    Map<String, List<String>> jobEntry = failingTests.computeIfAbsent(job, i -> new HashMap<>());
                    List<String> testEntry = jobEntry.computeIfAbsent(failingTest, i -> new LinkedList<>());
                    testEntry.add(runId);
                }
            }

//            for (String l = br.readLine(); l != null; l = br.readLine()) {
//                if (l.startsWith("Admin UI E2E")) {
//                    parseAdminUI(l);
//                }
//                l = stripColorCodes(l);
//                if (l.contains("✖")) {
//
//
//
//                    Matcher matcher = testNamePattern.matcher(l);
//                    if (matcher.find()) {
//                        String testname = matcher.group(0);
//                        failingTests.computeIfAbsent(testname, s -> new HashSet<>()).add(f.getName());
//                    }
//                }
//            }
        }
    }

    public void printFailingTests() {
        System.out.println("|Job|Failing test|Failures|Jobs|");
        System.out.println("|---|------------|--------|----|");
        for (Map.Entry<String, Map<String, List<String>>> jobEntry : failingTests.entrySet()) {
            String job = jobEntry.getKey();
            for (Map.Entry<String, List<String>> testEntry : jobEntry.getValue().entrySet()) {
                String failingTest = testEntry.getKey();

                System.out.println("|" + job + "|" + failingTest + "|" + testEntry.getValue().size() + "|" + testEntry.getValue().stream().map(m -> "[" + m + "](https://github.com/stianst/keycloak/actions/runs/" + m + ")").collect(Collectors.joining(" ")) + "|");
            }
        }
    }

    private String parseAdminUI(String l) {
        l = stripColorCodes(l);
        if (l.contains("✖")) {
            Matcher matcher = testNamePattern.matcher(l);
            if (matcher.find()) {
                return matcher.group(0);
            }
        }
        return null;
    }

    private String parseJava(String l) {
        if (l.contains("[ERROR]") && l.contains("<<<") && (l.endsWith("FAILURE!") || l.endsWith("ERROR!"))) {
            l = l.split("\\[ERROR]")[1].trim().split(" ")[0].replace("org.keycloak.testsuite.", "");
            return l;
        }
        return null;
    }

    private String stripColorCodes(String l) {
        return l.replaceAll("\u001B\\[[;\\d]*m", "");
    }

}
