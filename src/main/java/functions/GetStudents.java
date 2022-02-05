package functions;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.google.gson.JsonParseException;
import config.FirestoreConfiguration;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class GetStudents implements HttpFunction {

    private static final Logger logger = Logger.getLogger(GetStudents.class.getName());

    @Override
    public void service(HttpRequest request, HttpResponse response)
            throws IOException {

        var writer = new PrintWriter(response.getWriter());

        //firestore instance
        FirestoreConfiguration fs= new FirestoreConfiguration();
        Firestore db = fs.getFireStoreService();
        String studentNumber= request.getFirstQueryParameter("studentNumber").orElse(null);

        try {
            //get all students
            if(studentNumber ==null) {
                ApiFuture<QuerySnapshot> students = db.collection("students").get();
                List<QueryDocumentSnapshot> documents = students.get().getDocuments();
                for (QueryDocumentSnapshot document : documents) {
                    writer.printf("%s => %s \n", document.getId(), document.getData());
                }
            }
            else {
                // if get request has no parameters then get all students (implemented above), and in other case get specific students
                ApiFuture<DocumentSnapshot> students = db.collection("students").document(studentNumber).get();
                if(students.get().getData() !=null) {
                    writer.printf("STUDENT: %s\n", students.get().getData());
                }
                else{
                    writer.printf("Student with student number %s not found",studentNumber);
                }
            }
            response.setStatusCode(HttpURLConnection.HTTP_OK);
        } catch (JsonParseException e) {
            logger.severe("Error parsing JSON: " + e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
