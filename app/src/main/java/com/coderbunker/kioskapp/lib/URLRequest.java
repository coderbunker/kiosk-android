package com.coderbunker.kioskapp.lib;

import android.os.AsyncTask;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
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
            e.printStackTrace();
            return "";
        }
    }

    private static class Download extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... arg0) {
            downloadFile(arg0[0], arg0[1]);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            System.out.println("Downloaded");
        }
    }

    public static void startDownload(String fileURL, String fileName) {
        (new Download()).execute(fileURL, fileName);

    }

    private static void downloadFile(String fileURL, String fileName) {
        try {
            String rootDir = Environment.getExternalStorageDirectory()
                    + File.separator + "Video";
            File rootFile = new File(rootDir);
            rootFile.mkdir();
            URL url = new URL(fileURL);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            c.connect();
            FileOutputStream f = new FileOutputStream(new File(rootFile,
                    fileName));
            InputStream in = c.getInputStream();
            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = in.read(buffer)) > 0) {
                f.write(buffer, 0, len1);
            }
            f.close();
        } catch (IOException e) {
            Log.d("Error....", e.toString());
        }
    }
}