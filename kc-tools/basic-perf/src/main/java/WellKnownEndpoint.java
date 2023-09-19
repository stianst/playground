import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class WellKnownEndpoint {

    public static final int COUNT = 10000;

    public static final String WELLKNOWN_ENDPOINT = "http://localhost:8080/realms/master/.well-known/openid-configuration";

    public static void main(String[] args) throws IOException {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            List<Long> times = new LinkedList<>();
            for (int i = 0; i < COUNT; i++) {
                HttpGet httpGet = new HttpGet(WELLKNOWN_ENDPOINT);

                long start = System.currentTimeMillis();

                CloseableHttpResponse response = httpclient.execute(httpGet);
                assert response.getCode() == 200;

                times.add(System.currentTimeMillis() - start);

                if (i % 100 == 0) {
                    System.out.println(i);
                }

                response.close();
            }

            Long total = times.stream().reduce(0l, (a, b) -> a + b);

            double average = times.stream().mapToDouble(d -> d).average().getAsDouble();

            System.out.println();
            System.out.println("Total: " + total + " ms");
            System.out.println("Average: " + average + " ms");
        }

    }

}
