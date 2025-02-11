import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogEntry {

    private static final Pattern pattern = Pattern.compile("([^\\t]*)\\t([^\\t]*)\\t([^ ]*) (.*)");

    private final String job;
    private final String step;
    private final String time;
    private final String text;

    public static LogEntry parse(String line) {
        Matcher matcher = pattern.matcher(line);
        if (!matcher.matches() || matcher.groupCount() != 4) {
            return null;
        }

        String job = matcher.group(1);
        String step = matcher.group(2).substring(4);
        String time = matcher.group(3);
        String text = stripColorCodes(matcher.group(4)).trim();
        return new LogEntry(job, step, time, text);
    }

    private LogEntry(String job, String step, String time, String text) {
        this.job = job;
        this.step = step;
        this.time = time;
        this.text = text;
    }

    public String getJob() {
        return job;
    }

    public String getStep() {
        return step;
    }

    public String getTime() {
        return time;
    }

    public String getText() {
        return text;
    }

    private static String stripColorCodes(String l) {
        return l.replaceAll("\u001B\\[[;\\d]*m", "");
    }

}
