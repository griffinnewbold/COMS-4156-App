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
        System.out.println(jsonString);
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


  /*
  @GetMapping
  @GetMapping("/check-for-doc")
  @GetMapping("/see-previous")
  @GetMapping("/see-stats")
  @GetMapping("/download")
  @GetMapping("/compare")
  @PostMapping("/upload")
  @DeleteMapping("/delete")
  @PatchMapping("/share")
   */



}