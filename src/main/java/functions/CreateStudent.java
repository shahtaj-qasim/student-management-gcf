package functions;

import config.FirestoreConfiguration;
import models.Student;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.WriteResult;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.google.gson.*;
import com.google.cloud.firestore.Firestore;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;
public class CreateStudent implements HttpFunction {
    private static final Logger logger = Logger.getLogger(CreateStudent.class.getName());

    private static final Gson gson = new Gson();

    @Override
    public void service(HttpRequest request, HttpResponse response)
            throws IOException {
        // Check URL parameters for "name" field
        // "world" is the default value
        //String name = request.getFirstQueryParameter("name").orElse("world");
        String name = null;
        int studentNumber = 0;
        String contentType = request.getContentType().orElse("");
        var writer = new PrintWriter(response.getWriter());

        //firestore instance
        FirestoreConfiguration fs= new FirestoreConfiguration();
        Firestore db = fs.getFireStoreService();

        // Parse JSON request and get the data
        try {
            switch (contentType) {
                case "application/json":
                    JsonReader reader = new JsonReader(request.getReader());
                    JsonObject requestJson = null;
                    JsonElement requestParsed = gson.fromJson(reader, JsonElement.class);
                    if (requestParsed != null && requestParsed.isJsonObject()) {
                        requestJson = requestParsed.getAsJsonObject();
                    }

                    if (requestJson != null && requestJson.has("firstName")) {
                        name = requestJson.get("firstName").getAsString();
                        studentNumber = requestJson.get("studentNumber").getAsInt();
                    }

                    if(studentNumber ==0){
                        writer.printf("Student number is not provided. It is a required field.");
                        logger.severe("Student Number not found. It is mandatory field. " );
                        response.setStatusCode(HttpURLConnection.HTTP_NOT_FOUND);
                        break;
                    }
                    Student student= new Student(requestJson.get("firstName").getAsString(), requestJson.get("lastName").getAsString(),
                            requestJson.get("studentNumber").getAsInt(), requestJson.get("dob").getAsString(), requestJson.get("email").getAsString(),
                            requestJson.get("semester").getAsInt(), requestJson.get("degree").getAsString(), requestJson.get("address").getAsString());

                    postDataToDatabase(db, student, studentNumber);
                    writer.printf("Student %s is added into the database", name);
                    response.setStatusCode(HttpURLConnection.HTTP_OK);
                    break;
                default:
                    writer.printf("Request Content-Type is not JSON" );
                    logger.severe("Request Content-Type is not JSON" );
                    response.setStatusCode(HttpURLConnection.HTTP_UNSUPPORTED_TYPE);
            }
        } catch (JsonParseException e) {
            logger.severe("Error parsing JSON: " +  e);
            writer.printf("Error parsing JSON: " + e.getMessage());
            response.setStatusCode(HttpURLConnection.HTTP_UNSUPPORTED_TYPE);
        } catch (InterruptedException e) {
            logger.severe( "ERROR: "+e);
            e.printStackTrace();
        } catch (ExecutionException e) {
            logger.severe( "ERROR: "+e);
            e.printStackTrace();
        }

    }

    private void postDataToDatabase(Firestore db, Student student, int studentNumber) throws ExecutionException, InterruptedException {
        DocumentReference docRef = db.collection("students").document(String.valueOf(studentNumber));
        //asynchronously write data
        ApiFuture<WriteResult> result = docRef.set(student);

        // result.get() blocks on response
        System.out.println("Update time : " + result.get().getUpdateTime());
    }

}
