import java.util.LinkedList;
import java.util.List;

public class FailingTest {

    private final String failingTest;
    private final List<Failure> failures = new LinkedList<>();

    public FailingTest(String failingTest) {
        this.failingTest = failingTest;
    }

    public String getFailingTest() {
        return failingTest;
    }

    public void addFailure(String run, String job, String step) {
        failures.add(new Failure(run, job, step));
    }

    public List<Failure> getFailures() {
        return failures;
    }

    public static final class Failure {

        private String run;
        private String job;
        private String step;

        public Failure(String run, String job, String step) {
            this.run = run;
            this.job = job;
            this.step = step;
        }

        public String getRun() {
            return run;
        }

        public String getJob() {
            return job;
        }

        public String getStep() {
            return step;
        }
    }

}
