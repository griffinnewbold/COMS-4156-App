package com.dev.sweproject;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Tests for Firebase methods.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClientApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FirebaseTests {

  @Autowired
  private FirebaseService firebaseService = GlobalInfo.firebaseDataService;

  private final String collection = "testCollection";

  /**
   * Tests the successful creation of a network.
   */
  @Test
  @Order(1)
  public void testCreateCollectionSuccessfully() {
    assertDoesNotThrow(() -> {
      String result = firebaseService.createCollection(collection).get();
      assertEquals(collection, result);
    });
  }

  /**
   * Tests for successful adding to the database.
   */
  @Test
  @Order(2)
  public void testAddEntrySuccess() {
    String key = "testKey";
    String value = "testValue";
    CompletableFuture<Object> result = firebaseService.addEntry(collection, key, value);

    try {
      String resultValue = (String) result.get();
      assertNotNull(resultValue);
      assertEquals("testValue", resultValue);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Tests for successful adding to the database.
   */
  @Test
  @Order(3)
  public void testAddEntrySuccess2() {
    String key = "testKey2";
    String value = "testValue2";
    CompletableFuture<Object> result = firebaseService.addEntry(collection, key, value);

    try {
      String resultValue = (String) result.get();
      assertNotNull(resultValue);
      assertEquals("testValue2", resultValue);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Tests for successful removal from DB.
   */
  @Test
  @Order(4)
  public void testRemoveEntrySuccess() {
    String key = "testKey2";
    CompletableFuture<String> result = firebaseService.removeEntry(collection, key);

    try {
      String resultValue = result.get();
      assertNotNull(resultValue);
      assertEquals("Data removed successfully.", resultValue);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Tests for successful update.
   */
  @Test
  @Order(5)
  public void testUpdateEntrySuccess() {
    String key = "testKey";
    String newValue = "newTestValue";
    CompletableFuture<Object> result = firebaseService.updateEntry(collection, key, newValue);

    try {
      Object resultValue = result.get();
      assertNotNull(resultValue);
      assertEquals(newValue, resultValue);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Tests for successful get.
   */
  @Test
  @Order(6)
  public void testGetEntrySuccess() {
    String key = "testKey";
    CompletableFuture<Object> result = firebaseService.getEntry(collection, key);
    try {
      Object resultValue = result.get();
      assertNotNull(resultValue);
      assertEquals("newTestValue", resultValue);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Tests for unsuccessful get.
   */
  @Test
  @Order(7)
  public void testGetEntryFailure() {
    String key = "testKey3";
    CompletableFuture<Object> result = firebaseService.getEntry(collection, key);
    try {
      Object resultValue = result.get();
      assertNotNull(resultValue);
      assertEquals(new RuntimeException("Value not found."), resultValue);
    } catch (ExecutionException e) {
      System.out.println("Successfully caught: " + e);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Tests for a successful login.
   */
  @Test
  @Order(8)
  public void testConfirmLoginSuccess() {
    try {
      HashMap<String, String> value = new HashMap<>();
      value.put("birthday", "01-11-1111");
      value.put("email", "test@email.com");
      value.put("gender", "Male");
      value.put("name", "Tester");
      value.put("password", "aZ2");
      value.put("profession", "Doctor");
      String key = "test@email";
      firebaseService.addEntry(collection, key, value);
      CompletableFuture<Boolean> result = firebaseService.confirmLogin(key, "aZ2",
          collection);
      boolean isConfirmed = result.get();
      assertTrue(isConfirmed);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Tests for unsuccessful login.
   */
  @Test
  @Order(9)
  public void testConfirmLoginFailure() {
    try {
      CompletableFuture<Boolean> result = firebaseService.confirmLogin("test@email",
          "aZ2s", collection);
      boolean isConfirmed = result.get();
      assertFalse(isConfirmed);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Tests for getSubcollectionNames() method.
   */
  @Test
  @Order(10)
  public void testGetSubCollections() {
    try {
      CompletableFuture<List<String>> result = firebaseService.getSubcollectionNames(collection);
      List<String> children = result.get();
      assertEquals("[test@email, testKey]", children.toString());
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
}