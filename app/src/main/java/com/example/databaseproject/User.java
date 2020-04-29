package com.example.databaseproject;

import org.json.JSONArray;
import org.json.JSONException;
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
        name = uname;
        email = uemail;
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
   public String getEmail() throws JSONException {
        if (email == null) {
            String sql = String.format("SELECT email FROM users WHERE user_id='%d';", id);
            JSONArray response = QueryBuilder.performQuery(sql);
            JSONObject res = QueryBuilder.getJSONObject(response, 0);
            if (res != null) {
                email = res.getString("email");
            } else {
                email = null;
            }
        }
        return email;
    }
   public String getName() throws JSONException {
       if (name == null) {
           String sql = String.format("SELECT name FROM users WHERE id='%d';", id);
           JSONArray response = QueryBuilder.performQuery(sql);
           JSONObject res = QueryBuilder.getJSONObject(response, 0);
           if (res != null) {
               name = res.getString("name");
           } else {
               name = null;
           }
       }
       return name;
   }
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
