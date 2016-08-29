package carec2.camel.processors;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class RestProcessor {

    private String message;

    public void process(String type, String id) throws Exception {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Date d = new Date();

        String date = dateFormat.format(d);
        String time = timeFormat.format(d);

//        HttpClient client = new DefaultHttpClient();
//        HttpPost post = new HttpPost("http://localhost:8080/carec2/notify");
//
//        List nameValuePairs = new ArrayList();
//        nameValuePairs.add(new BasicNameValuePair("eventType", "update"));
//        nameValuePairs.add(new BasicNameValuePair("eventDate", d));
//        nameValuePairs.add(new BasicNameValuePair("eventTime", t));
//        nameValuePairs.add(new BasicNameValuePair("objectType", "patient"));
//        nameValuePairs.add(new BasicNameValuePair("objectId", "9011"));
//
//        post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//        HttpResponse response = client.execute(post);
//
//        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//        String resp = "";
//        String line = "";

        HttpClient httpClient = HttpClientBuilder.create().build(); //Use this instead
        HttpResponse response = null;

        try {
            HttpPost request = new HttpPost("http://localhost:8181/carec2/notify");
            StringEntity params =new StringEntity("{\n" +
                    "    \"eventType\":    \"change\",\n" +
                    "    \"eventDate\":    \"" + date + "\",\n" +
                    "    \"eventTime\":    \"" + time + "\",\n" +
                    "    \"objectType\":   \"" + type +"\",\n" +
                    "    \"objectId\":     \"" + id + "\"" +
                    "}");
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            response = httpClient.execute(request);

            // handle response here...
        }catch (Exception ex) {
            System.out.println(ex);
        }

 //       System.out.println(new BasicResponseHandler().handleResponse(response));
    }
}
