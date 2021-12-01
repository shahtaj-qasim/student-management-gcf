package functions;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;

public class MainFunction implements HttpFunction {
    @Override
    public void service(HttpRequest request, HttpResponse response) throws Exception {
        switch (request.getMethod()) {
            case "GET":
                GetStudents getStudents= new GetStudents();
                getStudents.service(request,response);
                break;
            case "POST":
                CreateStudent createStudent= new CreateStudent();
                createStudent.service(request,response);
                break;
            case "PUT":
                UpdateStudent updateStudent= new UpdateStudent();
                updateStudent.service(request,response);
                break;
            case "DEL":
                DeleteStudent deleteStudent = new DeleteStudent();
                deleteStudent.service(request,response);
                break;
            default:
                break;
        }
    }
}
