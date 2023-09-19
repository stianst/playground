import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ClientCredential {

    public static final int COUNT = 1;

    public static final String TOKEN_ENDPOINT = "http://localhost:8080/realms/myrealm/protocol/openid-connect/token";

    public static final String CLIENT_ID = "myclient";

    public static final String CLIENT_SECRET = "mysecret";

    public static void main(String[] args) throws IOException {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            List<Long> times = new LinkedList<>();
            for (int i = 0; i < COUNT; i++) {
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("grant_type", "client_credentials"));
                params.add(new BasicNameValuePair("client_id", CLIENT_ID));
                params.add(new BasicNameValuePair("client_secret", CLIENT_SECRET));

                HttpPost httpPost = new HttpPost(TOKEN_ENDPOINT);


                httpPost.setEntity(new UrlEncodedFormEntity(params));

                long start = System.currentTimeMillis();

                CloseableHttpResponse response = httpclient.execute(httpPost);

                times.add(System.currentTimeMillis() - start);

                if (response.getCode() != 200) {
                    throw new RuntimeException("Invalid response code " + response.getCode());
                }
                if (!new String(response.getEntity().getContent().readAllBytes()).contains("access_token")) {
                    throw new RuntimeException("No access_token in response");
                }

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
