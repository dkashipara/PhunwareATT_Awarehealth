package com.awarepoint.androidaccuracytest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by mspivey on 10/27/2016.
 * Copyright (c) 2016 Awarepoint. All rights reserved.
 */

public class OAuth2Client {

    public OAuth2Response SubmitRequest(OAuthParameters oauthParams)
    {
        String url = oauthParams.getUrl();
        String apiKey = oauthParams.getApiKey();
        String username = oauthParams.getUserName();
        String password = oauthParams.getPassword();

        OAuth2Response response = new OAuth2Response();
        String responseString = null;
        HttpResponse httpResponse = null;

        try {
            // http client
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpResponseEntity = null;

            HttpPost httpPost = new HttpPost(url);

            // adding post params
            OAuthNameValuePairs body = new OAuthNameValuePairs(username, password, apiKey, "client_credentials");
            httpPost.setEntity(new UrlEncodedFormEntity(body));

            httpResponse = httpClient.execute(httpPost);

            httpResponseEntity = httpResponse.getEntity();

            StringBuilder sb = new StringBuilder();
            try {
                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(httpResponseEntity.getContent()), 65728);
                String line = null;

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }
            catch (IOException e) { e.printStackTrace(); }
            catch (Exception e) { e.printStackTrace(); }

            responseString = sb.toString();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (responseString != null)
        {
            try {
                //Parse HTTP response string JSON in to an object.
                JSONObject json = new JSONObject(responseString);
                response.setAccessToken(json.getString("access_token"));
                response.setExpiresIn(json.getInt("expires_in"));
                response.setRefreshToken(json.getString("refresh_token"));
                response.setHttpStatusCode(httpResponse.getStatusLine().getStatusCode());
            }
            catch (Exception exc) {

            }
        }

        return response;
    }

}
