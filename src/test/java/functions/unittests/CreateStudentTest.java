package functions.unittests;

import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.google.gson.Gson;
import functions.CreateStudent;
import functions.UpdateStudent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.*;
import java.util.Map;
import java.util.Optional;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

public class CreateStudentTest {
    @Mock
    private HttpRequest request;
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
    public void createStudent_Test() throws IOException {
        when(request.getContentType()).thenReturn(Optional.of("application/json"));
        String requestJson = gson.toJson(Map.of("lastName", "Unit",
                "firstName", "Testing",
                "studentNumber", "2004000",
                 "dob", "13.09.1986",
                 "email","testing.unit@stud.uni-bamberg.de",
                 "semester",3,
                 "degree", "M.Sc. International Software Systems Science",
                 "address","Unitstreet 50, 96058, Bamberg"));
        BufferedReader jsonReader = new BufferedReader(new StringReader(requestJson));

        when(request.getReader()).thenReturn(jsonReader);
        new CreateStudent().service(request, response);

        writerOut.flush();
        assertThat(responseOut.toString()).isEqualTo("Student Testing is added into the database");
    }

}
