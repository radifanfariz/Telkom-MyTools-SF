package com.mediumsitompul.maps_query_odp_onsite.adapterprocess;

public class Datasales_getset {
    private String idx;
    private String timestamp;
    private String cust_name;
    private String myir_sc;
    private String status;

    public Datasales_getset(String idx, String timestamp, String cust_name, String myir_sc, String status) {
        this.idx = idx;
        this.timestamp = timestamp;
        this.cust_name = cust_name;
        this.myir_sc = myir_sc;
        this.status = status;
    }

    public String getIdx() {
        return idx;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getCust_name() {
        return cust_name;
    }

    public String getMyir_sc() {
        return myir_sc;
    }

    public String getStatus() {
        return status;
    }
}
