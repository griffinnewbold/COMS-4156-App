package com.dev.sweproject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.dev.sweproject.GlobalInfo.*;
@Controller
public class MainController {

  @GetMapping({"/", "/index", "/home"})
  public String index() {
    return "index";
  }

  @GetMapping("/dashboard")
  public String dashboard() { return "dashboard"; }

  @GetMapping("/document")
  public String document() { return "document"; }

  @GetMapping("/register")
  public String showForm(Model model) {
    User user = new User();
    model.addAttribute("user", user);

    List<String> listProfession = Arrays.asList("Doctor", "Nurse Practictioner", "Specialist");
    model.addAttribute("listProfession", listProfession);

    return "register_form";
  }

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

  @GetMapping("/login")
  public String gotoLogin(Model model) {
    return "login_form";
  }

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

  @GetMapping(value = "/retrieve-documents", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> retrieveDocumentsAPI(@RequestParam(value = "user_id") String userId)
          throws JsonProcessingException {
    ObjectMapper om = new ObjectMapper();
    return new ResponseEntity<>(om.writeValueAsString(retrieveDocuments(userId)), HttpStatus.OK);
  }

  public String retrieveDocuments(String userId) {
    String fullUrl = SERVICE_IP + RETRIEVE_URI + "?network-id=" + NETWORK_ID + "&user-id=" + userId;
    return sendHttpRequest(fullUrl);
  }

  @GetMapping(value = "/retrieve-document-stats", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> retrieveDocumentStatsAPI(@RequestParam(value = "user_id") String userId,
                                                    @RequestParam(value = "doc_id") String documentId)
          throws JsonProcessingException {
      ObjectMapper om = new ObjectMapper();
      return new ResponseEntity<>(om.writeValueAsString(retrieveDocumentStats(userId, documentId)), HttpStatus.OK);
  }
  public String retrieveDocumentStats(String userId, String documentTitle) {
    String fullUrl = SERVICE_IP + STATS_URI + "?network-id=" + NETWORK_ID + "&document-name="+documentTitle +
            "&your-user-id=" + userId;
    return sendHttpRequest(fullUrl);
  }

  public String retrievePreviousVersion(String userId, String documentTitle, int revisionNumber) {
    String fullUrl = SERVICE_IP + REVISION_URI + "?network-id=" + NETWORK_ID + "&document-name="+documentTitle +
            "&your-user-id=" + userId + "&revision-number=" + revisionNumber;
    return sendHttpRequest(fullUrl);
  }
  
  public String retrieveDocumentContents(String userId, String documentTitle) {
    String fullUrl = SERVICE_IP + DOWNLOAD_URI + "?network-id=" + NETWORK_ID + "&document-name="+documentTitle +
            "&your-user-id=" + userId;
    return sendHttpRequest(fullUrl);
  }

  public String retrieveDocumentDifferences(String userId, String fstDocumentTitle, String sndDocumentTitle) {
    String fullUrl = SERVICE_IP + DIFFERENCE_URI + "?network-id=" + NETWORK_ID + "&fst-doc-name="+ fstDocumentTitle
            + "&snd-doc-name=" + sndDocumentTitle + "&your-user-id=" + userId;
    return sendHttpRequest(fullUrl);
  }

  @GetMapping(value = "/usernames", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> retrieveDocumentStatsAPI() throws JsonProcessingException {
    ObjectMapper om = new ObjectMapper();
    return new ResponseEntity<>(om.writeValueAsString(retrieveUsernames()), HttpStatus.OK);
  }

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

  @PatchMapping(value = "/share-document", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> patchShareRequestAPI(@RequestParam(value = "user_id") String userId,
                                                @RequestParam(value = "doc_id") String documentId,
                                                @RequestParam(value = "new_user_id") String newUserId)
          throws JsonProcessingException {
    ObjectMapper om = new ObjectMapper();
    return new ResponseEntity<>(om.writeValueAsString(patchShareRequest(userId, documentId, newUserId)), HttpStatus.OK);
  }

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

  @PostMapping(value = "/upload-document", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> postUploadRequestAPI(@RequestParam(value = "user_id") String userId,
                                                @RequestParam(value = "doc_name") String documentName,
                                                @RequestParam(value = "contents") String contents)
          throws JsonProcessingException {
    ObjectMapper om = new ObjectMapper();
    return new ResponseEntity<>(om.writeValueAsString(postUploadRequest(userId, documentName, contents)), HttpStatus.OK);
  }

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