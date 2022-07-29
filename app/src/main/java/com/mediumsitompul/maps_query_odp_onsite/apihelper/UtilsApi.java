package com.mediumsitompul.maps_query_odp_onsite.apihelper;

import com.mediumsitompul.maps_query_odp_onsite.Maps_Constants;

public class UtilsApi {
    // 10.0.2.2 ini adalah localhost.
//    public static final String BASE_URL_API = "http://192.168.43.247/login/";

    // Mendeklarasikan Interface BaseApiService
    public static BaseApiService getAPIService(){
        return RetrofitClient.getClient(Maps_Constants.BASE_URL_API).create(BaseApiService.class);
    }
}
