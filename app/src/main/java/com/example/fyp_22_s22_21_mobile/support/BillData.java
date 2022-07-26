package com.example.fyp_22_s22_21_mobile.support;

public class BillData {
    private String vl_BillMth;
    private String vl_BillYr;
    private String vl_BillAmt;
    private String vl_BillDue;
    private String BillId;

    public BillData(String vl_BillMth, String vl_BillYr, String vl_BillAmt, String vl_BillDue, String BillId) {
        this.vl_BillMth = vl_BillMth;
        this.vl_BillYr = vl_BillYr;
        this.vl_BillAmt = vl_BillAmt;
        this.vl_BillDue = vl_BillDue;
        this.BillId = BillId;
    }

    public String getvl_BillMth() {
        return vl_BillMth;
    }

    public void setvl_BillMth(String vl_BillMth) {
        this.vl_BillMth = vl_BillMth;
    }

    public String getvl_BillYr() {
        return vl_BillYr;
    }

    public void setvl_BillYr(String vl_BillYr) {
        this.vl_BillYr = vl_BillYr;
    }

    public String getvl_BillAmt() {return vl_BillAmt;}

    public void setvl_BillAmt(String vl_BillAmt) {
        this.vl_BillAmt = vl_BillAmt;
    }

    public String getvl_BillDue() {
        return vl_BillDue;
    }

    public void setvl_BillDue(String vl_BillDue) { this.vl_BillDue = vl_BillDue;}

    public String getBillId(){ return BillId;}

    public void setBillId(String BillId){ this.BillId = BillId; }
}
