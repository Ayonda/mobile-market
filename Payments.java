package com.example.mobilemarket;

public class Payments {
    String p_uri;
    String p_uid;
    String b_uid;
    String o_uid;

    public Payments() {
    }

    public Payments(String p_uri, String p_uid, String b_uid, String o_uid) {
        this.p_uri = p_uri;
        this.p_uid = p_uid;
        this.b_uid = b_uid;
        this.o_uid = o_uid;
    }

    public String getP_uri() {
        return p_uri;
    }

    public void setP_uri(String p_uri) {
        this.p_uri = p_uri;
    }

    public String getP_uid() {
        return p_uid;
    }

    public void setP_uid(String p_uid) {
        this.p_uid = p_uid;
    }

    public String getB_uid() {
        return b_uid;
    }

    public void setB_uid(String b_uid) {
        this.b_uid = b_uid;
    }

    public String getO_uid() {
        return o_uid;
    }

    public void setO_uid(String o_uid) {
        this.o_uid = o_uid;
    }
}
