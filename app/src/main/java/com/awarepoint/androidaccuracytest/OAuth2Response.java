package com.awarepoint.androidaccuracytest;

/**
 * Created by mspivey on 10/27/2016.
 * Copyright (c) 2016 Awarepoint. All rights reserved.
 */

public class OAuth2Response {

    private String _accessToken;
    private int _expiresIn;
    private String _refreshToken;
    private int _httpStatusCode;

    public String getAccessToken() { return this._accessToken; }
    public void setAccessToken(String accessToken) { this._accessToken = accessToken; }

    public int getExpiresIn() { return this._expiresIn; }
    public void setExpiresIn(int expiresIn) { this._expiresIn = expiresIn; }

    public String getRefreshToken() { return this._refreshToken; }
    public void setRefreshToken(String refreshToken) { this._refreshToken = refreshToken; }

    public int getHttpStatusCode() { return this._httpStatusCode; }
    public void setHttpStatusCode(int httpStatusCode) { this._httpStatusCode = httpStatusCode; }

}
