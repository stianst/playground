public class CypressTestErrorParser implements TestErrorParser {

    @Override
    public String findFailingTest(String l) {
        if (l.contains("✖")) {
            if (!l.matches(".*[\\d]* of [\\d]* failed .*")) {
                String s = l.split("✖")[1].trim().split(" ")[0];
                return s;
            }
        }
        return null;
    }

}
