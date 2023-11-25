package com.dev.sweproject;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;

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

  public String retrieveDocuments(String userId) {
    String fullUrl = SERVICE_IP + RETRIEVE_URI + "?network-id=" + NETWORK_ID + "&user-id=" + userId;

    try {
      ResponseEntity<String> response = restTemplate.getForEntity(fullUrl, String.class);

      if (response.getStatusCode().is2xxSuccessful()) {
        return response.getBody();
      }

    } catch (HttpClientErrorException e) {
      System.out.println("HTTP error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
    } catch (Exception e) {
      System.out.println("Unexpected error: " + e.getMessage());
    }
    return "";
  }

  public String retrieveDocumentStats(String userId, String documentTitle) {
    String fullUrl = SERVICE_IP + STATS_URI + "?network-id=" + NETWORK_ID + "&document-name="+documentTitle +
            "&your-user-id=" + userId;
    try {
      ResponseEntity<String> response = restTemplate.getForEntity(fullUrl, String.class);

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

  public String retrievePreviousVersion(String userId, String documentTitle, int revisionNumber) {
    String fullUrl = SERVICE_IP + REVISION_URI + "?network-id=" + NETWORK_ID + "&document-name="+documentTitle +
            "&your-user-id=" + userId + "&revision-number=" + revisionNumber;
    try {
      ResponseEntity<String> response = restTemplate.getForEntity(fullUrl, String.class);

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

  public String retrieveDocumentContents(String userId, String documentTitle) {
    String fullUrl = SERVICE_IP + DOWNLOAD_URI + "?network-id=" + NETWORK_ID + "&document-name="+documentTitle +
            "&your-user-id=" + userId;
    try {
      ResponseEntity<String> response = restTemplate.getForEntity(fullUrl, String.class);

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

  public String retrieveDocumentDifferences(String userId, String fstDocumentTitle, String sndDocumentTitle) {
    String fullUrl = SERVICE_IP + DIFFERENCE_URI + "?network-id=" + NETWORK_ID + "&fst-doc-name="+ fstDocumentTitle
            + "&snd-doc-name=" + sndDocumentTitle + "&your-user-id=" + userId;
    try {
      ResponseEntity<String> response = restTemplate.getForEntity(fullUrl, String.class);

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

  /*
  @PostMapping("/upload")
  @DeleteMapping("/delete")
  @PatchMapping("/share")
   */



}