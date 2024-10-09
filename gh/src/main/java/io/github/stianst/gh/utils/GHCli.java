package io.github.stianst.gh.utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GHCli {

    private final JsonFactory jsonFactory = new JsonFactory();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public <T> List<T> apiGet(Class<T> clazz, String endpoint, String... args) throws IOException, InterruptedException {
        List<String> cmdarray = new LinkedList<>();
        cmdarray.add("gh");
        cmdarray.add("api");
        cmdarray.add("-X");
        cmdarray.add("GET");
        cmdarray.add("--cache");
        cmdarray.add("1h");
        cmdarray.add(endpoint.startsWith("repos/") ? endpoint : ("repos/keycloak/keycloak/" + endpoint));
        cmdarray.addAll(Arrays.asList(args));

        File output = new File("ghcli-out.log");

        ProcessBuilder pb = new ProcessBuilder(cmdarray);
        pb.environment().put("GH_DEBUG", "1");
        pb.redirectError(new File("ghcli-err.log"));
        pb.redirectOutput(output);

//        System.out.println(String.join(" ", cmdarray));

        Process process = pb.start();
        if (!process.waitFor(30, TimeUnit.SECONDS)) {
            throw new IOException("Timed out, see ghcli-err.log for details");
        }

        if (process.exitValue() != 0) {
            throw new IOException("Returned with " + process.exitValue() + ", see ghcli-err.log for details");
        }

        // gh api --paginate returns multiple json documents
        JsonParser parser = jsonFactory.createParser(output);
        parser.setCodec(objectMapper);
        parser.nextToken();
        List<T> list = new LinkedList<>();
        while (parser.hasCurrentToken()) {
            list.add(parser.readValueAs(clazz));
            parser.nextToken();
        }
        return list;
    }

}
