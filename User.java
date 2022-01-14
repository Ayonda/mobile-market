package com.example.mobilemarket;

public class User {
    String user_name;
    String user_id;
    String user_email;
    String user_cell;
    String user_location;
    String user_password;
    String user_unique_id;
    String last_seen;
    String status;

    public User() {
    }

    public User(String user_name, String user_id, String user_email, String user_cell, String user_location, String user_password, String user_unique_id, String last_seen, String status) {
        this.user_name = user_name;
        this.user_id = user_id;
        this.user_email = user_email;
        this.user_cell = user_cell;
        this.user_location = user_location;
        this.user_password = user_password;
        this.user_unique_id = user_unique_id;
        this.last_seen = last_seen;
        this.status = status;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_cell() {
        return user_cell;
    }

    public void setUser_cell(String user_cell) {
        this.user_cell = user_cell;
    }

    public String getUser_location() {
        return user_location;
    }

    public void setUser_location(String user_location) {
        this.user_location = user_location;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getUser_unique_id() {
        return user_unique_id;
    }

    public void setUser_unique_id(String user_unique_id) {
        this.user_unique_id = user_unique_id;
    }

    public String getLast_seen() {
        return last_seen;
    }

    public void setLast_seen(String last_seen) {
        this.last_seen = last_seen;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
