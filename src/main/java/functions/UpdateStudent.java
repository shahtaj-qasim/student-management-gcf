package functions;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import models.Student;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class UpdateStudent implements HttpFunction {

    private static final Logger logger = Logger.getLogger(UpdateStudent.class.getName());
    private static final Gson gson = new Gson();

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        var writer = new PrintWriter(response.getWriter());
        //firestore instance
        FirestoreOptions firestoreOptions =
                FirestoreOptions.getDefaultInstance().toBuilder()
                        .setProjectId("dsg-thesis")
                        .setCredentials(GoogleCredentials.getApplicationDefault())
                        .build();
        Firestore db = firestoreOptions.getService();
        String studentNumber = request.getFirstQueryParameter("studentNumber").orElse(null);
        String contentType = request.getContentType().orElse("");

        try {
            switch (contentType) {
                case "application/json":
                    if (studentNumber == null) {
                        logger.severe("Student number is not provided");
                    } else {
                        //student number changing is not allowed
                        JsonObject body = gson.fromJson(request.getReader(), JsonObject.class);
                        Map<String, Object> studentToUpdate = new HashMap<>();
                        if (body.has("firstName")) {
                            studentToUpdate.put("firstName", body.get("firstName").getAsString());
                        }
                        if (body.has("lastName")) {
                            studentToUpdate.put("lastName", body.get("lastName").getAsString());
                        }
                        if (body.has("dob")) {
                            studentToUpdate.put("dob", body.get("dob").getAsString());
                        }
                        if (body.has("email")) {
                            studentToUpdate.put("email", body.get("email").getAsString());
                        }
                        if (body.has("semester")) {
                            studentToUpdate.put("semester", body.get("semester").getAsInt());
                        }
                        if (body.has("degree")) {
                            studentToUpdate.put("degree", body.get("degree").getAsString());
                        }
                        if (body.has("address")) {
                            studentToUpdate.put("address", body.get("address").getAsString());
                        }

                        DocumentReference docRef = db.collection("students").document(studentNumber);
                        //asynchronously write data
                        ApiFuture<WriteResult> result = docRef.update(studentToUpdate);

                        // result.get() blocks on response
                        System.out.println("Update time : " + result.get().getUpdateTime());
                        response.setStatusCode(HttpURLConnection.HTTP_OK);
                    }
                default:
                    logger.severe("Request Content-Type is not JSON ");
            }
        } catch (JsonParseException e) {
            logger.severe("Error parsing JSON: " + e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
