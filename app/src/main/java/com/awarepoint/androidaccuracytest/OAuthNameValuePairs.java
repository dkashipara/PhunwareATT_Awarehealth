package com.awarepoint.androidaccuracytest;
/**
 * Created by mspivey on 10/27/2016.
 * Copyright (c) 2016 Awarepoint. All rights reserved.
 */

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

public class OAuthNameValuePairs extends ArrayList<NameValuePair>
{
    public String client_id;
    public String client_secret;
    public String scope;
    public String grant_type;

    public OAuthNameValuePairs(String clientId, String clientSecret, String apiKey, String grantType)
    {
        this.add(new BasicNameValuePair("client_id", clientId));
        this.add(new BasicNameValuePair("client_secret", clientSecret));
        this.add(new BasicNameValuePair("scope", "apikey=" + apiKey));
        this.add(new BasicNameValuePair("grant_type", grantType));
    }
}
