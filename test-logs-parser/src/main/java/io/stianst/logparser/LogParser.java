package io.stianst.logparser;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LogParser {

    public static void main(String[] args) throws IOException {
        File source = new File("/home/st/dev/playground/test-logs-parser/logs");
        ObjectMapper objectMapper = new ObjectMapper();

        File[] files = source.isDirectory() ? source.listFiles((dir, name) -> name.endsWith(".json")) : new File[] { source };

        Map<String, Integer> failedJobs = new HashMap<>();

        for (File f : files) {
            GHJobStatus ghJobStatus = objectMapper.readValue(f, GHJobStatus.class);

            for (GHJobStatus.GHJob job : ghJobStatus.jobs()) {
                if (!"success".equals(job.conclusion())) {

                    failedJobs.put(job.name(), failedJobs.getOrDefault(job.name(), 0) + 1);

                    if (job.name().contains("chromium")) {
                        System.out.println(job.url());
                    }
                }
            }

//            File jobLog = new File(f.getParentFile(), f.getName().replace(".json", ""));
//
//            BufferedReader br = new BufferedReader(new FileReader(jobLog));
//            for (String l = br.readLine(); l != null; l = br.readLine()) {
//                int t1 = l.indexOf('\t');
//                int t2 = l.indexOf('\t', t1 + 1);
//
//                String name = l.substring(0, t1);
//                String step = l.substring(t1 + 1, t2);
//                String output = l.substring(t2 + 1);
//
//                System.out.println("\"" + jobLog.getName() + "\"");
//                System.out.println("\"" + name + "\"");
//                System.out.println("\"" + step + "\"");
//                System.out.println("\"" + output + "\"");
//            }
        }

        failedJobs.forEach((k, v) -> System.out.println(k + " \t " + v));
    }

}
