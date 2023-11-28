package com.dev.sweproject;

import org.junit.jupiter.api.*;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserTests {

  private static User testUser;

  /**
   * Tests for successful creation of user.
   */
  @BeforeAll
  public static void setupUserForTesting() {
    testUser = new User();
  }

  /**
   * Tests for correct setting of user's name.
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
   * Tests for correct setting of user's email.
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
   * Tests for correct setting of user's password.
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
   * Tests for correct setting of user's gender.
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
   * Tests for correct setting of user's birthday.
   */
  @Test
  @Order(7)
  public void birthdayTest() {
    assertNull(testUser.getBirthday());
    testUser.setBirthday("10/30/2002");
    assertNotNull(testUser.getBirthday());
    assertEquals("10/30/2002", testUser.getBirthday());
  }

  /**
   * Tests for correct setting of user's profession.
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
   * Tests for correct formatting of user details returned as string.
   */
  @Test
  @Order(9)
  public void toStringTest() {
    String actualUser = testUser.toString();
    String expectedUser = "User [name=" + "Griffin" + ", email=" + "randomemail@service.com"
        + ", password=" + "mypass" + ", gender="
        + "male" + ", birthday=" + "10/30/2002"
        + ", profession=" + "Software Developer" + "]";
    assertEquals(expectedUser, actualUser);
  }
}
