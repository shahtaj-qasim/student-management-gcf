package functions.systemtests;

import static com.google.common.truth.Truth.assertThat;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class GetStudentsTest {
    // Root URL pointing to your Cloud Functions deployment
    // TODO<developer>: set this value, as an environment variable or within your test code
    private static final String BASE_URL = "https://us-central1-dsg-thesis-test.cloudfunctions.net";

    // Identity token used to send requests to authenticated-only functions
    // TODO<developer>: Set this value if your function requires authentication.
    //                  See the documentation for more info:
    // https://cloud.google.com/functions/docs/securing/authenticating
    private static final String IDENTITY_TOKEN = System.getenv("FUNCTIONS_IDENTITY_TOKEN");

    // Name of the deployed function
    // TODO<developer>: Set this to HelloHttp, as an environment variable or within your test code
    private static final String FUNCTION_DEPLOYED_NAME = "GetStudents";

    private static final String PARAMETER = "studentNumber=2004000";

    private static HttpClient client = HttpClient.newHttpClient();

    @Test
    public void getStudent_Test() throws IOException, InterruptedException {
        String functionUrl = BASE_URL + "/" + FUNCTION_DEPLOYED_NAME+"/?"+PARAMETER;

        HttpRequest.Builder getRequestBuilder = java.net.http.HttpRequest.newBuilder()
                .uri(URI.create(functionUrl))
                .GET();

        // Used to test functions that require authenticated invokers
        if (IDENTITY_TOKEN != null) {
            getRequestBuilder.header("Authorization", "Bearer " + IDENTITY_TOKEN);
        }

        java.net.http.HttpRequest getRequest = getRequestBuilder.build();

        HttpResponse response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        String responseStr= response.body().toString().substring(10,42); //get firstname and lastname

        assertThat(response.statusCode()).isEqualTo(HttpURLConnection.HTTP_OK);
        assertThat(responseStr).isEqualTo("lastName=Unit, firstName=Testing");
    }
}