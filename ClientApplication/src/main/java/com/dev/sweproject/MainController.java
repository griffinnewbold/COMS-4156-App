package com.dev.sweproject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.dev.sweproject.GlobalInfo.*;

/**
 * Utility class for handling HTTP requests from the client and managing interactions with service.
 */
@Controller
public class MainController {

  /**
   * Handles GET requests to display index page.
   *
   * @return String "index".
   */
  @GetMapping({"/", "/index", "/home"})
  public String index() {
    return "index";
  }

  /**
   * Handles GET request to display dashboard page.
   *
   * @return String "dashboard".
   */
  @GetMapping("/dashboard")
  public String dashboard() { return "dashboard"; }

  /**
   * Handles GET request to display document page.
   *
   * @return String "document".
   */
  @GetMapping("/document")
  public String document() { return "document"; }

  /**
   * Handles display of form to register new user.
   *
   * @param model Model representing a user in the service
   * @return String "register_form".
   */
  @GetMapping("/register")
  public String showForm(Model model) {
    User user = new User();
    model.addAttribute("user", user);

    List<String> listProfession = Arrays.asList("Doctor", "Nurse Practictioner", "Specialist");
    model.addAttribute("listProfession", listProfession);

    return "register_form";
  }

  /**
   * Handles GET request to display generate-key page.
   *
   * @return String "service_registration".
   */
  @GetMapping("/generate-key")
  public String generateKey() {
    return "service_registration";
  }

  /**
   * Handles the POST request to generate a key for network registration.
   *
   * @param model The model object used for adding attributes and the new key.
   * @return A String representing the view name:
   *         - If registration is successful, String 'service_registration'
   *         - If not successful, error message.
   */
  @PostMapping("/generate-key")
  public String postNetworkRequest(Model model) {
    String networkResult = postServiceRegistrationRequest().substring(9);

    if (networkResult.contains("error")) {
      model.addAttribute("error", "An error has occurred please try again later.");
      return "service_registration";
    }
    model.addAttribute("key", networkResult.substring(networkResult.indexOf(":")+2,
            networkResult.length()-2));
    return "service_registration";
  }

  /**
   * Handles form submission for user registration.
   *
   * @param model The model object used for adding attributes.
   * @param user The User object containing registration details submitted via the form.
   * @return A String representing the view name based on the registration outcome:
   *         - If user details are valid, redirects to login page.
   *         - If user details are invalid, reloads registration form with appropriate
   *           error messages.
   */
  @PostMapping("/register")
  public String submitForm(Model model, @ModelAttribute("user") User user) {
    boolean isValid = User.isValidUser(user);

    if (isValid) {
      System.out.println(user);
      return registerUser(user);
    }

    User blankUser = new User();
    model.addAttribute("user", blankUser);

    List<String> listProfession = Arrays.asList("Doctor", "Nurse Practictioner", "Specialist");
    model.addAttribute("listProfession", listProfession);
    model.addAttribute("error", "Please fill in all fields properly!");
    return "register_form";
  }

  /**
   * Handles GET request to display login page.
   *
   * @return String "login_form".
   */
  @GetMapping("/login")
  public String gotoLogin(Model model) {
    return "login_form";
  }

  /**
   * Handles POST request to attempt to login the user with provided credentials.
   *
   * @param email String email provided by user.
   * @param password String password provided by user.
   * @param model The model object used for adding attributes.
   * @return A String representing the view name based on the login outcome:
   *         - If login was successful, String "dashboard" to redirect to dashboard page.
   *         - If unsuccessful, error message to reload login form.
   */
  @PostMapping("/login")
  public String attemptLogin(String email, String password, Model model) {
    try {
      CompletableFuture<Boolean> result = firebaseDataService.confirmLogin(email.substring(
              0, email.indexOf('.')), password
      );
      boolean isSuccessful = result.get();

      if (isSuccessful) {
        model.addAttribute("user_id", email.substring(0, email.indexOf('@')));
        return "dashboard";
      }
      model.addAttribute("error", "Invalid credentials");
      return "login_form";

    } catch (Exception e) {
      System.out.println(e.getMessage());
      return "error";
    }

  }

  /**
   * Registers new User with provided credentials.
   *
   * @param user The User object containing registration details submitted via the form.
   * @return A String representing the view name based on the registration outcome:
   *         - If login was successful, String "login_form" to redirect to login page.
   *         - If unsuccessful, error message to reload registration form.
   */
  public String registerUser(User user) {
    try {
      CompletableFuture<Object> result = firebaseDataService.addEntry(NETWORK_ID, user.getEmail().substring(
              0, user.getEmail().indexOf('.')), user
      );
      result.get();
      return "login_form";
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return "error";
    }
  }

