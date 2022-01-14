package com.example.mobilemarket;

public class Admin {
    private String admin_name;
    private String admin_uid;
    private String admin_email;
    private String admin_password;
    private String admin_cell;
    private String admin_location;
    private String admin_bank;
    private String admin_account_type;
    private String admin_account_name;
    private String admin_account_number;

    public Admin() {
    }

    public Admin(String admin_name, String admin_uid, String admin_email, String admin_password, String admin_cell, String admin_location, String admin_bank, String admin_account_type, String admin_account_name, String admin_account_number) {
        this.admin_name = admin_name;
        this.admin_uid = admin_uid;
        this.admin_email = admin_email;
        this.admin_password = admin_password;
        this.admin_cell = admin_cell;
        this.admin_location = admin_location;
        this.admin_bank = admin_bank;
        this.admin_account_type = admin_account_type;
        this.admin_account_name = admin_account_name;
        this.admin_account_number = admin_account_number;
    }

    public String getAdmin_name() {
        return admin_name;
    }

    public void setAdmin_name(String admin_name) {
        this.admin_name = admin_name;
    }

    public String getAdmin_uid() {
        return admin_uid;
    }

    public void setAdmin_uid(String admin_uid) {
        this.admin_uid = admin_uid;
    }

    public String getAdmin_email() {
        return admin_email;
    }

    public void setAdmin_email(String admin_email) {
        this.admin_email = admin_email;
    }

    public String getAdmin_password() {
        return admin_password;
    }

    public void setAdmin_password(String admin_password) {
        this.admin_password = admin_password;
    }

    public String getAdmin_cell() {
        return admin_cell;
    }

    public void setAdmin_cell(String admin_cell) {
        this.admin_cell = admin_cell;
    }

    public String getAdmin_location() {
        return admin_location;
    }

    public void setAdmin_location(String admin_location) {
        this.admin_location = admin_location;
    }

    public String getAdmin_bank() {
        return admin_bank;
    }

    public void setAdmin_bank(String admin_bank) {
        this.admin_bank = admin_bank;
    }

    public String getAdmin_account_type() {
        return admin_account_type;
    }

    public void setAdmin_account_type(String admin_account_type) {
        this.admin_account_type = admin_account_type;
    }

    public String getAdmin_account_name() {
        return admin_account_name;
    }

    public void setAdmin_account_name(String admin_account_name) {
        this.admin_account_name = admin_account_name;
    }

    public String getAdmin_account_number() {
        return admin_account_number;
    }

    public void setAdmin_account_number(String admin_account_number) {
        this.admin_account_number = admin_account_number;
    }
}
