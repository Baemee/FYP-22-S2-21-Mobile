package com.example.fyp_22_s22_21_mobile.support;

public class notificationData {

    String vl_notificationId;
    String vl_recipientId;
    String vl_date;
    String vl_notificationContent;

    public notificationData(String vl_notificationId,
                            String vl_recipientId,
                            String vl_date,
                            String vl_notificationContent)
    {
        this.vl_notificationId = vl_notificationId;
        this.vl_recipientId = vl_recipientId;
        this.vl_date = vl_date;
        this.vl_notificationContent = vl_notificationContent;
    }

    public String getVl_notificationId() {
        return vl_notificationId;
    }

    public void setVl_notificationId(String vl_notificationId) {
        this.vl_notificationId = vl_notificationId;
    }

    public String getvl_recipientId() {
        return vl_recipientId;
    }

    public void vl_recipientId(String vl_recipientId) {
        this.vl_recipientId = vl_recipientId;
    }

    public String getVl_date() {
        return vl_date;
    }

    public void setVl_date(String vl_date) {
        this.vl_date = vl_date;
    }

    public String getVl_notificationContent() {
        return vl_notificationContent;
    }

    public void setVl_notificationContent(String vl_notificationContent) {
        this.vl_notificationContent = vl_notificationContent;
    }
}
