package com.dev.sweproject;

//Stylechecker wanted this, otherwise we'd clearly do .*
import static com.dev.sweproject.GlobalInfo.DELETE_URI;
import static com.dev.sweproject.GlobalInfo.DIFFERENCE_URI;
import static com.dev.sweproject.GlobalInfo.DOC_NAME_URI;
import static com.dev.sweproject.GlobalInfo.DOWNLOAD_URI;
import static com.dev.sweproject.GlobalInfo.NETWORK_ID;
import static com.dev.sweproject.GlobalInfo.REGISTRATION_URI;
import static com.dev.sweproject.GlobalInfo.RETRIEVE_URI;
import static com.dev.sweproject.GlobalInfo.REVISION_URI;
import static com.dev.sweproject.GlobalInfo.SEARCH_URI;
import static com.dev.sweproject.GlobalInfo.SERVICE_IP;
import static com.dev.sweproject.GlobalInfo.SHARE_URI;
import static com.dev.sweproject.GlobalInfo.STATS_URI;
import static com.dev.sweproject.GlobalInfo.UPLOAD_URI;
import static com.dev.sweproject.GlobalInfo.firebaseDataService;
import static com.dev.sweproject.GlobalInfo.restTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;


/**
 * MainController is responsible for all routing logic as well as making
 * calls to the service API.
 */
@Controller
public class MainController {

  /**
   * Redirects to the homepage.
   *
   * @return A String containing the name of the html file to be loaded.
   */
  @GetMapping({"/", "/index", "/home"})
  public String index() {
    return "index";
  }

  /**
   * Redirects to the dashboard.
   *
   * @return A String containing the name of the html file to be loaded.
   */
  @GetMapping("/dashboard")
  public String dashboard() {
    return "dashboard";
  }

  /**
   * Redirects to the document page.
   *
   * @return A String containing the name of the html file to be loaded.
   */
  @GetMapping("/document")
  public String document() {
    return "document";
  }

  /**
   * Redirects to the compare documents page.
   *
   * @return A String containing the name of the html file to be loaded.
   */
  @GetMapping("/compare")
  public String compare() { return "compare"; }

  /**
   * Redirects to the registration page.
   *
   * @return A String containing the name of the html file to be loaded.
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
   * Redirects to the network generation page.
   *
   * @return A String containing the name of the html file to be loaded.
   */
  @GetMapping("/generate-key")
  public String generateKey() {
    return "service_registration";
  }

  /**
   * Redirects to the login page.
   *
   * @return A String containing the name of the html file to be loaded.
   */
  @GetMapping("/login")
  public String gotoLogin(Model model) {
    return "login_form";
  }

