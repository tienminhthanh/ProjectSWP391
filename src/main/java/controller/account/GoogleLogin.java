package controller.account;

import com.google.gson.Gson;  // Import Gson to convert objects to JSON and vice versa.

import com.google.gson.JsonObject;  // Import JsonObject to handle JSON responses.
import model.Iconstant;  // Import constant values (like client ID and secret).
import model.GoogleAccount;  // Import the model to hold Google user information.

import java.io.IOException;
import java.net.URI;  // Import URI class to work with URLs.
import java.net.URLEncoder;  // Import URLEncoder to encode URL parameters.
import java.net.http.HttpClient;  // Import HttpClient to make HTTP requests.
import java.net.http.HttpRequest;  // Import HttpRequest to create HTTP requests.
import java.net.http.HttpResponse;  // Import HttpResponse to handle the response.
import java.nio.charset.StandardCharsets;  // Import StandardCharsets for character encoding.
import java.util.HashMap;  // Import HashMap to store key-value pairs.
import java.util.Map;  // Import Map to work with key-value pairs.

public class GoogleLogin {

    // This method gets the token from Google's OAuth using the authorization code
    public static String getToken(String code) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();  // Create a new HTTP client.

        // Set the parameters to send in the request to get the token
        Map<String, String> parameters = new HashMap<>();
        parameters.put("client_id", Iconstant.GOOGLE_CLIENT_ID);  // Set the client ID.
        parameters.put("client_secret", Iconstant.GOOGLE_CLIENT_SECRET);  // Set the client secret.
        parameters.put("redirect_uri", Iconstant.GOOGLE_REDIRECT_URI);  // Set the redirect URI.
        parameters.put("code", code);  // Add the authorization code.
        parameters.put("grant_type", Iconstant.GOOGLE_GRANT_TYPE);  // Set the grant type.

        String formData = getFormData(parameters);  // Convert the parameters to x-www-form-urlencoded format.

        // Create a POST request to Google's token endpoint
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Iconstant.GOOGLE_LINK_GET_TOKEN))  // Set the URL for getting the token.
                .header("Content-Type", "application/x-www-form-urlencoded")  // Set the content type.
                .POST(HttpRequest.BodyPublishers.ofString(formData))  // Send the form data.
                .build();

        // Send the request and get the response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonObject jobj = new Gson().fromJson(response.body(), JsonObject.class);  // Parse the response body to JSON.
        
        // Check if the response contains an access token
        if (!jobj.has("access_token")) {
            throw new IOException("No access_token found in Google's response: " + response.body());
        }
        
        // Return the access token
        return jobj.get("access_token").getAsString();
    }

    // This method gets the user information from Google using the access token
    public static GoogleAccount getUserInfo(final String accessToken) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();  // Create a new HTTP client.

        // Create a GET request to fetch the user's information from Google
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Iconstant.GOOGLE_LINK_GET_USER_INFO))  // Set the URL for user info.
                .header("Authorization", "Bearer " + accessToken)  // Add the access token in the header.
                .header("Accept", "application/json")  // Set the Accept header to JSON.
                .GET()  // Make a GET request.
                .build();

        // Send the request and get the response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Convert the response body to a GoogleAccount object and return it
        return new Gson().fromJson(response.body(), GoogleAccount.class);
    }

    // This method converts parameters to a x-www-form-urlencoded format
    private static String getFormData(Map<String, String> params) {
        StringBuilder formData = new StringBuilder();  // Use StringBuilder to build the form data.
        
        // Loop through the parameters and add each key-value pair to the form data
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (formData.length() > 0) {
                formData.append("&");  // Add an ampersand between parameters.
            }
            formData.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8))  // Encode the key.
                    .append("=")
                    .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));  // Encode the value.
        }
        
        return formData.toString();  // Return the form data as a string.
    }
}
