package functions;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import config.FirestoreConfiguration;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.util.logging.Logger;

public class DeleteStudent implements HttpFunction {
    private static final Logger logger = Logger.getLogger(DeleteStudent.class.getName());

    @Override
    public void service(HttpRequest request, HttpResponse response) throws Exception {
        var writer = new PrintWriter(response.getWriter());

        //firestore instance
        FirestoreConfiguration fs= new FirestoreConfiguration();
        Firestore db = fs.getFireStoreService();
        String studentNumber= request.getFirstQueryParameter("studentNumber").orElse(null);

        try {
            //get all students
            if (studentNumber == null) {
                logger.severe("Student number is not provided");
            } else {
                // asynchronously delete a document
                ApiFuture<WriteResult> writeResult = db.collection("students").document(studentNumber).delete();
                writer.printf("STUDENT DELETED");
                System.out.println("Update time : " + writeResult.get().getUpdateTime());
                response.setStatusCode(HttpURLConnection.HTTP_OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
