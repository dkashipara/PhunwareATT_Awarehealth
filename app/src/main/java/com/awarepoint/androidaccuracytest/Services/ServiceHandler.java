package com.awarepoint.androidaccuracytest.Services;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import static com.awarepoint.androidaccuracytest.HttpService.HttpsServiceHandler.getHttpsClient;

/*
 * Copyright Awarepoint 2015. All rights reserved.
 */

public class ServiceHandler {

    static String response = null;
    static String bearerToken = "Bearer ";
    static String httpAccept = "application/hal+json";
    public final static int GET = 1;
    public final static int POST = 2;

    public ServiceHandler(String oauthToken) {
        bearerToken = "Bearer " + oauthToken;
    }

    /**
     * Making service call
     * @url - url to make request
     * @method - http request method
     * */
    public String makeServiceCall(String url, int method) {
        return this.makeServiceCall(url, method, null);
    }

    /**
     * Making service call
     * @url - url to make request
     * @method - http request method
     * @params - http request params
     * */
    public String makeServiceCall(String url, int method,
                                  List<NameValuePair> params) {
        try {
            // http client
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;

            // Checking http request method type
            if (method == POST) {
                HttpPost httpPost = new HttpPost(url);
                // adding post params
                if (params != null) {
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                }

                httpResponse = httpClient.execute(httpPost);

            } else if (method == GET) {
                // appending params to url
                if (params != null) {
                    String paramString = URLEncodedUtils
                            .format(params, "utf-8");
                    url += "?" + paramString;
                }
                HttpGet httpGet = new HttpGet(url);
                httpGet.addHeader("Authorization", bearerToken);
                httpGet.addHeader("Accept", httpAccept);
                httpResponse = httpClient.execute(httpGet);

            }
            httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;

    }


    public Bitmap makeFileServiceCall(String url, int method, List<NameValuePair> params) {
        Bitmap map = null;
        try {

            // http client
            HttpClient httpClient = getHttpsClient(new DefaultHttpClient());
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;

            // Checking http request method type
            if (method == POST) {
                HttpPost httpPost = new HttpPost(url);
                // adding post params
                if (params != null) {
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                }

                httpResponse = httpClient.execute(httpPost);

            } else if (method == GET) {
                // appending params to url
                if (params != null) {
                    String paramString = URLEncodedUtils
                            .format(params, "utf-8");
                    url += "?" + paramString;
                }
                HttpGet httpGet = new HttpGet(url);
                httpGet.addHeader("Authorization", bearerToken);
                httpGet.addHeader("Accept", httpAccept);
              //  httpResponse = httpClient.execute(httpGet);
                httpResponse = httpClient.execute(httpGet);

                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    HttpEntity entity = httpResponse.getEntity();
                    byte[] bytes = EntityUtils.toByteArray(entity);

                    map = BitmapFactory.decodeByteArray(bytes, 0,
                            bytes.length);



                    return map;
                } else {
                    return null;
                }
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;

    }


    public String GetHttpString(String url)
    {
        String result = "";
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet request = new  HttpGet(url);
     //   request.addHeader("Authorization" ,  TokenHelper.Token_Type + " " +TokenHelper.Access_Token);
        request.addHeader("Authorization", bearerToken);
        request.addHeader("Content-Type","application/hal+json" );
        request.addHeader("Accept","application/hal+json" );
        HttpResponse response;

        try
        {
            response = httpclient.execute(request);
            result = GetHttpEntityAsString(response.getEntity().getContent());
        }
        catch (ClientProtocolException e)
        {
            e.printStackTrace();
        }
        catch (IOException e) {

            e.printStackTrace();
        }
        catch (Exception e) {

            e.printStackTrace();
        }

        return result;
    }

    private String GetHttpEntityAsString (InputStream input)
    {
        StringBuilder sb=new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        String read;

        try
        {
            while ((read = br.readLine()) != null)
            {
                //System.out.println(read);
                sb.append(read);
            }

        }
        catch (Exception e)
        {

            e.printStackTrace();
        }

        return sb.toString();
    }

}
