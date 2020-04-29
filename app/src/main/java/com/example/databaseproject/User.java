package com.example.databaseproject;

public class User {
    private int id;
    private String name;
    private String email;
    private String userType;

    User(int uid) {
        id = uid;
    }

    User(int uid, String uname, String uemail) {
        id = uid;
        uname = name;
        uemail = email;
        userType = "student";
    }

   User(int uid, String uname, String uemail, String uType) {
       id = uid;
       name = uname;
       email = uemail;
       userType = uType;
   }

   public int getId() {
       return id;
   }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getUserType() { return userType; }
}
