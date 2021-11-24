package functions;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.WriteResult;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.google.gson.*;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
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
        FirestoreOptions firestoreOptions =
                FirestoreOptions.getDefaultInstance().toBuilder()
                        .setProjectId("dsg-thesis")
                        .setCredentials(GoogleCredentials.getApplicationDefault())
                        .build();
        Firestore db = firestoreOptions.getService();

        // Parse JSON request and get the data
        try {
            switch (contentType) {
                case "application/json":
                    JsonReader reader = new JsonReader(request.getReader());
                    reader.setLenient(true);
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
                        logger.severe("Student Number not found. It is mandatory field. " );
                        break;
                    }
                    Map<String, Object> data = getTheDataInKeysAndValues(requestJson);
                    postDataToDatabase(db, data, studentNumber);
                    break;
                default:
                    logger.severe("Request Content-Type is not JSON " );
            }
        } catch (JsonParseException e) {
            logger.severe("Error parsing JSON: " + e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        writer.printf("Hello %s!", name);

    }

    private Map<String, Object> getTheDataInKeysAndValues(JsonObject requestJson){
        //String streetName = addressObj.get("streetName").getAsString();

        // firestore db accepts maps with keys and values are posted in firestore
        Map<String, Object> student = new HashMap<>();
        student.put("firstName", requestJson.get("firstName").getAsString());
        student.put("lastName", requestJson.get("lastName").getAsString());
        student.put("studentNumber", requestJson.get("studentNumber").getAsInt());
        student.put("dob", requestJson.get("dob").getAsString());
        student.put("email", requestJson.get("email").getAsString());
        student.put("semester", requestJson.get("semester").getAsInt());
        student.put("degree", requestJson.get("degree").getAsString());
        student.put("address",requestJson.get("address").getAsString());
        System.out.println("what is this student information: "+ student);
        return student;
    }

    private void postDataToDatabase(Firestore db, Map<String, Object> data, int studentNumber) throws ExecutionException, InterruptedException {
        DocumentReference docRef = db.collection("students").document(String.valueOf(studentNumber));
        //asynchronously write data
        ApiFuture<WriteResult> result = docRef.set(data);

        // result.get() blocks on response
        System.out.println("Update time : " + result.get().getUpdateTime());
    }

}
