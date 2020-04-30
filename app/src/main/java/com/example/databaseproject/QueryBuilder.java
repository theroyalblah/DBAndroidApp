package com.example.databaseproject;

import android.net.SSLCertificateSocketFactory;
import android.os.StrictMode;
import android.util.Log;

import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
public class QueryBuilder {
    public static JSONArray performQuery(String query) {
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new
                        StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }

            URL url = new URL("https://192.168.1.17/phase3/queryAPI.php");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setSSLSocketFactory(SSLCertificateSocketFactory.getInsecure(0, null));
            connection.setHostnameVerifier(new AllowAllHostnameVerifier());
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("query", query);

            Log.i("JSON", jsonParam.toString());
            DataOutputStream os = new DataOutputStream(connection.getOutputStream());
            os.writeBytes(jsonParam.toString());

            os.flush();
            os.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line+"\n");
            }
            br.close();
            String response = sb.toString();

            Log.d("STATUS", String.valueOf(connection.getResponseCode()));
            Log.d("MSG" , connection.getResponseMessage());
            Log.d("RESPONSE" , response);

            connection.disconnect();

            try {
                JSONArray jsonResult = new JSONArray(response);
                return jsonResult;
            }catch (JSONException err){
                Log.d("JSON Error!", err.toString());
            }
        }
        catch(Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static JSONObject getJSONObject(JSONArray arr, int index) {
        if (arr != null && arr.length() != 0) {
            try {
                JSONObject res = arr.getJSONObject(index);
                return res;

            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        else {
            return null;
        }
    }
}
