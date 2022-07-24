package com.example.fyp_22_s22_21_mobile.support;

public class reportData {
    private String vl_reportDate;
    private String vl_reporttitle;
    private String vl_reportdescription;
    private String vl_status;
    private String reportId;

    public reportData(String vl_reportDate, String vl_reporttitle, String vl_reportdescription, String vl_status, String reportId) {
        this.vl_reportDate = vl_reportDate;
        this.vl_reporttitle = vl_reporttitle;
        this.vl_reportdescription = vl_reportdescription;
        this.vl_status = vl_status;
        this.reportId = reportId;
    }

    public String getVl_reportDate() {
        return vl_reportDate;
    }

    public void setVl_reportDate(String vl_reportDate) {
        this.vl_reportDate = vl_reportDate;
    }

    public String getVl_reporttitle() {
        return vl_reporttitle;
    }

    public void setVl_reporttitle(String vl_reporttitle) {
        this.vl_reporttitle = vl_reporttitle;
    }

    public String getVl_reportdescription() {
        return vl_reportdescription;
    }

    public void setVl_reportdescription(String vl_reportdescription) {
        this.vl_reportdescription = vl_reportdescription;
    }

    public String getvl_status() {
        return vl_status;
    }

    public void setvl_status(String vl_status) {
        this.vl_status = vl_status;
    }

    public String getReportId(){ return reportId;}

    public void setReportId(String reportId){ this.reportId = reportId; }
}
