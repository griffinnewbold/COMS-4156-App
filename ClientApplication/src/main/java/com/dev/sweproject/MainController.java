package com.dev.sweproject;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import static com.dev.sweproject.GlobalInfo.*;

@Controller
public class MainController {

  @GetMapping({"/", "/index", "/home"})
  public String index() {
    return "index";
  }

  @GetMapping("/register")
  public String showForm(Model model) {
    User user = new User();
    model.addAttribute("user", user);

    List<String> listProfession = Arrays.asList("Doctor", "Nurse Practictioner", "Specialist");
    model.addAttribute("listProfession", listProfession);

    return "register_form";
  }

  @PostMapping("/register")
  public String submitForm(@ModelAttribute("user") User user) {
    System.out.println(user);
    return registerUser(user);
  }

  @GetMapping("/login")
  public String gotoLogin(Model model) {
    return "login_form";
  }

  @PostMapping("/login")
  public String attemptLogin(String email, String password, Model model) {
    try {
      CompletableFuture<Boolean> result = firebaseDataService.confirmLogin(email, password);
      boolean isSuccessful = result.get();

      if (isSuccessful) {
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
      CompletableFuture<Object> result = firebaseDataService.addEntry(NETWORK_ID, user.getEmail(), user);
      result.get();
      return "login_form";
    } catch (Exception e) {
      System.out.println(e.getMessage());
      System.out.println(e);
      return "error";
    }
  }

  /*
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