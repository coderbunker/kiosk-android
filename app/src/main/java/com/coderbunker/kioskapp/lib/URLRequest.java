package com.coderbunker.kioskapp.lib;

import android.os.StrictMode;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public class URLRequest {
    public static String requestURL(URL url, String method, String body) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String result = "";
        HttpURLConnection httpCon = null;
        try {
            try {
                httpCon = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert httpCon != null;
            httpCon.setDoOutput(true);
            try {
                httpCon.setRequestMethod(method);
            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            OutputStreamWriter out = null;
            try {
                out = new OutputStreamWriter(httpCon.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                assert out != null;
                out.write(body);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                httpCon.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            InputStream is = null;
            result = "";
            try {
                is = new BufferedInputStream(httpCon.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String inputLine = "";
            try {
                while ((inputLine = br.readLine()) != null) {
                    result += inputLine;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        } catch (NullPointerException e) {
            return "";
        }
    }
}
