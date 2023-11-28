package com.dev.sweproject;

import static com.dev.sweproject.GlobalInfo.NETWORK_ID;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The `FirebaseService` class provides service methods related to the database (DB).
 * It utilizes `CompletableFuture<\Object>` for handling asynchronous operations and representing
 * future results. This class is part of the `java.util.concurrent` package and is used when you
 * want to retrieve a result once it's available or handle an error if the operation fails.
 *
 * <p>
 * Note: The Firebase API for Java used to primarily rely on Tasks, which have since been
 * deprecated, and `CompletableFuture` is one of the recommended alternatives.
 * </p>
 *
 * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html">
 *   CompletableFuture Documentation</a>
 */
@Service
public class FirebaseService {

  private final FirebaseApp firebaseApp;

  /**
   * Creates an instance of the Firebase Service.
   *
   * @param firebaseApp A `FirebaseApp` object representing the Firebase configuration.
   */
  @Autowired
  public FirebaseService(FirebaseApp firebaseApp) {
    this.firebaseApp = firebaseApp;
  }

  /**
   * Returns a reference to the Firebase Realtime Database. If multiple clients are using
   * this service, it will provide the same database reference instance.
   *
   * @return A reference to the Firebase Realtime Database.
   */
  public DatabaseReference getDatabaseReference() {
    FirebaseDatabase database = FirebaseDatabase.getInstance(firebaseApp);
    return database.getReference();
  }

