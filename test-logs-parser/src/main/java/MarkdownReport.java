import java.util.List;

public class MarkdownReport {

    private final String repository;

    public MarkdownReport(String repository) {
        this.repository = repository;
    }

    public void print(List<FailingTest> failingTests) {
        boolean first = true;
        for (FailingTest failingTest : failingTests) {
            if (!first) {
                System.out.println();
            }
            System.out.println("### " + failingTest.getFailingTest());
            System.out.println("|Run|Job|Step|");
            System.out.println("|------------|--------|----|");

            for (FailingTest.Failure failure : failingTest.getFailures()) {
                if (repository != null) {
                    System.out.print("|[" + failure.getRun() + "](https://github.com/" + repository + "/actions/runs/" + failure.getRun() + ")");
                } else {
                    System.out.print("|" + failure.getRun());
                }
                System.out.print("|" + failure.getJob());
                System.out.print("|" + failure.getStep());
                System.out.println("|");
            }
            first = false;
        }
    }

}
