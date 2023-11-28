package com.dev.sweproject;

import java.sql.Date;

/**
 * Represents a User entity with associated properties.
 */
public class User {

  private String name;
  private String email;
  private String password;
  private String gender;
  private String birthday;
  private String profession;

  /**
   * Get the name of this user.
   *
   * @return The name of the user.
   */
  public String getName() {
    return name;
  }

  /**
   * Set the name of this user.
   *
   * @param name String name of the user.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Get the email of this user.
   *
   * @return The email of the user.
   */
  public String getEmail() {
    return email;
  }

  /**
   * Set the email of this user.
   *
   * @param email String email of the user.
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Get the password of this user.
   *
   * @return The password of the user.
   */
  public String getPassword() {
    return password;
  }

  /**
   * Set the password of this user.
   *
   * @param password String password of the user.
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Get the gender of this user.
   *
   * @return The gender of the user.
   */
  public String getGender() {
    return gender;
  }

  /**
   * Set the gender of this user.
   *
   * @param gender String gender of the user.
   */
  public void setGender(String gender) {
    this.gender = gender;
  }

  /**
   * Get the birthday of this user.
   *
   * @return The birthday of the user.
   */
  public String getBirthday() {
    return birthday;
  }

  /**
   * Set the birthday of this user.
   *
   * @param birthday String birthday of the user.
   */
  public void setBirthday(String birthday) {
    this.birthday = birthday;
  }

  /**
   * Get the profession of this user.
   *
   * @return The profession of the user.
   */
  public String getProfession() {
    return profession;
  }

  /**
   * Set the profession of this user.
   *
   * @param profession String profession of the user.
   */
  public void setProfession(String profession) {
    this.profession = profession;
  }

  /**
   * Return details of user as string.
   *
   * @return The details of the user as string.
   */
  @Override
  public String toString() {
    return "User [name=" + name + ", email=" + email + ", password=" + password + ", gender="
        + gender + ", birthday=" + birthday + ", profession=" + profession + "]";
  }

  /**
   * Check the validity of user and its properties.
   *
   * @param user User object whose validity is to be checked.
   * @return Boolean value of whether user is valid or not.
   */
  public static boolean isValidUser(User user) {
    return !user.getEmail().isEmpty() && !user.getName().isEmpty() && !user.getProfession().isEmpty()
            && !user.getBirthday().isEmpty() && !user.getGender().isEmpty() && !user.getPassword().isEmpty()
            && user.getEmail().contains(".") && user.getBirthday().length() == 10 && user.getBirthday().contains("-")
            && user.getBirthday().substring(user.getBirthday().indexOf("-")+1).contains("-");


  }

}