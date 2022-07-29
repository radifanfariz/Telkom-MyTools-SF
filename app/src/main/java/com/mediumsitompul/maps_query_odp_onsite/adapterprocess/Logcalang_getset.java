package com.mediumsitompul.maps_query_odp_onsite.adapterprocess;

public class Logcalang_getset {
    private String no;
    private String timestamp;
    private String userid;

    public Logcalang_getset(String no, String timestamp, String userid) {
        this.no = no;
        this.timestamp = timestamp;
        this.userid = userid;
    }

    public String getNo() {
        return no;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getUserid() {
        return userid;
    }
}
