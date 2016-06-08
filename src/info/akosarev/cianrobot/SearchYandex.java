package info.akosarev.cianrobot;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.util.Log;

public class SearchYandex extends Search {

	static String GIS_API_KEY = "rusazx2220";
	private SendRequestTask sender;
	public SearchYandex() throws IOException {
		sender = new SendRequestTask();
	}
	@Override
	public Set<String> lookForFlats(SharedPreferences settings, SharedPreferences.Editor editor, CheckHandler handler) throws IOException {
		Set<String> objects = new HashSet<String>();

		String generatedUrl = "http://map.cian.ru/ajax/map/roundabout/?deal_type=2&flats=yes&minprice=1000000&maxprice=8000000&currency=2&room2=1&room3=1&minkarea=8&mintarea=50&minfloor=2&engine_version=2&in_polygon[0]=";

		Integer equalCount = 0;
	    Integer flatCount  = 0;
	    Integer cianCount  = 0;
	    Integer pointCount = 0;
	    
	    
	    for (Shape shape :shapes){
		    try {

		        String response = sender.performGetCall(generatedUrl+shape+"&_=1455551798781");
		    	
			    Log.i("CianTask", "JSON: "+shape+" response " + response);


			    JSONObject flatsCurrentObject = new JSONObject(response);

			    Log.i("CianTask", "JSON: offers count " + flatsCurrentObject.getJSONObject("data").getInt("offers_count"));
			    
			    if (flatsCurrentObject.getJSONObject("data").getInt("offers_count") > 300) {
			    	Log.i("CianTask", "JSON: offers "+shape+" count > 300!");
			    	cianCount += 300;
			    } else {
			    	cianCount += flatsCurrentObject.getJSONObject("data").getInt("offers_count");
			    }

			    String responseStatus = flatsCurrentObject.getString("status");
	
			    Log.i("CianTask", "JSON: status" + responseStatus);

			    if (!"ok".equals(responseStatus) && !"toomuch".equals(responseStatus)) {
			    	throw new IOException("response status: " + responseStatus);
			    }
	
			    for (Iterator<String> pointIterator = flatsCurrentObject.getJSONObject("data").getJSONObject("points").keys(); pointIterator.hasNext();) {
			    	String pointPosition = pointIterator.next();
			    	
			    	JSONObject pointObject = flatsCurrentObject.getJSONObject("data").getJSONObject("points").getJSONObject(pointPosition);
			    	flatCount++;

//				    try {
//
//				    	if (flatsObject.getJSONObject("data").getJSONObject("points").getJSONObject(pointPosition)!= null){
//				    		equalCount++;
//				    	}
//				    } catch (JSONException e) {
//						e.printStackTrace();
//					}

			    	JSONArray flatObjects = pointObject.getJSONArray("offers");
			    	String flatAddress = pointObject.getJSONObject("content").getString("text");
			    	String clossestStation = null;

			    	if (flatObjects.length() < 50) { //Исключаем новостройки
					    for (Integer i = 0; i < flatObjects.length(); i++) {
			
						    	JSONObject flatObject = (JSONObject) flatObjects.get(i);

						    	HashMap<String, Object> flat = new HashMap<String, Object>();

						    	JSONArray flatData = flatObject.getJSONArray("link_text");

						    	flat.put("flatAddress", flatAddress);
						    	flat.put("flatId", "cian_" + flatObject.getString("id"));
						    	flat.put("flatType", flatData.getString(0));
						    	flat.put("flatArea", flatData.getString(1));
						    	flat.put("flatPrice", Long.parseLong(flatData.getString(2).replaceAll("р.", "").replaceAll(" ", "")));
						    	flat.put("flatFlat", flatData.getString(3));
						    	flat.put("flatUrl", "http://www.cian.ru/sale/flat/" + flatObject.getString("id") + "/");
						    	flat.put("pointPosition", pointPosition);

						    	pointCount++;

						    	clossestStation = settings.getString("clossestStation" + pointPosition, "");
						    	Long clossestDestantion = settings.getLong("clossestDestantion" + pointPosition, new Long(0));

					    		if ("".equals(clossestStation)) {
									Log.i("CianTask", "JSON: look formetro  " + flatAddress + " " + pointPosition );
						    									    			
					    			clossestStation = getClossestStation(pointPosition);
					    	        Pattern destantionPattern = Pattern.compile("(\\d+)m");
					    	        Matcher destantionMatcher = destantionPattern.matcher(clossestStation);

					    	        if (destantionMatcher.find()){
					    	        	clossestDestantion = Long.parseLong(destantionMatcher.group(1));
							    		editor.putString("clossestStation" + pointPosition, clossestStation);
										editor.putLong("clossestDestantion" + pointPosition, clossestDestantion);
					    	        } else {
					    	        	clossestDestantion = (long) 5000;
					    	        }
									Log.i("CianTask", "JSON: result  " + clossestStation );
					    		}

						    	flat.put("clossestStation", clossestStation);
					    		flat.put("clossestDestantion", clossestDestantion);

					    		handler.check(flat);
					    		objects.add("cian_" + flatObject.getString("id"));
					    }

			    	}
			    	
			    }
		    } catch (JSONException e) {
				throw new IOException("Cant parse result " + e.toString()); 
			}
		    try {
        	    Thread.sleep(1000);
        	} catch(InterruptedException ex) {
        		ex.printStackTrace();
        	}
	    }

	    Log.i("CianTask", "JSON: equal count " + equalCount);
	    Log.i("CianTask", "JSON: flat count " + flatCount);
	    Log.i("CianTask", "JSON: cian count " + cianCount);
	    Log.i("CianTask", "operate count " + pointCount);

		return objects;
	}

	
//	Request URL:https://realty.yandex.ru/gate/page/loadBlocks
//		Request Method:POST
//		Status Code:200 OK
//		Remote Address:213.180.204.41:443
//		Response Headers
//		view source
//		Connection:keep-alive
//		Content-Encoding:gzip
//		Content-Type:application/json; charset=utf-8
//		Date:Wed, 08 Jun 2016 21:42:11 GMT
//		Server:nginx
//		Set-Cookie:from=direct;path=/
//		Strict-Transport-Security:max-age=31536000
//		Transfer-Encoding:chunked
//		X-Content-Type-Options:nosniff
//		X-Frame-Options:SAMEORIGIN
//		Request Headers
//		view source
//		Accept:application/json, text/javascript, */*; q=0.01
//		Accept-Encoding:gzip, deflate
//		Accept-Language:ru-RU,ru;q=0.8,en-US;q=0.6,en;q=0.4
//		Connection:keep-alive
//		Content-Length:942
//		Content-Type:application/x-www-form-urlencoded; charset=UTF-8
//		Cookie:yandexuid=174259741462382619; fuid01=572b11da7c071cda.hQ953A4XBeYRZ8KDeRwRMB9TYbMBbG0eG6tw-PCQqvM0Q5p46pyqli7P-Zw0ZVsaapjylvSGaYTJ4WrSaaXy1mTf3xcSnYIeaDUyaNaFu8-NjSsErMxWNoQkDH9ZAYAf; L=SQ15YnJBU3lRYFBGDEZEBHoGQn1+cwVkLVsAAl8gKh4sCwYZAlsEeQ==.1462440428.12387.378585.253bd79755c001e22f130ea4017dc40a; yandex_login=boxofpandora2008; zm=m-white_bender.flex.webp.css-https%3Awww_zNa-f_5uJcPTYK6A_vgZjL6hOW8%3Al; yandex_gid=213; yabs-frequency=/4/0000000000000000/paMmSAGaPm00/; Session_id=3:1465295701.5.0.1462440428000:KvtrTg:8.0|37638243.0.2|146809.331269.UY8eLXeJAh_s6DRCwsME2FD1Tw0; sessionid2=3:1465295701.5.0.1462440428000:KvtrTg:8.1|37638243.0.2|146809.19187.aokl-30kRd01_VQpMIVWmwPY4Cw; _ym_uid=1465308024460072192; yp=1470216409.ww.1#1481076025.szm.1_00:1920x1200:1855x1091#1777800428.udn.cDpib3hvZnBhbmRvcmEyMDA4#1467803787.ygu.1; _ym_isad=2; uid=1bTMKVdYjg9Hja6+BI6/Ag==; _ym_visorc_2119876=b; subscription_popup_count=1; subscription_popup_shown=1; rgid=587795; offer_map_zoom=14; from=direct
//		Host:realty.yandex.ru
//		Origin:https://realty.yandex.ru
//		Referer:https://realty.yandex.ru/search?type=SELL&category=APARTMENT&rgid=587795&mapPolygon=55.840446%2C37.547443%3B55.842377%2C37.605293%3B55.81262%2C37.621944%3B55.79821%2C37.577312%3B55.81223%2C37.540234%3B55.827885%2C37.536972%3B55.837643%2C37.542294&roomsTotal=1&roomsTotal=2&priceMin=1000000&priceMax=7600000&kitchenSpaceMin=7&floorMin=2&minFloors=2&bathroomUnit=SEPARATED&balcony=BALCONY&areaMin=50
//		User-Agent:Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.82 Safari/537.36
//		X-Requested-With:XMLHttpRequest
//		X-Retpath-Y:https://realty.yandex.ru/search?type=SELL&category=APARTMENT&rgid=587795&mapPolygon=55.840446%2C37.547443%3B55.842377%2C37.605293%3B55.81262%2C37.621944%3B55.79821%2C37.577312%3B55.81223%2C37.540234%3B55.827885%2C37.536972%3B55.837643%2C37.542294&roomsTotal=1&roomsTotal=2&priceMin=1000000&priceMax=7600000&kitchenSpaceMin=7&floorMin=2&minFloors=2&bathroomUnit=SEPARATED&balcony=BALCONY&areaMin=50
//		Form Data
//		view source
//		view URL encoded
//		params[type]:search
//		params[params][type]:SELL
//		params[params][category]:APARTMENT
//		params[params][rgid]:587795
//		params[params][mapPolygon]:55.840446,37.547443;55.842377,37.605293;55.81262,37.621944;55.79821,37.577312;55.81223,37.540234;55.827885,37.536972;55.837643,37.542294
//		params[params][roomsTotal][]:1
//		params[params][roomsTotal][]:2
//		params[params][priceMin]:1000000
//		params[params][priceMax]:7600000
//		params[params][kitchenSpaceMin]:7
//		params[params][floorMin]:2
//		params[params][minFloors]:2
//		params[params][bathroomUnit]:SEPARATED
//		params[params][balcony]:BALCONY
//		params[params][areaMin]:50
//		params[blocks][]:content-bottom
//		params[blocks][]:b-page__title
//		params[blocks][]:type-switcher
//		params[blocks][]:footer
//		version:2.0-1050
//		crc:u3bfccb2f02975f33526ee1c31e39885c
		

}

