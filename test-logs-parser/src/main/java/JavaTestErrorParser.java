public class JavaTestErrorParser implements TestErrorParser {

    @Override
    public String findFailingTest(String l) {
        if (l.contains("[ERROR]") && l.contains("<<<") && (l.endsWith("FAILURE!") || l.endsWith("ERROR!"))) {
            l = l.split("\\[ERROR]")[1].trim().split(" ")[0];
            int lastDot = l.lastIndexOf('.');
            l = l.substring(0, lastDot) + "#" + l.substring(lastDot + 1);
            return l;
        }
        return null;
    }

}
