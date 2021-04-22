import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;

public class LogParser {

    public static void main(String[] args) throws Exception {
        BufferedReader r = new BufferedReader(new FileReader("/home/st/tmp/kc2.log"));

        List<List<String>> entries = new LinkedList<>();
        List e = null;
        for (String s = r.readLine(); s != null; s = r.readLine()) {
            if (s.matches(".*[0-9][0-9]:[0-9][0-9].*\\[.*\\].*")) {
                e = new LinkedList();
                e.add(s);

                if (s.contains("org.keycloak.services.error.KeycloakErrorHandler")) {
                    entries.add(e);
                }
            } else {
                if (e != null) {
                    if (s.contains("org.keycloak")) {
                        e.add(s);
                    }
                }
            }
        }

        for (List<String> s : entries) {
            for (String l : s) {
                System.out.println(l);
            }
            System.out.println();
        }
    }

}
