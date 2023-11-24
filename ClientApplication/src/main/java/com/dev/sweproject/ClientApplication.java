package com.dev.sweproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.*;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ClientApplication {

  private static FirebaseService firebaseDataService;

  public static void main(String[] args) {
    ApplicationContext context = SpringApplication.run(ClientApplication.class, args);
    firebaseDataService = context.getBean(FirebaseService.class);
    GlobalInfo.assignDatabase(firebaseDataService);
    GlobalInfo.assignTemplate(new RestTemplate());
  }

}
