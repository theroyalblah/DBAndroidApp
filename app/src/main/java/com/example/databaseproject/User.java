package com.example.databaseproject;

import org.json.JSONArray;
import org.json.JSONObject;

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
   public String getEmail() { return email; }
    public String getName() { return name; }
    public String getUserType() {
        if (userType == null) {
            String sql = String.format("SELECT * FROM admins WHERE admin_id='%d';", id);
            JSONArray response = QueryBuilder.performQuery(sql);
            if(response != null) {
                userType = "admin";
            } else {
                sql = String.format("SELECT * FROM parents WHERE parent_id='%d';", id);
                response = QueryBuilder.performQuery(sql);
                if(response != null) {
                    userType = "parent";
                } else {
                    userType = "student";
                }
            }
        }
        return userType;
    }
}
