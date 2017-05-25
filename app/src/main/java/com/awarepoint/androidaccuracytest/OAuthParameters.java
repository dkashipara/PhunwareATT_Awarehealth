package com.awarepoint.androidaccuracytest;
/**
 * Created by mspivey on 10/27/2016.
 * Copyright (c) 2016 Awarepoint. All rights reserved.
 */

public class OAuthParameters
{
    String _url;
    String _apiKey;
    String _username;
    String _password;

    public String getUrl() { return this._url; }
    public String getApiKey() { return this._apiKey; }
    public String getUserName() { return this._username; }
    public String getPassword() { return this._password; }

    public OAuthParameters(String url, String apiKey, String username, String password)
    {
        this._url = url;
        this._apiKey = apiKey;
        this._username = username;
        this._password = password;
    }
}