  /**
   * Handles the GET request to retrieve documents for a given user ID.
   *
   * @param userId The ID of the user whose documents are to be retrieved.
   * @return A ResponseEntity containing JSON representation of the retrieved documents:
   *         - If successful, it returns a JSON response with the retrieved documents and HTTP status OK (200).
   * @throws JsonProcessingException If there's an issue with JSON processing while retrieving documents.
   */
  @GetMapping(value = "/retrieve-documents", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> retrieveDocumentsAPI(@RequestParam(value = "user_id") String userId)
          throws JsonProcessingException {
    ObjectMapper om = new ObjectMapper();
    return new ResponseEntity<>(om.writeValueAsString(retrieveDocuments(userId)), HttpStatus.OK);
  }

  /**
   * Retrieves documents for a specified user ID by sending an HTTP request.
   *
   * @param userId The ID of the user for whom documents are to be retrieved.
   * @return A String representing the retrieved documents or an error message.
   */
  public String retrieveDocuments(String userId) {
    String fullUrl = SERVICE_IP + RETRIEVE_URI + "?network-id=" + NETWORK_ID + "&user-id=" + userId;
    return sendHttpRequest(fullUrl);
  }

  /**
   * Handles the GET request to retrieve document stats for a given user ID and document ID.
   *
   * @param userId The ID of the user with ownership to specified document.
   * @param documentId The ID of the document whose stats are to be retrieved.
   * @return A ResponseEntity containing JSON representation of the retrieved document stats:
   *         - If successful, it returns a JSON response with the retrieved document stats and status OK (200).
   * @throws JsonProcessingException If there's an issue with JSON processing while retrieving documents.
   */
  @GetMapping(value = "/retrieve-document-stats", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> retrieveDocumentStatsAPI(@RequestParam(value = "user_id") String userId,
                                                    @RequestParam(value = "doc_id") String documentId)
          throws JsonProcessingException {
      ObjectMapper om = new ObjectMapper();
      return new ResponseEntity<>(om.writeValueAsString(retrieveDocumentStats(userId, documentId)), HttpStatus.OK);
  }

  /**
   * Retrieves document stats for a specified user ID and document title by sending an HTTP request.
   *
   * @param userId The ID of the user with ownership to specified document.
   * @param documentTitle The title of the document whose stats are to be retrieved.
   * @return A String representing the retrieved document stats or an error message.
   */
  public String retrieveDocumentStats(String userId, String documentTitle) {
    String fullUrl = SERVICE_IP + STATS_URI + "?network-id=" + NETWORK_ID + "&document-name="+documentTitle +
            "&your-user-id=" + userId;
    return sendHttpRequest(fullUrl);
  }

  /**
   * Retrieves a specified version of a document by sending an HTTP request.
   *
   * @param userId The ID of the user with ownership to specified document.
   * @param documentTitle The title of the document whose previous version is to be retrieved.
   * @param revisionNumber The index of the version to be retrieved
   * @return A String representing the retrieved document or an error message.
   */
  public String retrievePreviousVersion(String userId, String documentTitle, int revisionNumber) {
    String fullUrl = SERVICE_IP + REVISION_URI + "?network-id=" + NETWORK_ID + "&document-name="+documentTitle +
            "&your-user-id=" + userId + "&revision-number=" + revisionNumber;
    return sendHttpRequest(fullUrl);
  }

  /**
   * Retrieves document contents for a specified user ID and document title by sending an HTTP request.
   *
   * @param userId The ID of the user with ownership to specified document.
   * @param documentTitle The title of the document whose contents are to be retrieved.
   * @return A String representing the retrieved document contents or an error message.
   */
  public String retrieveDocumentContents(String userId, String documentTitle) {
    String fullUrl = SERVICE_IP + DOWNLOAD_URI + "?network-id=" + NETWORK_ID + "&document-name="+documentTitle +
            "&your-user-id=" + userId;
    return sendHttpRequest(fullUrl);
  }

  /**
   * Retrieves differences between two specified documents by sending an HTTP request.
   *
   * @param userId The ID of the user with ownership to specified document.
   * @param fstDocumentTitle The title of the first document to be compared.
   * @param sndDocumentTitle The title of the second document to be compared.
   * @return A String representing the retrieved differences or an error message.
   */
  public String retrieveDocumentDifferences(String userId, String fstDocumentTitle, String sndDocumentTitle) {
    String fullUrl = SERVICE_IP + DIFFERENCE_URI + "?network-id=" + NETWORK_ID + "&fst-doc-name="+ fstDocumentTitle
            + "&snd-doc-name=" + sndDocumentTitle + "&your-user-id=" + userId;
    return sendHttpRequest(fullUrl);
  }

  /**
   * Checks for the existence of a specified document by sending an HTTP request.
   *
   * @param userId The ID of the user with ownership to specified document.
   * @param documentTitle The title of the document whose existence is being checked for.
   * @return A String representing the existence of a document or an error message.
   */
  public String retrieveDocumentExistence(String userId, String documentTitle) {
    String fullUrl = SERVICE_IP + SEARCH_URI + "?network-id=" + NETWORK_ID + "&document-name="+ documentTitle
            + "&your-user-id=" + userId;
    return sendHttpRequest(fullUrl);
  }

  /**
   * Handles the GET request to retrieve usernames as JSON data.
   *
   * @return A ResponseEntity containing JSON representation of usernames:
   *         - If successful, a JSON response with usernames and HTTP status OK (200).
   * @throws JsonProcessingException If there's an issue with JSON processing while retrieving usernames.
   */
  @GetMapping(value = "/usernames", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> retrieveDocumentStatsAPI() throws JsonProcessingException {
    ObjectMapper om = new ObjectMapper();
    return new ResponseEntity<>(om.writeValueAsString(retrieveUsernames()), HttpStatus.OK);
  }

  /**
   * Retrieves the usernames of users in the database asynchronously.
   *
   * @return JSON-formatted string containing the usernames.
   */
  public String retrieveUsernames() {
    CompletableFuture<List<String>> result = firebaseDataService.getSubcollectionNames();
    try {
     List<String> listOfUsers = result.get();

     String list_str = "[";
     for (int i = 0; i < listOfUsers.size(); i++) {
       String new_entry = "\"" + listOfUsers.get(i) + "\"";
       list_str += new_entry;
       if (i != listOfUsers.size() - 1) list_str += ",";
     }
     list_str += "]";

     return list_str;
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return "[]";
    }
  }

  /**
   * Handles the PATCH request to share a document with a new user.
   *
   * @param userId      The ID of the user who owns the document.
   * @param documentId  The ID of the document to be shared.
   * @param newUserId   The ID of the new user with whom the document is to be shared.
   * @return A ResponseEntity containing JSON representation of the sharing request:
   *         - If successful, it returns a JSON response with the result of the sharing operation and HTTP status OK (200).
   * @throws JsonProcessingException If there's an issue with JSON processing while handling the sharing request.
   */
  @PatchMapping(value = "/share-document", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> patchShareRequestAPI(@RequestParam(value = "user_id") String userId,
                                                @RequestParam(value = "doc_id") String documentId,
                                                @RequestParam(value = "new_user_id") String newUserId)
          throws JsonProcessingException {
    ObjectMapper om = new ObjectMapper();
    return new ResponseEntity<>(om.writeValueAsString(patchShareRequest(userId, documentId, newUserId)), HttpStatus.OK);
  }

  /**
   * Sends a PATCH request to share a document with a new user.
   *
   * @param userId         The ID of the user who owns the document.
   * @param documentTitle  The title of the document to be shared.
   * @param newUserId      The ID of the new user with whom the document is to be shared.
   * @return A String indicating the result of the sharing operation:
   *         - If successful, a success message with the result from the server.
   *         - If an error occurs during the request, an error message.
   */
  public String patchShareRequest(String userId, String documentTitle, String newUserId) {
    documentTitle = convertDocumentTitle(documentTitle);
    String fullUrl = SERVICE_IP + SHARE_URI + "?network-id=" + NETWORK_ID + "&document-name="+ documentTitle
            + "&your-user-id=" + userId + "&their-user-id=" + newUserId;

    try {
      HttpClient httpClient = HttpClients.createDefault();
      HttpPatch httpPatch = new HttpPatch(fullUrl);

      httpPatch.setHeader("Content-Type", "application/json");

      HttpResponse response = httpClient.execute(httpPatch);

      BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
      StringBuilder result = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        result.append(line);
      }
      return "success: " + result;

    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      return "error: An unexpected error has occurred.";
    }
  }

  /**
   * Handles the DELETE request to delete a document.
   *
   * @param userId        The ID of the user with ownership of the document.
   * @param documentName  The name of the document to be deleted.
   * @return A ResponseEntity containing JSON representation of the deletion request:
   *         - If successful, it returns a JSON response with the result of the deletion and HTTP status OK (200).
   * @throws JsonProcessingException If there's an issue with JSON processing while handling the deletion request.
   */
  @DeleteMapping(value = "/delete-document", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> postUploadRequestAPI(@RequestParam(value = "user_id") String userId,
                                                @RequestParam(value = "doc_name") String documentName)
          throws JsonProcessingException {
    ObjectMapper om = new ObjectMapper();
    return new ResponseEntity<>(om.writeValueAsString(deleteRequest(userId, documentName)), HttpStatus.OK);
  }

  /**
   * Sends a DELETE request to delete a document.
   *
   * @param userId         The ID of the user with ownership of the document.
   * @param documentTitle  The title of the document to be deleted.
   * @return A String indicating the result of the deletion operation:
   *         - If successful, success message with the result from the server.
   *         - If an error occurs during request, it returns an error message.
   */
  public String deleteRequest(String userId, String documentTitle) {
    documentTitle = convertDocumentTitle(documentTitle);
    String fullUrl = SERVICE_IP + DELETE_URI + "?network-id=" + NETWORK_ID + "&document-name="+ documentTitle
            + "&your-user-id=" + userId;
    try {
      HttpClient httpClient = HttpClients.createDefault();
      HttpDelete httpDelete = new HttpDelete(fullUrl);

      HttpResponse response = httpClient.execute(httpDelete);

      BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
      StringBuilder result = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        result.append(line);
      }
      return "success: " + result;
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      return "error: An unexpected error has occurred.";
    }
  }

  /**
   * Handles the POST request to upload a document.
   *
   * @param userId        The ID of the user with ownership of the document.
   * @param documentName  The name of the document to be uploaded.
   * @param contents      The contents of the document to be uploaded.
   * @return A ResponseEntity containing JSON representation of the upload request:
   *         - If successful, JSON response with the result of the upload operation and HTTP status OK (200).
   * @throws JsonProcessingException If there's an issue with JSON processing while handling the upload request.
   */
  @PostMapping(value = "/upload-document", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> postUploadRequestAPI(@RequestParam(value = "user_id") String userId,
                                                @RequestParam(value = "doc_name") String documentName,
                                                @RequestParam(value = "contents") String contents)
          throws JsonProcessingException {
    ObjectMapper om = new ObjectMapper();
    return new ResponseEntity<>(om.writeValueAsString(
            postUploadRequest(userId, documentName, contents)), HttpStatus.OK);
  }

  /**
   * Sends a POST request to upload a document.
   *
   * @param userId         The ID of the user with ownership of document.
   * @param documentTitle  The title of the document to be uploaded.
   * @param contents       The contents of the document to be uploaded.
   * @return A String indicating the result of the upload operation:
   *         - If successful, success message with the result from the server.
   *         - If the upload fails, it returns an appropriate failure message.
   */
  public String postUploadRequest(String userId, String documentTitle, String contents) {
    String fullUrl = SERVICE_IP + UPLOAD_URI + "?network-id=" + NETWORK_ID + "&document-name="+ documentTitle
            + "&user-id=" + userId;
    try {
      byte[] contentsBytes = contents.getBytes();
      ByteArrayMultipartFile file = new ByteArrayMultipartFile(contentsBytes, documentTitle, "text/plain",
              documentTitle + ".txt");
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.MULTIPART_FORM_DATA);

      MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
      body.add("contents", new ByteArrayResource(file.getBytes()) {
        @Override
        public String getFilename() {
          return file.getOriginalFilename();
        }
      });

      org.springframework.http.HttpEntity<MultiValueMap<String, Object>> requestEntity =
              new org.springframework.http.HttpEntity<>(body, headers);

      ResponseEntity<String> response = restTemplate.postForEntity(fullUrl, requestEntity, String.class);

      if (response.getStatusCode().is2xxSuccessful()) {
        return "success: " + response.getBody();
      } else {
        return "failure: " + response.getStatusCode() + " - " + response.getBody();
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return "error: An unexpected error has occurred.";
    }
  }

  /**
   * Sends a POST request to register a service.
   *
   * @return A String indicating the result of the service registration:
   *         - If successful, a success message with the result from the server.
   *         - If unsuccessful, an error message.
   */
  public String postServiceRegistrationRequest() {
    String fullUrl = SERVICE_IP + REGISTRATION_URI;
    try {
      HttpClient httpClient = HttpClients.createDefault();
      HttpPost httpPost = new HttpPost(fullUrl);

      httpPost.setHeader("Content-Type", "application/json");
      HttpResponse response = httpClient.execute(httpPost);

      BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
      StringBuilder result = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        result.append(line);
      }
      return "success: " + result;
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      return "error: An unexpected error has occurred.";
    }
  }

  private String sendHttpRequest(String url) {
    try {
      ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

      if (response.getStatusCode().is2xxSuccessful()) {
        return response.getBody();
      }
      return "failure: " + response.getStatusCode() + " - " + response.getBody();
    } catch (HttpClientErrorException e) {
      System.out.println("HTTP error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
      return "failure: " + e.getStatusCode() + " - " + e.getResponseBodyAsString();
    } catch (Exception e) {
      System.out.println("Unexpected error: " + e.getMessage());
      return "error: An unexpected error has occurred.";
    }
  }

  private String convertDocumentTitle(String title) {
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < title.length(); i++) {
      if (title.charAt(i) != ' ') {
        result.append(title.charAt(i));
      } else {
        result.append("%20");
      }
    }
    return result.toString();
  }
}