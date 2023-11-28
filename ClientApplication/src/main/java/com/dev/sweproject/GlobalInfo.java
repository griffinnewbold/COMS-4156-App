package com.dev.sweproject;

import org.springframework.web.client.RestTemplate;

/**
 * Utility class containing global information and constants for handling Firebase services.
 */
public class GlobalInfo {
  /** Represents the Firebase service for data operations. */
  public static FirebaseService firebaseDataService;

  /** Represents the RestTemplate used for REST API operations. */
  public static RestTemplate restTemplate;

  /** Base IP address for the service (backend). */
  public static final String SERVICE_IP = "http://127.0.0.1:8080";

  /** URI for uploading a document. */
  public static final String UPLOAD_URI = "/upload-doc";

  /** URI for sharing a document. */
  public static final String SHARE_URI = "/share-document";

  /** URI for deleting a document. */
  public static final String DELETE_URI = "/delete-doc";

  /** URI for checking if a document exists. */
  public static final String SEARCH_URI = "/check-for-doc";

  /** URI for seeing previous version of a document. */
  public static final String REVISION_URI = "/see-previous-version";

  /** URI for seeing document stats. */
  public static final String STATS_URI = "/see-document-stats";

  /** URI for generating difference summary between docs. */
  public static final String DIFFERENCE_URI = "/generate-difference-summary";

  /** URI for downloading a document. */
  public static final String DOWNLOAD_URI = "/download-doc";

  /** URI for retrieving a document. */
  public static final String RETRIEVE_URI = "/retrieve-docs";

  /** URI for registering a new client. */
  public static final String REGISTRATION_URI = "/register-client";

  /**
   * Default Network ID used for testing purposes.
   * (Alter this before running a new instance if testing for concurrency)
   */
  public static final String NETWORK_ID = "QCO1700369101620";

  /**
   * Assigns the provided FirebaseService to the firebaseDataService.
   *
   * @param fb The FirebaseService instance to be assigned.
   */
  public static void assignDatabase(final FirebaseService fb) {
    firebaseDataService = fb;
  }

  /**
   * Assigns the provided RestTemplate to the static field restTemplate.
   *
   * @param rt The RestTemplate instance to be assigned.
   */
  public static void assignTemplate(final RestTemplate rt) {
    restTemplate = rt;
  }

}
