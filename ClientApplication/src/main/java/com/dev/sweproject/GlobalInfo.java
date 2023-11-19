package com.dev.sweproject;

public class GlobalInfo {

  public static FirebaseService firebaseDataService;
  public static final String SERVICE_IP = "http://127.0.0.1:8080";
  public static final String UPLOAD_URI = "/upload-doc";
  public static final String SHARE_URI = "/share-doc";
  public static final String DELETE_URI = "/delete-doc";
  public static final String SEARCH_URI = "/check-for-doc";
  public static final String REVISION_URI = "/see-previous-version";
  public static final String STATS_URI = "/see-document-stats";
  public static final String DIFFERENCE_URI = "/generate-difference-summary";
  public static final String DOWNLOAD_URI = "/download-doc";


  //IF YOU ARE TESTING FOR CONCURRENCY PLEASE
  //ALTER THIS BEFORE RUNNING A NEW INSTANCE
  public static final String NETWORK_ID = "QCO1700369101620";

}
