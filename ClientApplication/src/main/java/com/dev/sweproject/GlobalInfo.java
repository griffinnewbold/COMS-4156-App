package com.dev.sweproject;

import org.springframework.web.client.RestTemplate;

/**
 *  GlobalInfo is a class that holds valuable constants for use in other
 *  places in the program.
 */
public class GlobalInfo {

  /**
   * A global static reference to the set of database operations.
   */
  public static FirebaseService firebaseDataService;

  /**
   * A global static reference for making HTTP requests.
   */
  public static RestTemplate restTemplate;

  /**
   * A String constant for the IP address of the service.
   * If you are hosting the service non locally please alter this prior to execution.
   */
  public static final String SERVICE_IP = "http://34.86.161.172:8080";

  /**
   * A String constant for accessing a specific API endpoint: upload-doc.
   */
  public static final String UPLOAD_URI = "/upload-doc";

  /**
   * A String constant for accessing a specific API endpoint: share-document.
   */
  public static final String SHARE_URI = "/share-document";

  /**
   * A String constant for accessing a specific API endpoint: delete-doc.
   */
  public static final String DELETE_URI = "/delete-doc";

  /**
   * A String constant for accessing a specific API endpoint: check-for-doc.
   */
  public static final String SEARCH_URI = "/check-for-doc";

  /**
   * A String constant for accessing a specific API endpoint: see-previous-version.
   */
  public static final String REVISION_URI = "/see-previous-version";

  /**
   * A String constant for accessing a specific API endpoint: see-document-stats.
   */
  public static final String STATS_URI = "/see-document-stats";

  /**
   * A String constant for accessing a specific API endpoint: generate-difference-summary.
   */
  public static final String DIFFERENCE_URI = "/generate-difference-summary";

  /**
   * A String constant for accessing a specific API endpoint: download-doc.
   */
  public static final String DOWNLOAD_URI = "/download-doc";

  /**
   * A String constant for accessing a specific API endpoint: retrieve-docs.
   */
  public static final String RETRIEVE_URI = "/retrieve-docs";

  /**
   * A String constant for accessing a specific API endpoint: register-client.
   */
  public static final String REGISTRATION_URI = "/register-client";

  /**
   * A String constant for accessing a specific API endpoint: retrieve-doc-names.
   */
  public static final String DOC_NAME_URI = "/retrieve-doc-names";

  /**
   * A String representing the specific client using the app.
   * IF YOU ARE TESTING FOR CONCURRENCY PLEASE ALTER THIS BEFORE RUNNING A NEW INSTANCE.
   */
  public static final String NETWORK_ID = "secondIterationDemo";

  /**
   * Assigns the global variable for the database.
   *
   * @param fb The referenced used for database operations.
   */
  public static void assignDatabase(FirebaseService fb) {
    firebaseDataService = fb;
  }

  /**
   * Assigns the global variable for the rest template to perform HTTP operations.
   *
   * @param rt The referenced used for most HTTP operations.
   */
  public static void assignTemplate(RestTemplate rt) {
    restTemplate = rt;
  }
}
