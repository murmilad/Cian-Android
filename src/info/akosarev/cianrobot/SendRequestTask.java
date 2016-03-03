package info.akosarev.cianrobot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;





import android.os.AsyncTask;
import android.util.Log;

public class SendRequestTask extends AsyncTask<Object, Integer, String> {

	@Override
	protected String doInBackground(Object...  objects) {
		String response = "";
		try {
			if ((Boolean) objects[0]) {
					response = performPostCall((String)objects[1], (HashMap<String, String>) objects[2]);
			} else {
				response = performGetCall((String)objects[1]);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return response;
	}

	public String  performPostCall(String requestURL,
            HashMap<String, String> postDataParams) throws IOException {

        URL url;
        String response = "";

        url = new URL(requestURL);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(60000);
        conn.setConnectTimeout(60000);
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);


        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(os, "UTF-8"));
        writer.write(getPostDataString(postDataParams));

        writer.flush();
        writer.close();
        os.close();
        int responseCode=conn.getResponseCode();

        if (responseCode == HttpsURLConnection.HTTP_OK) {
            String line;
            BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line=br.readLine()) != null) {
                response+=line;
            }
        } else {
        	Log.e("Downloader", "HTTP NOK: " + responseCode);    
        	throw new IOException("HTTP NOK: " + responseCode);
        }

        return response;
    }

	public String  performJSONCall(String requestURL, String json) throws IOException {

        URL url;
        String response = "";

            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(60000);
            conn.setConnectTimeout(60000);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(json);

            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            } else {
            	Log.e("Downloader", "HTTP NOK: " + responseCode);    
            	throw new IOException("HTTP NOK: " + responseCode);
            }

        return response;
    }
	public String  performGetCall(String requestURL) throws IOException {

        System.setProperty("http.agent", "");

        URL url;
        String response = "";
        url = new URL(requestURL);
        
//            Log.i("CianTask", " requestURL " + url.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        
        conn.setReadTimeout(15*1000);
        conn.connect();

        int responseCode=conn.getResponseCode();

        if (responseCode == HttpsURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null) {
            	stringBuilder.append(line + "\n");
            }
            response = stringBuilder.toString();
        } else {
        	Log.e("Downloader", "HTTP NOK: " + responseCode);    
        	throw new IOException("HTTP NOK: " + responseCode);

        }

        return response;
    }

	private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

}
