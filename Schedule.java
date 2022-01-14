package com.example.mobilemarket;

public class Schedule {

    private String schedule_time;
    private String schedule_date;
    private String schedule_user;
    private String schedule_uid;
    private String schedule_status;
    private String patient_name;
    private String patient_gender;
    private String patient_id;
    private String patient_cell;

    public Schedule() {
    }

    public Schedule(String schedule_time, String schedule_date, String schedule_user, String schedule_uid, String schedule_status, String patient_name, String patient_gender, String patient_id, String patient_cell) {
        this.schedule_time = schedule_time;
        this.schedule_date = schedule_date;
        this.schedule_user = schedule_user;
        this.schedule_uid = schedule_uid;
        this.schedule_status = schedule_status;
        this.patient_name = patient_name;
        this.patient_gender = patient_gender;
        this.patient_id = patient_id;
        this.patient_cell = patient_cell;
    }

    public String getSchedule_time() {
        return schedule_time;
    }

    public void setSchedule_time(String schedule_time) {
        this.schedule_time = schedule_time;
    }

    public String getSchedule_date() {
        return schedule_date;
    }

    public void setSchedule_date(String schedule_date) {
        this.schedule_date = schedule_date;
    }

    public String getSchedule_user() {
        return schedule_user;
    }

    public void setSchedule_user(String schedule_user) {
        this.schedule_user = schedule_user;
    }

    public String getSchedule_uid() {
        return schedule_uid;
    }

    public void setSchedule_uid(String schedule_uid) {
        this.schedule_uid = schedule_uid;
    }

    public String getSchedule_status() {
        return schedule_status;
    }

    public void setSchedule_status(String schedule_status) {
        this.schedule_status = schedule_status;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }

    public String getPatient_gender() {
        return patient_gender;
    }

    public void setPatient_gender(String patient_gender) {
        this.patient_gender = patient_gender;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public String getPatient_cell() {
        return patient_cell;
    }

    public void setPatient_cell(String patient_cell) {
        this.patient_cell = patient_cell;
    }
}
