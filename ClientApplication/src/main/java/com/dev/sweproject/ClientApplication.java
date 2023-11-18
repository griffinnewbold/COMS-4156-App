package com.dev.sweproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.*;

@SpringBootApplication
public class ClientApplication {

  public static void main(String[] args) {
    ApplicationContext context = SpringApplication.run(ClientApplication.class, args);
    GlobalInfo.firebaseDataService = context.getBean(FirebaseService.class);
  }

}
