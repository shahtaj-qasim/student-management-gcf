package functions.unittests;

import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.google.gson.Gson;
import functions.GetStudents;
import functions.UpdateStudent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.*;
import java.util.Map;
import java.util.Optional;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class UpdateStudentTest {
    @Mock private HttpRequest request;
    @Mock private HttpResponse response;

    private BufferedWriter writerOut;
    private StringWriter responseOut;
    private static final Gson gson = new Gson();

    @Before
    public void beforeTest() throws IOException {
        MockitoAnnotations.initMocks(this);

        // use an empty string as the default request content
        BufferedReader reader = new BufferedReader(new StringReader(""));
        when(request.getReader()).thenReturn(reader);

        responseOut = new StringWriter();
        writerOut = new BufferedWriter(responseOut);
        when(response.getWriter()).thenReturn(writerOut);
    }

    @Test
    public void updateStudent_Test() throws IOException {
        when(request.getContentType()).thenReturn(Optional.of("application/json"));
        when(request.getFirstQueryParameter("studentNumber")).thenReturn(Optional.of("2004000"));
        String requestJson = gson.toJson(Map.of("lastName", "TestUnit"));
        BufferedReader jsonReader = new BufferedReader(new StringReader(requestJson));

        when(request.getReader()).thenReturn(jsonReader);
        new UpdateStudent().service(request, response);

        writerOut.flush();
        assertThat(responseOut.toString()).isEqualTo("STUDENT UPDATED");
    }

}
