package com.dev.sweproject;

import org.junit.jupiter.api.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.test.context.junit4.*;

import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Firebase methods.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClientApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FirebaseTests {

  @Autowired
  private FirebaseService firebaseService = GlobalInfo.firebaseDataService;

  private final String collectionName = "testCollection";

  /**
   * Tests the successful creation of a network.
   */
  @Test
  @Order(1)
  public void testCreateCollectionSuccessfully() {
    assertDoesNotThrow(() -> {
      String result = firebaseService.createCollection(collectionName).get();
      assertEquals(collectionName, result);
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
    CompletableFuture<Object> result = firebaseService.addEntry(collectionName, key, value);

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
    CompletableFuture<Object> result = firebaseService.addEntry(collectionName, key, value);

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
    CompletableFuture<String> result = firebaseService.removeEntry(collectionName, key);

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
    CompletableFuture<Object> result = firebaseService.updateEntry(collectionName, key, newValue);

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
    CompletableFuture<Object> result = firebaseService.getEntry(collectionName, key);
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
    CompletableFuture<Object> result = firebaseService.getEntry(collectionName, key);
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
  /*
  @Test
  @Order(8)
  public void testConfirmLoginSuccess() {
    try {
      CompletableFuture<Boolean> result = firebaseService.confirmLogin("test@email", "aZ2");
      boolean isConfirmed = result.get();
      assertTrue(isConfirmed);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
  */

  /**
   * Tests for unsuccessful login.
   */
  @Test
  @Order(9)
  public void testConfirmLoginFailure() {
    try {
      CompletableFuture<Boolean> result = firebaseService.confirmLogin("test@email", "aZ2s");
      boolean isConfirmed = result.get();
      assertFalse(isConfirmed);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Tests for getSubcollectionNames() method.
   */
  /*
  @Test
  @Order(10)
  public void testGetSubCollections() {
    try {
      CompletableFuture<List<String>> result = firebaseService.getSubcollectionNames(collectionName);
      List<String> children = result.get();
      assertEquals("[test@email, testKey]", children.toString());
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
  //TODO use service to add test@email in testCollection
  */
}