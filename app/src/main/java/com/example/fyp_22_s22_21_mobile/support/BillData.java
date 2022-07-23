package com.example.fyp_22_s22_21_mobile.support;

public class BillData {
    private String vl_BillDate;
    private String vl_BillAmt;
    private String vl_BillUsage;
    private String BillId;

    public BillData(String vl_BillDate, String vl_BillAmt, String vl_BillUsage, String BillId) {
        this.vl_BillDate = vl_BillDate;
        this.vl_BillAmt = vl_BillAmt;
        this.vl_BillUsage = vl_BillUsage;
        this.BillId = BillId;
    }

    public String getvl_BillDate() {
        return vl_BillDate;
    }

    public void setvl_BillDate(String vl_BillDate) {
        this.vl_BillDate = vl_BillDate;
    }

    public String getvl_BillAmt() {
        return vl_BillAmt;
    }

    public void setvl_BillAmt(String vl_BillAmt) {
        this.vl_BillAmt = vl_BillAmt;
    }

    public String getvl_BillUsage() {
        return vl_BillUsage;
    }

    public void setvl_BillUsage(String vl_BillUsage) { this.vl_BillUsage = vl_BillUsage;}

    public String getBillId(){ return BillId;}

    public void setBillId(String BillId){ this.BillId = BillId; }
}
