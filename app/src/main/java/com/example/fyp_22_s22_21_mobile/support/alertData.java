package com.example.fyp_22_s22_21_mobile.support;

public class alertData {

    private String vl_Date;
    private String vl_title;
    private String vl_description;
    private String alert_Id;

    public alertData(String vl_Date, String vl_title, String vl_description, String alert_Id) {
        this.vl_Date = vl_Date;
        this.vl_title = vl_title;
        this.vl_description = vl_description;
        this.alert_Id = alert_Id;
    }

    public String getVl_Date() {
        return vl_Date;
    }

    public void setVl_Date(String vl_Date) {
        this.vl_Date = vl_Date;
    }

    public String getVl_title() {
        return vl_title;
    }

    public void setVl_title(String vl_title) {
        this.vl_title = vl_title;
    }

    public String getVl_description() {
        return vl_description;
    }

    public void setVl_description(String vl_description) {
        this.vl_description = vl_description;
    }

    public String getAlert_Id() {
        return alert_Id;
    }

    public void setAlert_Id(String alert_Id) {
        this.alert_Id = alert_Id;
    }
}
