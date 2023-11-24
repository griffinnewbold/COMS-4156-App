package com.dev.sweproject;

import java.sql.Date;

public class User {

  private String name;
  private String email;
  private String password;
  private String gender;
  private String birthday;
  private String profession;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getBirthday() {
    return birthday;
  }

  public void setBirthday(String birthday) {
    this.birthday = birthday;
  }

  public String getProfession() {
    return profession;
  }

  public void setProfession(String profession) {
    this.profession = profession;
  }

  @Override
  public String toString() {
    return "User [name=" + name + ", email=" + email + ", password=" + password + ", gender="
        + gender + ", birthday=" + birthday + ", profession=" + profession + "]";
  }

}