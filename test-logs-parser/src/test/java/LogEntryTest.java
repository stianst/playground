import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LogEntryTest {

    @Test
    public void logEntryTest() {
        LogEntry logEntry = LogEntry.parse("Status Check - Keycloak JavaScript CI\tRun /./.github/actions/status-check\t2025-01-14T09:44:29.3103507Z ##[error]Process completed with exit code 1.");
        Assertions.assertEquals("Status Check - Keycloak JavaScript CI", logEntry.getJob());
        Assertions.assertEquals("/./.github/actions/status-check", logEntry.getStep());
        Assertions.assertEquals("2025-01-14T09:44:29.3103507Z", logEntry.getTime());
        Assertions.assertEquals("##[error]Process completed with exit code 1.", logEntry.getText());
    }

    @Test
    public void logWithColor() {
        LogEntry logEntry = LogEntry.parse("Admin UI E2E (1, chrome)\tRun Cypress\t2025-01-14T08:59:53.6042377Z \u001B[0m\u001B[31m     AssertionError: Timed out retrying after 30000ms: Unable to find an element by: [data-testid=\"last-alert\"]");
        Assertions.assertEquals("AssertionError: Timed out retrying after 30000ms: Unable to find an element by: [data-testid=\"last-alert\"]", logEntry.getText());
    }

}
