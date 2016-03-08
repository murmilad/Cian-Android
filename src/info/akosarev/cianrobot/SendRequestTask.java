package info.akosarev.cianrobot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;





import android.os.AsyncTask;
import android.util.Log;

public class SendRequestTask extends AsyncTask<Object, Integer, String> {

	private List<ProxyIp> proxies = new LinkedList<ProxyIp>();
	private Integer firstCorrectProxy = 0;

	public SendRequestTask() throws IOException  {
//		proxies.add(new ProxyIp("189.219.90.139", 59216));
//		proxies.add(new ProxyIp("201.172.241.164", 14344));
//		proxies.add(new ProxyIp("187.185.4.128", 10000));
//		proxies.add(new ProxyIp("186.34.50.187", 10000));
//		proxies.add(new ProxyIp("187.160.181.76", 10000));
//		proxies.add(new ProxyIp("201.172.84.197", 40297));
//		proxies.add(new ProxyIp("200.225.1.230", 10000));
//		proxies.add(new ProxyIp("187.160.122.228", 49476));
//		proxies.add(new ProxyIp("187.161.227.241", 22608));
//		proxies.add(new ProxyIp("187.161.17.37", 10000));
//		proxies.add(new ProxyIp("187.161.3.63", 10000));
//		proxies.add(new ProxyIp("201.166.236.124", 10000));
//		proxies.add(new ProxyIp("201.172.78.213", 34681));
//		proxies.add(new ProxyIp("189.218.116.239", 51509));
//		proxies.add(new ProxyIp("189.218.164.9", 16611));
//		proxies.add(new ProxyIp("189.219.93.10", 57553));
//		proxies.add(new ProxyIp("189.218.221.210", 24584));
//		proxies.add(new ProxyIp("189.219.76.30", 61893));
//		proxies.add(new ProxyIp("189.219.98.34", 57337));
//		proxies.add(new ProxyIp("189.219.93.78", 57493));
//		proxies.add(new ProxyIp("189.219.92.145", 57674));
//		proxies.add(new ProxyIp("189.218.144.56", 10000));
//		proxies.add(new ProxyIp("117.135.251.132", 8080));
//		proxies.add(new ProxyIp("117.135.251.131", 8080));
//		proxies.add(new ProxyIp("189.219.84.153", 59714));
//		proxies.add(new ProxyIp("201.172.105.128", 41004));
//		proxies.add(new ProxyIp("201.172.170.144", 10000));
//		proxies.add(new ProxyIp("182.254.153.54", 8080));
//		proxies.add(new ProxyIp("189.219.205.144", 28747));
//		proxies.add(new ProxyIp("189.218.98.35", 57337));
//		proxies.add(new ProxyIp("189.218.62.99", 33721));
//		proxies.add(new ProxyIp("187.242.165.19", 17905));
//		proxies.add(new ProxyIp("189.219.84.175", 59764));
//		proxies.add(new ProxyIp("189.219.83.143", 61012));
//		proxies.add(new ProxyIp("189.219.112.206", 52501));
//		proxies.add(new ProxyIp("201.173.223.247", 15722));
//		proxies.add(new ProxyIp("189.219.179.103", 13772));
//		proxies.add(new ProxyIp("189.219.136.95", 13700));
//		proxies.add(new ProxyIp("189.218.62.189", 33639));
//		proxies.add(new ProxyIp("189.219.72.202", 62737));
//		proxies.add(new ProxyIp("189.219.255.118", 17069));
//		proxies.add(new ProxyIp("189.219.228.214", 22797));
//		proxies.add(new ProxyIp("187.161.19.128", 43041));
//		proxies.add(new ProxyIp("189.219.89.94", 58501));
//		proxies.add(new ProxyIp("200.239.11.95", 50096));
//		proxies.add(new ProxyIp("189.219.86.172", 60279));
//		proxies.add(new ProxyIp("201.173.185.141", 10000));
//		proxies.add(new ProxyIp("189.218.69.22", 10000));
//		proxies.add(new ProxyIp("189.219.108.21", 53710));

		URL url = new URL("https://free-proxy-list.net");
		
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        
        conn.setReadTimeout(2000);
        conn.connect();

        int responseCode=conn.getResponseCode();
        String response = "";
        
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

        Pattern proxyPattern = Pattern.compile("(\\d+\\.\\d+\\.\\d+\\.\\d+)</td><td>(\\d+)");
        Matcher proxyMatcher = proxyPattern.matcher(response);
        while (proxyMatcher.find()){
        	if (!"0.0.0.0".equals(proxyMatcher.group(1))) {
        		proxies.add(new ProxyIp(proxyMatcher.group(1), Integer.parseInt(proxyMatcher.group(2))));
        	}
	    }

// {"table":"<!-- used for AJAX call, gives only port table content --><tr class=\"\" rel=\"24629242\"> <td class=\"leftborder timestamp\" rel=\"1457460544\"> <span class=\"updatets \"> 20secs <\/span> <\/td> <td> <span> <style>.ikrX{display:none}\n.qZOV{display:inline}\n.akUz{display:none}\n.qB71{display:inline}\n.LSsb{display:none}\n.jsmp{display:inline}<\/style><span class=\"129\">107<\/span><span style=\"display:none\">171<\/span><span class=\"LSsb\">171<\/span><div style=\"display:none\">171<\/div>.<span style=\"display:none\">129<\/span><span class=\"LSsb\">129<\/span><div style=\"display:none\">129<\/div><span class=\"qB71\">151<\/span><span class=\"jsmp\">.<\/span><span style=\"display: inline\">152<\/span><span class=\"173\">.<\/span><span class=\"95\">210<\/span> <\/span> <\/td> <td> 80 <\/td> <td style=\"text-align:left\" class=\"country\" rel=\"us\"> <span style=\"white-space:nowrap;\"> <img src=\"\/images\/1x1.png\" style=\"width: 16px; height: 11px; margin-right: 5px;\" class=\"flags-us\" alt=\"flag \"\/> USA <\/span> <\/td> <td> <div class=\"progress-indicator response_time\" style=\"width: 114px\" value=\"533\" levels=\"speed\" rel=\"533\"> <div class=\"indicator\" style=\"width: 95%; background-color: rgb(0, 173, 173)\"><\/div> <\/div> <\/td> <td> <div class=\"progress-indicator connection_time\" style=\"width: 114px\" title=\"\" rel=\"161\" value=\"161\" levels=\"speed\"> <div class=\"indicator\" style=\"width: 100%; background-color: rgb(0, 173, 173)\"><\/div> <\/div> <\/td> <td> HTTP <\/td> <td nowrap> High +KA <\/td><\/tr><tr class=\"altshade\" rel=\"24629238\"> <td class=\"leftborder timestamp\" rel=\"1457460544\"> <span class=\"updatets \"> 20secs <\/span> <\/td> <td> <span> <style>.ieD-{display:none}\n.Z9KC{display:inline}<\/style><span style=\"display:none\">115<\/span><span style=\"display: inline\">117<\/span><span style=\"display:none\">201<\/span><span style=\"display:none\">205<\/span><span class=\"ieD-\">205<\/span><div style=\"display:none\">205<\/div><span class=\"3\">.<\/span><span style=\"display:none\">61<\/span><span class=\"ieD-\">61<\/span><div style=\"display:none\">61<\/div><span class=\"Z9KC\">135<\/span><span class=\"Z9KC\">.<\/span><span class=\"ieD-\">78<\/span><span><\/span><span style=\"display: inline\">250<\/span><span class=\"Z9KC\">.<\/span><span class=\"ieD-\">45<\/span><div style=\"display:none\">45<\/div><span style=\"display:none\">56<\/span><div style=\"display:none\">56<\/div><span style=\"display:none\">118<\/span><span class=\"ieD-\">118<\/span><div style=\"display:none\">118<\/div><span style=\"display:none\">126<\/span><div style=\"display:none\">126<\/div><span class=\"ieD-\">132<\/span><div style=\"display:none\">132<\/div><span style=\"display: inline\">134<\/span><span style=\"display:none\">201<\/span> <\/span> <\/td> <td> 80 <\/td> <td style=\"text-align:left\" class=\"country\" rel=\"cn\"> <span style=\"white-space:nowrap;\"> <img src=\"\/images\/1x1.png\" style=\"width: 16px; height: 11px; margin-right: 5px;\" class=\"flags-cn\" alt=\"flag \"\/> China <\/span> <\/td> <td> <div class=\"progress-indicator response_time\" style=\"width: 114px\" value=\"659\" levels=\"speed\" rel=\"659\"> <div class=\"indicator\" style=\"width: 93%; background-color: rgb(0, 173, 173)\"><\/div> <\/div> <\/td> <td> <div class=\"progress-indicator connection_time\" style=\"width: 114px\" title=\"\" rel=\"217\" value=\"217\" levels=\"speed\"> <div class=\"indicator\" style=\"width: 96%; background-color: rgb(0, 173, 173)\"><\/div> <\/div> <\/td> <td> HTTP <\/td> <td nowrap> High +KA <\/td><\/tr><tr class=\"\" rel=\"24629219\"> <td class=\"leftborder timestamp\" rel=\"1457460483\"> <span class=\"updatets \"> 1min <\/span> <\/td> <td> <span> <style>.GG8d{display:none}\n.IvNq{display:inline}<\/style><span class=\"51\">42<\/span><span style=\"display:none\">99<\/span><span><\/span><span class=\"223\">.<\/span><span style=\"display:none\">60<\/span><span class=\"GG8d\">60<\/span><div style=\"display:none\">67<\/div>118<span style=\"display:none\">127<\/span><span style=\"display:none\">182<\/span><span class=\"GG8d\">182<\/span><div style=\"display:none\">182<\/div><span style=\"display:none\">184<\/span><span><\/span><span style=\"display:none\">241<\/span><div style=\"display:none\">241<\/div>.<span style=\"display:none\">15<\/span><span class=\"GG8d\">15<\/span><span><\/span><div style=\"display:none\">26<\/div><span class=\"GG8d\">53<\/span><div style=\"display:none\">53<\/div><span style=\"display:none\">82<\/span><div style=\"display:none\">82<\/div><span class=\"GG8d\">138<\/span><span style=\"display:none\">143<\/span><span class=\"GG8d\">143<\/span><span><\/span><span style=\"display:none\">153<\/span><span class=\"GG8d\">153<\/span><div style=\"display:none\">153<\/div><span class=\"GG8d\">157<\/span><span style=\"display:none\">176<\/span><span style=\"display:none\">205<\/span><span><\/span><span class=\"GG8d\">210<\/span><span style=\"display: inline\">216<\/span><span class=\"GG8d\">237<\/span><div style=\"display:none\">237<\/div><span style=\"display: inline\">.<\/span><span class=\"GG8d\">125<\/span><div style=\"display:none\">125<\/div><span class=\"GG8d\">205<\/span><span><\/span>219 <\/span> <\/td> <td> 3128 <\/td> <td style=\"text-align:left\" class=\"country\" rel=\"vn\"> <span style=\"white-space:nowrap;\"> <img src=\"\/images\/1x1.png\" style=\"width: 16px; height: 11px; margin-right: 5px;\" class=\"flags-vn\" alt=\"flag \"\/> Viet Nam <\/span> <\/td> <td> <div class=\"progress-indicator response_time\" style=\"width: 114px\" value=\"1222\" levels=\"speed\" rel=\"1222\"> <div class=\"indicator\" style=\"width: 88%; background-color: rgb(0, 173, 173)\"><\/div> <\/div> <\/td> <td> <div class=\"progress-indicator connection_time\" style=\"width: 114px\" title=\"\" rel=\"3338\" value=\"3338\" levels=\"speed\"> <div class=\"indicator\" style=\"width: 33%; background-color: rgb(237, 28, 36)\"><\/div> <\/div> <\/td> <td> HTTP <\/td> <td nowrap> High +KA <\/td><\/tr><tr class=\"altshade\" rel=\"24629232\"> <td class=\"leftborder timestamp\" rel=\"1457460483\"> <span class=\"updatets \"> 1min <\/span> <\/td> <td> <span> <style>.Tcxi{display:none}\n.w-QA{display:inline}<\/style>125<span class=\"w-QA\">.<\/span><span style=\"display:none\">13<\/span><span class=\"Tcxi\">13<\/span><span><\/span><span style=\"display:none\">22<\/span><span class=\"Tcxi\">22<\/span><div style=\"display:none\">22<\/div>39<span style=\"display:none\">115<\/span><div style=\"display:none\">115<\/div><span style=\"display:none\">133<\/span><span class=\"Tcxi\">133<\/span><span><\/span><span style=\"display:none\">189<\/span><div style=\"display:none\">189<\/div><span class=\"Tcxi\">197<\/span>.<span style=\"display: inline\">67<\/span>.<span style=\"display: inline\">194<\/span> <\/span> <\/td> <td> 3128 <\/td> <td style=\"text-align:left\" class=\"country\" rel=\"cn\"> <span style=\"white-space:nowrap;\"> <img src=\"\/images\/1x1.png\" style=\"width: 16px; height: 11px; margin-right: 5px;\" class=\"flags-cn\" alt=\"flag \"\/> China <\/span> <\/td> <td> <div class=\"progress-indicator response_time\" style=\"width: 114px\" value=\"1058\" levels=\"speed\" rel=\"1058\"> <div class=\"indicator\" style=\"width: 89%; background-color: rgb(0, 173, 173)\"><\/div> <\/div> <\/td> <td> <div class=\"progress-indicator connection_time\" style=\"width: 114px\" title=\"\" rel=\"336\" value=\"336\" levels=\"speed\"> <div class=\"indicator\" style=\"width: 93%; background-color: rgb(0, 173, 173)\"><\/div> <\/div> <\/td> <td> HTTP <\/td> <td nowrap> High +KA <\/td><\/tr><tr class=\"\" rel=\"24629203\"> <td class=\"leftborder timestamp\" rel=\"1457460423\"> <span class=\"updatets \"> 2mins <\/span> <\/td> <td> <span> <style>.t1L5{display:none}\n.lR6t{display:inline}\n.GLRZ{display:none}\n.kCLA{display:inline}\n.AsRK{display:none}\n.B4R5{display:inline}\n.YQg4{display:none}\n.zUSL{display:inline}<\/style><span class=\"t1L5\">39<\/span><div style=\"display:none\">39<\/div><span style=\"display: inline\">107<\/span><span class=\"175\">.<\/span><span class=\"lR6t\">151<\/span>.<span class=\"kCLA\">136<\/span><span style=\"display: inline\">.<\/span><span style=\"display:none\">129<\/span><span class=\"AsRK\">129<\/span><div style=\"display:none\">129<\/div>194 <\/span> <\/td> <td> 80 <\/td> <td style=\"text-align:left\" class=\"country\" rel=\"us\"> <span style=\"white-space:nowrap;\"> <img src=\"\/images\/1x1.png\" style=\"width: 16px; height: 11px; margin-right: 5px;\" class=\"flags-us\" alt=\"flag \"\/> USA <\/span> <\/td> <td> <div class=\"progress-indicator response_time\" style=\"width: 114px\" value=\"551\" levels=\"speed\" rel=\"551\"> <div class=\"indicator\" style=\"width: 94%; background-color: rgb(0, 173, 173)\"><\/div> <\/div> <\/td> <td> <div class=\"progress-indicator connection_time\" style=\"width: 114px\" title=\"\" rel=\"166\" value=\"166\" levels=\"speed\"> <div class=\"indicator\" style=\"width: 100%; background-color: rgb(0, 173, 173)\"><\/div> <\/div> <\/td> <td> HTTP <\/td> <td nowrap> High +KA <\/td><\/tr><tr class=\"altshade\" rel=\"24629197\"> <td class=\"leftborder timestamp\" rel=\"1457460363\"> <span class=\"updatets \"> 3mins <\/span> <\/td> <td> <span> <style>.zdTu{display:none}\n.sAvP{display:inline}\n.shB4{display:none}\n.Nwgy{display:inline}\n.BEh2{display:none}\n.oDc3{display:inline}<\/style><span style=\"display:none\">17<\/span><div style=\"display:none\">17<\/div><span style=\"display:none\">64<\/span><span style=\"display: inline\">122<\/span><span><\/span><span class=\"BEh2\">201<\/span><div style=\"display:none\">201<\/div><span style=\"display:none\">212<\/span><div style=\"display:none\">212<\/div><span style=\"display:none\">215<\/span><span style=\"display:none\">231<\/span><span class=\"BEh2\">231<\/span><div style=\"display:none\">231<\/div><span class=\"oDc3\">.<\/span><span class=\"oDc3\">96<\/span><span style=\"display:none\">129<\/span><span class=\"shB4\">129<\/span><span class=\"BEh2\">157<\/span><span><\/span><div style=\"display:none\">202<\/div><span class=\"164\">.<\/span><span><\/span><span style=\"display: inline\">59<\/span><span class=\"zdTu\">109<\/span><div style=\"display:none\">109<\/div><span class=\"zdTu\">134<\/span><div style=\"display:none\">134<\/div><span class=\"sAvP\">.<\/span><span class=\"zdTu\">1<\/span><div style=\"display:none\">1<\/div><span style=\"display:none\">24<\/span><span><\/span><span style=\"display:none\">28<\/span><div style=\"display:none\">89<\/div><span style=\"display: inline\">104<\/span><span class=\"zdTu\">109<\/span><div style=\"display:none\">109<\/div><span style=\"display:none\">149<\/span><span><\/span><span style=\"display:none\">207<\/span><span><\/span><span style=\"display:none\">243<\/span><div style=\"display:none\">243<\/div><span style=\"display:none\">250<\/span><span><\/span> <\/span> <\/td> <td> 82 <\/td> <td style=\"text-align:left\" class=\"country\" rel=\"cn\"> <span style=\"white-space:nowrap;\"> <img src=\"\/images\/1x1.png\" style=\"width: 16px; height: 11px; margin-right: 5px;\" class=\"flags-cn\" alt=\"flag \"\/> China <\/span> <\/td> <td> <div class=\"progress-indicator response_time\" style=\"width: 114px\" value=\"1359\" levels=\"speed\" rel=\"1359\"> <div class=\"indicator\" style=\"width: 86%; background-color: rgb(0, 173, 173)\"><\/div> <\/div> <\/td> <td> <div class=\"progress-indicator connection_time\" style=\"width: 114px\" title=\"\" rel=\"345\" value=\"345\" levels=\"speed\"> <div class=\"indicator\" style=\"width: 93%; background-color: rgb(0, 173, 17	
	}
			
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

