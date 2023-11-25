package com.dev.sweproject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        String jsonString = retrieveDocuments(email.substring(0, email.indexOf('@')));
        //System.out.println(jsonString);
        //retrieveDocumentStats(email.substring(0, email.indexOf('@')), "common aliments");
        //retrievePreviousVersion(email.substring(0, email.indexOf('@')), "common aliments", 1);
        //retrieveDocumentContents(email.substring(0, email.indexOf('@')), "common aliments");
        //retrieveDocumentDifferences(email.substring(0, email.indexOf('@')), "common aliments",
        //        "jane doe birth");
        //sendShareRequest(email.substring(0, email.indexOf('@')), "common aliments",
        //        "mohsin@outlook");
        //sendDeleteRequest(email.substring(0, email.indexOf('@')), "john doe second diagnose");
        //sendServiceRegistrationRequest();
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

  public String sendShareRequest(String userId, String documentTitle, String newUserId) {
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
      System.out.println("Response from API: " + result);

    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
    }
    return "";
  }

  public String sendDeleteRequest(String userId, String documentTitle) {
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

      System.out.println("Response from API: " + result);

      //add logic to redirect user back to dashboard if result === "Your document was successfully deleted"

    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
    }
    return "";
  }

  public String sendUploadRequest(String userId, String documentTitle, MultipartFile contents) {
    return "";
  }

  public String sendServiceRegistrationRequest() {
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

      System.out.println("Response from API: " + result);

    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
    }
    return "";
  }

  private String sendHttpRequest(String url) {
    try {
      ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

      if (response.getStatusCode().is2xxSuccessful()) {
        System.out.println(response.getBody());
        return response.getBody();
      }

    } catch (HttpClientErrorException e) {
      System.out.println("HTTP error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
    } catch (Exception e) {
      System.out.println("Unexpected error: " + e.getMessage());
    }
    return "";
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

  /*
  @PostMapping("/upload")
   */
}