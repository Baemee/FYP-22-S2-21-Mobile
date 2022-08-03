package com.example.fyp_22_s22_21_mobile.support;

public class BillData {

    private String vl_BillAmt;
    private String vl_BillDue;
    private String BillId;

    public BillData(String vl_BillAmt, String vl_BillDue, String BillId) {

        this.vl_BillAmt = vl_BillAmt;
        this.vl_BillDue = vl_BillDue;
        this.BillId = BillId;
    }

    public String getVl_BillAmt() {
        return vl_BillAmt;
    }

    public void setVl_BillAmt(String vl_BillAmt) {
        this.vl_BillAmt = vl_BillAmt;
    }

    public String getVl_BillDue() {
        return vl_BillDue;
    }

    public void setVl_BillDue(String vl_BillDue) {
        this.vl_BillDue = vl_BillDue;
    }

    public String getBillId() {
        return BillId;
    }

    public void setBillId(String billId) {
        BillId = billId;
    }
}
