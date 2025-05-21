package io.stianst.logparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BasicPlaywrightReport {

    public static void main(String[] args) throws IOException {
        File logs = new File("/home/st/dev/playground/test-logs-parser/logs");
        Map<String, Integer> failed = new HashMap<>();

        for (File log : logs.listFiles((dir, name) -> !name.equals(".json"))) {
            BufferedReader br = new BufferedReader(new FileReader(log));
            for (String l = br.readLine(); l != null; l = br.readLine()) {
                int i = l.indexOf("##[error]");
                if (i != -1 && l.indexOf('›') != -1) {
                    l = l.substring(i + 9);
                    l = l.substring(l.indexOf(')') + 2);

                    int c = failed.getOrDefault(l, 0);
                    failed.put(l, c + 1);
                }
            }

        }

        failed.forEach((k, v) -> System.out.println(k + "\t" + v));
    }

    private static String trimOutput(String output) {
        if (!output.startsWith("[")) {
            output = output.substring(output.indexOf('['));
        }
        return output.trim();
    }

}