        String response = "";

        IOException ex = new IOException("Proxy not exist");
    	
        for (int i = firstCorrectProxy; i < proxies.size(); i++) {
        	try {
        		
        		ProxyIp proxyIp = proxies.get(i);
        		Proxy  proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyIp.getIp(), proxyIp.getPort()));
        		URL url = new URL(requestURL);
	
	            HttpURLConnection conn = (HttpURLConnection) url.openConnection(proxy);
	            conn.setReadTimeout(10000);
	            conn.setConnectTimeout(10000);
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

	            ex = null;
	            firstCorrectProxy = i;
		        Log.i("Downloader", "Proxy OK: " + i + " " );
	            break;
        	} catch (IOException e) {
        		Log.e("Downloader", "Proxy NOK: " + i + " " + e);
        		ex = e;
        	}
    	}

    	if (ex != null ) {
    		throw ex;
    	}

    	return response;
    }
	
	
	public String  performGetCall(String requestURL) throws IOException {

        System.setProperty("http.agent", "");

        String response = "";
        IOException ex = new IOException("Proxy not exist");
    	
        for (int i = firstCorrectProxy; i < proxies.size(); i++) {
        	try {
        		
        		ProxyIp proxyIp = proxies.get(i);
        		Proxy  proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyIp.getIp(), proxyIp.getPort()));
        		URL url = new URL(requestURL);

        
//            Log.i("CianTask", " requestURL " + url.toString());
		        HttpURLConnection conn = (HttpURLConnection) url.openConnection(proxy);
		        conn.setRequestMethod("GET");
		        
		        conn.setReadTimeout(10000);
		        conn.setConnectTimeout(10000);
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
		        ex = null;
		        firstCorrectProxy = i;
		        Log.i("Downloader", "Proxy OK: " + i + " " );
	            break;
        	} catch (IOException e) {
        		Log.e("Downloader", "Proxy NOK: " + i + " " + e);
        		ex = e;
        	}
    	}

    	if (ex != null ) {
    		throw ex;
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
