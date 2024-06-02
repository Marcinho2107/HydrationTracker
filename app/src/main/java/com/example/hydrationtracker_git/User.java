package com.example.hydrationtracker_git;



public class User {
    private String username;
    private int age;
    private int height;
    private String profileImagePath;
    private String gender;

    public User(String username, int age, int height, String profileImagePath, String gender) {
        this.username = username;
        this.age = age;
        this.height = height;
        this.profileImagePath = profileImagePath;
        this.gender = gender;
    }

    // Getter und Setter Methoden


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
