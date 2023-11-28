package com.dev.sweproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.client.RestTemplate;

/**
 * The main driver class for the application.
 */
@SpringBootApplication
public class ClientApplication {

  private static FirebaseService firebaseDataService;


  public static void main(String[] args) {
    try {
      ApplicationContext context = SpringApplication.run(ClientApplication.class, args);
      firebaseDataService = context.getBean(FirebaseService.class);
      GlobalInfo.assignDatabase(firebaseDataService);
      GlobalInfo.assignTemplate(new RestTemplate());
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

  }

}
