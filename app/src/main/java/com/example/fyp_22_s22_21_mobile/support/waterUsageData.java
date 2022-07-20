package com.example.fyp_22_s22_21_mobile.support;

public class waterUsageData {

    private String vl_waterUsageHistoryDate;
    private int vl_waterUsageHistory;

    public waterUsageData(String vl_waterUsageHistoryDate, int vl_waterUsageHistory) {
        this.vl_waterUsageHistoryDate = vl_waterUsageHistoryDate;
        this.vl_waterUsageHistory = vl_waterUsageHistory;
    }

    public String getVl_waterUsageHistoryDate() {
        return vl_waterUsageHistoryDate;
    }

    public void setVl_waterUsageHistoryDate(String vl_waterUsageHistoryDate) {
        this.vl_waterUsageHistoryDate = vl_waterUsageHistoryDate;
    }

    public int getVl_waterUsageHistory() {
        return vl_waterUsageHistory;
    }

    public void setVl_waterUsageHistory(int vl_waterUsageHistory) {
        this.vl_waterUsageHistory = vl_waterUsageHistory;
    }
}
