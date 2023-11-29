package com.dev.sweproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;

/**
 * A JUnit Test Suite that tests all major methods within User.java.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserTests {

  private static User testUser;

  /**
   *  Setups a testUser for use in all the proceeding tests.
   */
  @BeforeAll
  public static void setupUserForTesting() {
    testUser = new User();
  }

  /**
   * Tests the setName() and getName() methods.
   */
  @Test
  @Order(1)
  public void nameTest() {
    assertNull(testUser.getName());
    testUser.setName("Griffin");
    assertNotNull(testUser.getName());
    assertEquals("Griffin", testUser.getName());
  }

  /**
   * Tests the setEmail() and getEmail() methods.
   */
  @Test
  @Order(2)
  public void emailTest() {
    assertNull(testUser.getEmail());
    testUser.setEmail("randomemail@service.com");
    assertNotNull(testUser.getEmail());
    assertEquals("randomemail@service.com", testUser.getEmail());
  }

  /**
   * Tests the setPassword() and getPassword() methods.
   */
  @Test
  @Order(3)
  public void passwordTest() {
    assertNull(testUser.getPassword());
    testUser.setPassword("mypass");
    assertNotNull(testUser.getPassword());
    assertEquals("mypass", testUser.getPassword());
  }

  /**
   * Tests the setGender() and getGender() methods.
   */
  @Test
  @Order(4)
  public void genderTest() {
    assertNull(testUser.getGender());
    testUser.setGender("male");
    assertNotNull(testUser.getGender());
    assertEquals("male", testUser.getGender());
  }

  /**
   * Tests the setBirthday() and getBirthday() methods.
   */
  @Test
  @Order(7)
  public void birthdayTest() {
    assertNull(testUser.getBirthday());
    testUser.setBirthday("10-30-2002");
    assertNotNull(testUser.getBirthday());
    assertEquals("10-30-2002", testUser.getBirthday());
  }

  /**
   * Tests the setProfession() and getProfession() methods.
   */
  @Test
  @Order(8)
  public void professionTest() {
    assertNull(testUser.getProfession());
    testUser.setProfession("Software Developer");
    assertNotNull(testUser.getProfession());
    assertEquals("Software Developer", testUser.getProfession());
  }

  /**
   * Tests the toString() method.
   */
  @Test
  @Order(9)
  public void toStringTest() {
    String actualUser = testUser.toString();
    String expectedUser = "User [name=" + "Griffin" + ", email=" + "randomemail@service.com"
        + ", password=" + "mypass" + ", gender="
        + "male" + ", birthday=" + "10-30-2002"
        + ", profession=" + "Software Developer" + "]";
    assertEquals(expectedUser, actualUser);
  }

  /**
   * Tests the isValidUser method, expects success.
   */
  @Test
  @Order(10)
  public void isValidTestSuccess() {
    assertTrue(User.isValidUser(testUser));
  }

  /**
   * Tests the isValidUser method, expects failure.
   */
  @Test
  @Order(11)
  public void isValidTestFailure() {
     assertFalse(User.isValidUser(new User()));
  }
}
