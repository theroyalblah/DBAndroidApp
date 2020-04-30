package com.example.databaseproject;

import java.util.Date;

public class Material {
    public int id;
    public String title;
    public String author;
    public String type;
    public String url;
    public String assigned_date;
    public String notes;

    Material(String title, String author, String type, String url, String assigned_date, String notes) {
        this.title = title;
        this.author = author;
        this.type = type;
        this.url = url;
        this.assigned_date = assigned_date;
        this.notes = notes;
    }
}