  /**
   * Retrieves the documents associated with the specified userId.
   *
   * @param userId A String representing which user to retrieve documents from.
   * @return A ResponseEntity detailing the success of the procedure.
   * @throws JsonProcessingException if a JSON error occurs.
   */
  @GetMapping(value = "/retrieve-documents", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> retrieveDocumentsApi(@RequestParam(value = "user_id") String userId)
                                                throws JsonProcessingException {
    ObjectMapper om = new ObjectMapper();
    return new ResponseEntity<>(om.writeValueAsString(retrieveDocuments(userId)), HttpStatus.OK);
  }

  /**
   * Retrieves the statistics for the specific document specified for the specified user.
   *
   * @param userId A String representing the user.
   * @param docId A String representing the documentId.
   * @return A ResponseEntity detailing the success of the procedure.
   * @throws JsonProcessingException if a JSON error occurs.
   */
  @GetMapping(value = "/retrieve-document-stats", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> retrieveDocumentStatsApi(@RequestParam(value = "user_id") String userId,
                                                    @RequestParam(value = "doc_id") String docId)
                                                    throws JsonProcessingException {
    ObjectMapper om = new ObjectMapper();
    return new ResponseEntity<>(om.writeValueAsString(retrieveDocumentStats(userId, docId)),
        HttpStatus.OK);
  }

  /**
   * Retrieves the contents of the specified document.
   *
   * @param userId A String representing the user.
   * @param docName A String representing the document to get contents of.
   * @return A ResponseEntity detailing the success of the procedure.
   * @throws JsonProcessingException if a JSON error occurs.
   */
  @GetMapping(value = "/document-contents", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> retrieveDocContentsApi(@RequestParam(value = "user_id") String userId,
                                                  @RequestParam(value = "doc_name") String docName)
                                                  throws JsonProcessingException {
    ObjectMapper om = new ObjectMapper();
    return new ResponseEntity<>(om.writeValueAsString(retrieveDocumentContents(userId, docName)),
        HttpStatus.OK);
  }

  /**
   * Checks to see if the specified document exists.
   *
   * @param userId A String representing the user.
   * @param docName A String representing the name of the document to search for.
   * @return A ResponseEntity detailing the success of the procedure.
   * @throws JsonProcessingException if a JSON error occurs.
   */
  @GetMapping(value = "/check-for-document", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> retrieveDocExistenceApi(@RequestParam(value = "user_id") String userId,
                                                   @RequestParam(value = "doc_name") String docName)
                                                   throws JsonProcessingException {
    ObjectMapper om = new ObjectMapper();
    return new ResponseEntity<>(om.writeValueAsString(retrieveDocumentExistence(userId, docName)),
        HttpStatus.OK);
  }

  /**
   * Retrieves the usernames in the executing network.
   *
   * @return A ResponseEntity detailing the success of the procedure.
   * @throws JsonProcessingException if a JSON error occurs.
   */
  @GetMapping(value = "/usernames", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> retrieveUsernamesApi() throws JsonProcessingException {
    ObjectMapper om = new ObjectMapper();
    return new ResponseEntity<>(om.writeValueAsString(retrieveUsernames()), HttpStatus.OK);
  }

  /**
   * Retrieves the names of documents associated with the specified user.
   *
   * @param userId A String representing the user.
   * @return A ResponseEntity detailing the success of the procedure.
   * @throws JsonProcessingException if a JSON error occurs.
   */
  @GetMapping(value = "/docnames", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> retrieveDocumentNamesApi(@RequestParam(value = "user_id") String userId)
                                                    throws JsonProcessingException {
    ObjectMapper om = new ObjectMapper();
    return new ResponseEntity<>(om.writeValueAsString(retrieveDocNames(userId)), HttpStatus.OK);
  }

  /**
   * Attempts to generate a new network key for a first time user.
   *
   * @param model A Model object used for adding dynamic elements to HTML.
   * @return A String detailing the result of the operation.
   */
  @PostMapping("/generate-key")
  public String postNetworkRequest(Model model) {
    String networkResult = postServiceRegistrationRequest().substring(9);

    if (networkResult.contains("error")) {
      model.addAttribute("error", "An error has occurred please try again");
      return "service_registration";
    }
    model.addAttribute("key", networkResult.substring(networkResult.indexOf(":") + 2,
        networkResult.length() - 2));
    return "service_registration";
  }

  /**
   *  Attempts to register the user with the app, upon success is redirected to the login page
   *  upon failure redirected to the registration page with an error message.
   *
   * @param model A Model object used for adding dynamic elements to HTML.
   * @param user A User object representing the newly created user.
   * @return A String detailing where to redirect the user.
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

    List<String> listProfession = Arrays.asList("Doctor", "Nurse Practitioner", "Specialist");
    model.addAttribute("listProfession", listProfession);
    model.addAttribute("error", "Please fill in all fields properly!");
    return "register_form";
  }

  /**
   *  Attempts to log the user in to the app, upon success is redirected to the dashboard
   *  upon failure, is redirected to the login page with an error message.
   *
   * @param email A String representing the email of the user.
   * @param password A String representing the password of the user.
   * @param model A Model object used for adding dynamic elements to HTML.
   * @return A String detailing where to redirect the user.
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
   * Uploads a document to the service.
   *
   * @param userId   A String representing the current user.
   * @param docName  A String representing the name of the document.
   * @param contents A String representing the file's contents.
   * @return A ResponseEntity detailing the success of the procedure.
   * @throws JsonProcessingException if a JSON error occurs.
   */
  @PostMapping(value = "/upload-document", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> postUploadRequestApi(@RequestParam(value = "user_id") String userId,
                                                @RequestParam(value = "doc_name") String docName,
                                                @RequestParam(value = "contents") String contents)
                                                throws JsonProcessingException {
    ObjectMapper om = new ObjectMapper();
    return new ResponseEntity<>(om.writeValueAsString(postUploadRequest(userId, docName, contents)),
        HttpStatus.OK);
  }

  /**
   * Shares the specified document with the newly specified user.
   *
   * @param userId A String representing the current user.
   * @param documentId A String representing the documentId.
   * @param newUser A String representing the user to be granted access to the document.
   * @return A ResponseEntity detailing the success of the procedure.
   * @throws JsonProcessingException if a JSON error occurs.
   */
  @PatchMapping(value = "/share-document", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> patchShareRequestApi(@RequestParam(value = "user_id") String userId,
                                                @RequestParam(value = "doc_id") String documentId,
                                                @RequestParam(value = "new_user_id") String newUser)
                                                throws JsonProcessingException {
    ObjectMapper om = new ObjectMapper();
    return new ResponseEntity<>(om.writeValueAsString(patchShareRequest(userId, documentId,
        newUser)), HttpStatus.OK);
  }

  /**
   * Deletes the specified document from existence.
   *
   * @param userId A String representing the user
   * @param docName A String representing the name of the document to be deleted.
   * @return A ResponseEntity detailing the success of the procedure.
   * @throws JsonProcessingException if a JSON error occurs.
   */
  @DeleteMapping(value = "/delete-document", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> postDeleteRequestApi(@RequestParam(value = "user_id") String userId,
                                                @RequestParam(value = "doc_name") String docName)
                                                throws JsonProcessingException {
    ObjectMapper om = new ObjectMapper();
    return new ResponseEntity<>(om.writeValueAsString(deleteRequest(userId, docName)),
        HttpStatus.OK);
  }

  private String registerUser(User user) {
    try {
      CompletableFuture<Object> result = firebaseDataService.addEntry(NETWORK_ID,
          user.getEmail().substring(0, user.getEmail().indexOf('.')), user);
      result.get();
      return "login_form";
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return "error";
    }
  }

  private String retrieveDocuments(String userId) {
    String fullUrl = SERVICE_IP + RETRIEVE_URI + "?network-id=" + NETWORK_ID + "&user-id=" + userId;
    return sendHttpRequest(fullUrl);
  }

  private String retrieveDocumentStats(String userId, String documentTitle) {
    String fullUrl = SERVICE_IP + STATS_URI + "?network-id=" + NETWORK_ID + "&document-name="
        + documentTitle + "&your-user-id=" + userId;
    return sendHttpRequest(fullUrl);
  }

  private String retrievePreviousVersion(String userId, String documentTitle, int revisionNumber) {
    String fullUrl = SERVICE_IP + REVISION_URI + "?network-id=" + NETWORK_ID + "&document-name="
        + documentTitle + "&your-user-id=" + userId + "&revision-number=" + revisionNumber;
    return sendHttpRequest(fullUrl);
  }

  private String retrieveDocumentContents(String userId, String documentTitle) {
    String fullUrl = SERVICE_IP + DOWNLOAD_URI + "?network-id=" + NETWORK_ID + "&document-name="
        + documentTitle + "&your-user-id=" + userId;
    return sendHttpRequest(fullUrl);
  }

  private String retrieveDocumentDifferences(String userId, String fstDocumentTitle,
                                            String sndDocumentTitle) {
    String fullUrl = SERVICE_IP + DIFFERENCE_URI + "?network-id=" + NETWORK_ID + "&fst-doc-name="
        + fstDocumentTitle + "&snd-doc-name=" + sndDocumentTitle + "&your-user-id=" + userId;
    return sendHttpRequest(fullUrl);
  }

  private String retrieveDocumentExistence(String userId, String documentTitle) {
    String fullUrl = SERVICE_IP + SEARCH_URI + "?network-id=" + NETWORK_ID + "&document-name="
        + documentTitle + "&your-user-id=" + userId;
    return sendHttpRequest(fullUrl);
  }

  private String retrieveUsernames() {
    CompletableFuture<List<String>> result = firebaseDataService.getSubcollectionNames();
    try {
      List<String> listOfUsers = result.get();

      StringBuilder listStr = new StringBuilder("[");
      for (int i = 0; i < listOfUsers.size(); i++) {
        String newEntry = "\"" + listOfUsers.get(i) + "\"";
        listStr.append(newEntry);
        if (i != listOfUsers.size() - 1) {
          listStr.append(",");
        }
      }
      listStr.append("]");

      return listStr.toString();
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return "[]";
    }
  }

  private String retrieveDocNames(String userId) {
    String fullUrl = SERVICE_IP + DOC_NAME_URI + "?network-id=" + NETWORK_ID
        + "&user-id=" + userId;
    String res = sendHttpRequest(fullUrl);
    System.out.println(res);
    return res;
  }

  private String patchShareRequest(String userId, String documentTitle, String newUserId) {
    documentTitle = convertDocumentTitle(documentTitle);
    String fullUrl = SERVICE_IP + SHARE_URI + "?network-id=" + NETWORK_ID + "&document-name="
        + documentTitle + "&your-user-id=" + userId + "&their-user-id=" + newUserId;

    try {
      HttpClient httpClient = HttpClients.createDefault();
      HttpPatch httpPatch = new HttpPatch(fullUrl);

      httpPatch.setHeader("Content-Type", "application/json");

      HttpResponse response = httpClient.execute(httpPatch);

      InputStream inputStream = response.getEntity().getContent();
      BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
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

  private String deleteRequest(String userId, String documentTitle) {
    documentTitle = convertDocumentTitle(documentTitle);
    String fullUrl = SERVICE_IP + DELETE_URI + "?network-id=" + NETWORK_ID + "&document-name="
        + documentTitle + "&your-user-id=" + userId;
    try {
      HttpClient httpClient = HttpClients.createDefault();
      HttpDelete httpDelete = new HttpDelete(fullUrl);

      HttpResponse response = httpClient.execute(httpDelete);

      InputStream inputStream = response.getEntity().getContent();
      BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
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

  private String postUploadRequest(String userId, String documentTitle, String contents) {
    String fullUrl = SERVICE_IP + UPLOAD_URI + "?network-id=" + NETWORK_ID + "&document-name="
        + documentTitle + "&user-id=" + userId;
    try {
      byte[] contentsBytes = contents.getBytes();
      ByteArrayMultipartFile file = new ByteArrayMultipartFile(contentsBytes, documentTitle,
          "text/plain", documentTitle + ".txt");
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

      ResponseEntity<String> response = restTemplate.postForEntity(fullUrl, requestEntity,
          String.class);

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

  private String postServiceRegistrationRequest() {
    String fullUrl = SERVICE_IP + REGISTRATION_URI;
    try {
      HttpClient httpClient = HttpClients.createDefault();
      HttpPost httpPost = new HttpPost(fullUrl);

      httpPost.setHeader("Content-Type", "application/json");
      HttpResponse response = httpClient.execute(httpPost);

      InputStream inputStream = response.getEntity().getContent();
      BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
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