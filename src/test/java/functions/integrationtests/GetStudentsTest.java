package functions.integrationtests;

import static com.google.common.truth.Truth.assertThat;

//import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.retry.IntervalFunction;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

public class GetStudentsTest {
    // Each function must be assigned a unique port to run on.
    // Otherwise, tests can flake when 2+ functions run simultaneously.
    // This is also specified in the `function-maven-plugin` config in `pom.xml`.
    private static final int PORT = 8081;

    // Root URL pointing to the locally hosted function
    // The Functions Framework Maven plugin lets us run a function locally
    private static final String BASE_URL = "http://localhost:" + PORT;

    private static Process emulatorProcess = null;
    private static HttpClient client = HttpClient.newHttpClient();

    @BeforeClass
    public static void setUp() throws IOException {
        // Get the sample's base directory (the one containing a pom.xml file)
        String baseDir = System.getProperty("user.dir");

        // Emulate the function locally by running the Functions Framework Maven plugin
        emulatorProcess = new ProcessBuilder()
                .command("gradlew", "function:run")
                .directory(new File(baseDir))
                .start();
    }

    @AfterClass
    public static void tearDown() throws IOException {
        // Display the output of the plugin process
        InputStream stdoutStream = emulatorProcess.getInputStream();
        ByteArrayOutputStream stdoutBytes = new ByteArrayOutputStream();
        stdoutBytes.write(stdoutStream.readNBytes(stdoutStream.available()));
        System.out.println(stdoutBytes.toString(StandardCharsets.UTF_8));

        // Terminate the running Functions Framework Maven plugin process
        if (emulatorProcess.isAlive()) {
            emulatorProcess.destroy();
        }
    }

    @Test
    public void helloHttp_Test() throws Throwable {
        String functionUrl = BASE_URL + "/GetStudents/?studentNumber=2014848";

        HttpRequest getRequest = HttpRequest.newBuilder().uri(URI.create(functionUrl)).GET().build();

        // The Functions Framework Maven plugin process takes time to start up
        // Use resilience4j to retry the test HTTP request until the plugin responds
        // See `retryOnResultPredicate` here: https://resilience4j.readme.io/docs/retry
        RetryRegistry registry = RetryRegistry.of(RetryConfig.custom()
                .maxAttempts(12)
                .intervalFunction(IntervalFunction.ofExponentialBackoff(200, 2))
                .retryExceptions(IOException.class)
                .retryOnResult(body -> body.toString().length() == 0)
                .build());
        Retry retry = registry.retry("my");

        // Perform the request-retry process
        String body = Retry.decorateCheckedSupplier(retry, () -> client.send(
                getRequest,
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)).body()
        ).apply();

        // Verify the function returned the right results
        assertThat(body).isEqualTo("Hello world!");
    }
}
