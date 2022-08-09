package com.example.fyp_22_s22_21_mobile.support;

public class waterUsageData {

    private String vl_waterUsageHistoryDate;
    private String vl_waterUsageHistory;
    int i;

    public waterUsageData(int i, String vl_waterUsageHistoryDate, String vl_waterUsageHistory) {
        this.vl_waterUsageHistoryDate = vl_waterUsageHistoryDate;
        this.vl_waterUsageHistory = vl_waterUsageHistory;
        this.i = i;
    }

    public String getVl_waterUsageHistoryDate() {
        return vl_waterUsageHistoryDate;
    }

    public void setVl_waterUsageHistoryDate(String vl_waterUsageHistoryDate) {
        this.vl_waterUsageHistoryDate = vl_waterUsageHistoryDate;
    }

    public String getVl_waterUsageHistory() {
        return vl_waterUsageHistory;
    }

    public void setVl_waterUsageHistory(String vl_waterUsageHistory) {
        this.vl_waterUsageHistory = vl_waterUsageHistory;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }
}
