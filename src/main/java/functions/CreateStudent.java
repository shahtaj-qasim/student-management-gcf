package functions;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
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
        String contentType = request.getContentType().orElse("");
        var writer = new PrintWriter(response.getWriter());

        // Parse JSON request and check for "name" field
        try {
            switch (contentType) {
                case "application/json":
                    // '{"name":"John"}'
                    JsonElement requestParsed = gson.fromJson(request.getReader(), JsonElement.class);
                    JsonObject requestJson = null;
                    if (requestParsed != null && requestParsed.isJsonObject()) {
                        requestJson = requestParsed.getAsJsonObject();
                    }

                    if (requestJson != null && requestJson.has("firstName")) {
                        name = requestJson.get("firstName").getAsString();
                    }
                    break;
            }
        } catch (JsonParseException e) {
            logger.severe("Error parsing JSON: " + e.getMessage());
        }


        writer.printf("Hello %s!", name);

    }
}
