package com.dev.sweproject;

import org.springframework.web.client.RestTemplate;

public class GlobalInfo {
  public static FirebaseService firebaseDataService;
  public static RestTemplate restTemplate;
  public static final String SERVICE_IP = "http://127.0.0.1:8080";
  public static final String UPLOAD_URI = "/upload-doc";
  public static final String SHARE_URI = "/share-document";
  public static final String DELETE_URI = "/delete-doc";
  public static final String SEARCH_URI = "/check-for-doc";
  public static final String REVISION_URI = "/see-previous-version";
  public static final String STATS_URI = "/see-document-stats";
  public static final String DIFFERENCE_URI = "/generate-difference-summary";
  public static final String DOWNLOAD_URI = "/download-doc";
  public static final String RETRIEVE_URI = "/retrieve-docs";
  public static final String REGISTRATION_URI = "/register-client";
  public static final String DOC_NAME_URI = "/retrieve-doc-names";


  //IF YOU ARE TESTING FOR CONCURRENCY PLEASE
  //ALTER THIS BEFORE RUNNING A NEW INSTANCE
  public static final String NETWORK_ID = "QCO1700369101620";

  public static void assignDatabase(FirebaseService fb) {
    firebaseDataService = fb;
  }
  public static void assignTemplate(RestTemplate rt) { restTemplate = rt;}

}