  /**
   * Used to confirm whether the user can be logged into the app or not.
   *
   * @param email The email provided for login.
   * @param password The password provided for login
   * @return A CompletableFuture that will be completed with the true or false or
   *         completes exceptionally with an error message if an error occurs.
   */
  public CompletableFuture<Boolean> confirmLogin(String email, String password) {
    CompletableFuture<Boolean> future = new CompletableFuture<>();

    DatabaseReference databaseReference = getDatabaseReference();
    DatabaseReference userReference = databaseReference.child(NETWORK_ID).child(email);

    userReference.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()) {
          String storedPassword = dataSnapshot.child("password").getValue(String.class);

          if (storedPassword != null && storedPassword.equals(password)) {
            future.complete(true);
          } else {
            future.complete(false);
          }
        } else {
          future.complete(false);
        }
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {
        future.completeExceptionally(databaseError.toException());
      }
    });

    return future;
  }

  /**
   * Retrieves the list of users who exist within the network.
   *
   * @return A CompletableFuture that will be completed with the retrieved list or
   *         completes exceptionally with an error message if an error occurs.
   */
  public CompletableFuture<List<String>> getSubcollectionNames() {
    CompletableFuture<List<String>> future = new CompletableFuture<>();

    DatabaseReference databaseReference = getDatabaseReference();
    DatabaseReference networkReference = databaseReference.child(NETWORK_ID);

    networkReference.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        List<String> subcollectionNames = new ArrayList<>();

        for (DataSnapshot subcollectionSnapshot : dataSnapshot.getChildren()) {
          subcollectionNames.add(subcollectionSnapshot.getKey());
        }
        future.complete(subcollectionNames);
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {
        future.completeExceptionally(new RuntimeException("Error reading data from Firebase"));
      }
    });

    return future;
  }

  /**
   * Add an entry to the specified collection with the key-value association provided.
   *
   * @param collectionName A String denoting which collection this entry belongs to.
   * @param key A String denoting the key for the association.
   * @param value An object representing the value to be added.
   * @return A CompletableFuture that completes with the value upon successful addition
   *         or completes exceptionally with an error message.
   */
  public CompletableFuture<Object> addEntry(String collectionName, String key, Object value) {
    DatabaseReference databaseReference = getDatabaseReference();
    DatabaseReference collectionReference = databaseReference.child(collectionName);

    CompletableFuture<Object> resultFuture = new CompletableFuture<>();

    collectionReference.child(key).setValue(value, (error, ref) -> {
      if (error != null) {
        String errorMessage = "Data could not be added: " + error.getMessage();
        System.out.println(errorMessage);
        resultFuture.completeExceptionally(new RuntimeException(errorMessage));
      } else {
        System.out.println("Data added successfully.");
        resultFuture.complete(value);
      }
    });

    return resultFuture;
  }

  /**
   * Remove an entry from the specified collection with the given key.
   *
   * @param collectionName A String denoting which collection this entry belongs to.
   * @param key A String denoting the key for the entry to be removed.
   * @return A CompletableFuture that completes with a success message upon successful removal
   *         or completes exceptionally with an error message.
   */
  public CompletableFuture<String> removeEntry(String collectionName, String key) {
    DatabaseReference databaseReference = getDatabaseReference();
    DatabaseReference collectionReference = databaseReference.child(collectionName);

    CompletableFuture<String> resultFuture = new CompletableFuture<>();

    collectionReference.child(key).removeValue((error, ref) -> {
      if (error != null) {
        String errorMessage = "Data could not be removed: " + error.getMessage();
        System.out.println(errorMessage);
        resultFuture.completeExceptionally(new RuntimeException(errorMessage));
      } else {
        String successMessage = "Data removed successfully.";
        System.out.println(successMessage);
        resultFuture.complete(successMessage);
      }
    });

    return resultFuture;
  }

  /**
   * Create a collection in the database with the specified name.
   *
   * @param collectionName A String representing the name of the collection to be created.
   * @return A CompletableFuture that completes with the collection name upon successful
   *         creation or completes exceptionally with an error message.
   */
  public CompletableFuture<String> createCollection(String collectionName) {
    DatabaseReference databaseReference = getDatabaseReference();
    Map<String, Object> collectionData = new HashMap<>();
    collectionData.put(collectionName, "");

    CompletableFuture<String> resultFuture = new CompletableFuture<>();

    databaseReference.updateChildren(collectionData, (error, ref) -> {
      if (error != null) {
        String errorMessage = "Collection could not be created: " + error.getMessage();
        System.out.println(errorMessage);
        resultFuture.completeExceptionally(new RuntimeException(errorMessage));
      } else {
        System.out.println("Collection created successfully: " + collectionName);
        resultFuture.complete(collectionName);
      }
    });

    return resultFuture;
  }

  /**
   * Delete a collection from the database with the specified name.
   *
   * @param collectionName A String representing the name of the collection to be deleted.
   * @return A CompletableFuture that completes with the collection name upon successful
   *         deletion or completes exceptionally with an error message.
   */
  public CompletableFuture<String> deleteCollection(String collectionName) {
    DatabaseReference databaseReference = getDatabaseReference();
    DatabaseReference collectionReference = databaseReference.child(collectionName);

    CompletableFuture<String> resultFuture = new CompletableFuture<>();

    collectionReference.removeValue((error, ref) -> {
      if (error != null) {
        String errorMessage = "Error deleting documents in collection: " + error.getMessage();
        System.out.println(errorMessage);
        resultFuture.completeExceptionally(new RuntimeException(errorMessage));
      } else {
        System.out.println("Collection deleted successfully: " + collectionName);
        resultFuture.complete(collectionName);
      }
    });

    return resultFuture;
  }

  /**
   * Update an entry in the specified collection with the provided key : value association.
   *
   * @param collectionName A String denoting which collection this entry belongs to.
   * @param key A String denoting the key for the association to be updated.
   * @param newValue An object representing the new value to be assigned.
   * @return A CompletableFuture that completes with the new value upon successful update
   *         or completes exceptionally with an error message.
   */
  public CompletableFuture<Object> updateEntry(String collectionName, String key, Object newValue) {
    DatabaseReference databaseReference = getDatabaseReference();
    DatabaseReference collectionReference = databaseReference.child(collectionName);
    DatabaseReference entryReference = collectionReference.child(key);

    CompletableFuture<Object> resultFuture = new CompletableFuture<>();

    entryReference.setValue(newValue, (error, ref) -> {
      if (error != null) {
        String errorMessage = "Value could not be changed: " + error.getMessage();
        System.out.println(errorMessage);
        resultFuture.completeExceptionally(new RuntimeException(errorMessage));
      } else {
        System.out.println("Value was changed successfully: " + newValue);
        resultFuture.complete(newValue);
      }
    });

    return resultFuture;
  }

  /**
   * Retrieves the value associated with the specified collection and key.
   *
   * @param collectionName A String denoting which collection this entry belongs to.
   * @param key A String denoting the key for the association to be retrieved.
   * @return A CompletableFuture that will be completed with the retrieved value or
   *         completes exceptionally with an error message if the value is not found.
   */
  public CompletableFuture<Object> getEntry(String collectionName, String key) {
    CompletableFuture<Object> future = new CompletableFuture<>();

    DatabaseReference databaseReference = getDatabaseReference();
    DatabaseReference collectionReference = databaseReference.child(collectionName);
    DatabaseReference entryReference = collectionReference.child(key);

    entryReference.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        Object value = dataSnapshot.getValue();
        if (value != null) {
          System.out.println("The value has been successfully retrieved");
          System.out.println(value.toString());
          future.complete(value);
        } else {
          future.completeExceptionally(new RuntimeException("Value not found."));
        }
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {
        future.completeExceptionally(new RuntimeException(databaseError.getMessage()));
      }
    });

    return future;
  }
}
