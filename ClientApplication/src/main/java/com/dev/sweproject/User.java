package com.dev.sweproject;

/**
 *  The User class contains information pertaining to a user.
 */
public class User {

  private String name;
  private String email;
  private String password;
  private String gender;
  private String birthday;
  private String profession;

  /**
   * Retrieves the name attribute of the calling Object.
   *
   * @return A String representing the name.
   */
  public String getName() {
    return name;
  }

  /**
   * Reassigns the name attribute of the calling Object.
   *
   * @param name The new name for the user.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Retrieves the email attribute of the calling Object.
   *
   * @return A String representing the email.
   */
  public String getEmail() {
    return email;
  }

  /**
   * Reassigns the email attribute of the calling Object.
   *
   * @param email The new email for the user.
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Retrieves the password attribute of the calling Object.
   *
   * @return A String representing the password.
   */
  public String getPassword() {
    return password;
  }

  /**
   * Reassigns the password attribute of the calling Object.
   *
   * @param password The new email for the user.
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Retrieves the gender attribute of the calling Object.
   *
   * @return A String representing the gender.
   */
  public String getGender() {
    return gender;
  }

  /**
   * Reassigns the gender attribute of the calling Object.
   *
   * @param gender The new email for the user.
   */
  public void setGender(String gender) {
    this.gender = gender;
  }

  /**
   * Retrieves the birthday attribute of the calling Object.
   *
   * @return A String representing the name.
   */
  public String getBirthday() {
    return birthday;
  }

  /**
   * Reassigns the birthday attribute of the calling Object.
   *
   * @param birthday The new birthday for the user.
   */
  public void setBirthday(String birthday) {
    this.birthday = birthday;
  }

  /**
   * Retrieves the profession attribute of the calling Object.
   *
   * @return A String representing the name.
   */
  public String getProfession() {
    return profession;
  }

  /**
   * Reassigns the email attribute of the calling Object.
   *
   * @param profession The new email for the user.
   */
  public void setProfession(String profession) {
    this.profession = profession;
  }

  /**
   * Converts the user to a String for representation purposes.
   *
   * @return A String representing the user.
   */
  @Override
  public String toString() {
    return "User [name=" + name + ", email=" + email + ", password=" + password + ", gender="
        + gender + ", birthday=" + birthday + ", profession=" + profession + "]";
  }

  /**
   * Determines if the specified user is valid or not.
   *
   * @param user A User object used for testing.
   * @return True if user is considered valid otherwise false.
   */
  public static boolean isValidUser(User user) {
    return nonNullAttributes(user)
        && !user.getEmail().isEmpty()
        && !user.getName().isEmpty()
        && !user.getProfession().isEmpty()
        && !user.getBirthday().isEmpty()
        && !user.getGender().isEmpty()
        && !user.getPassword().isEmpty()
        && user.getEmail().contains(".")
        && user.getBirthday().length() == 10
        && user.getBirthday().contains("-")
        && user.getBirthday().substring(user.getBirthday().indexOf("-") + 1).contains("-");
  }

  private static boolean nonNullAttributes(User user) {
    return user.birthday != null
        && user.email != null
        && user.name != null
        && user.password != null
        && user.profession != null
        && user.gender != null;
  }
}