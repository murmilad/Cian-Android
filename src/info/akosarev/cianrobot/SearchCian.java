package info.akosarev.cianrobot;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.util.Log;

public class SearchCian extends Search {

	static String GIS_API_KEY = "rusazx2220";

	@Override
	public List<HashMap<String, Object>> lookForFlats(SharedPreferences settings, SharedPreferences.Editor editor, CheckHandler handler) {
		List<HashMap<String, Object>> objects = new LinkedList<HashMap<String, Object>>();

		String generatedUrl = "http://map.cian.ru/ajax/map/roundabout/?deal_type=2&flats=yes&minprice=1000000&maxprice=8000000&currency=2&room2=1&room3=1&minkarea=8&mintarea=48&minfloor=2&minfloorn=6&engine_version=2&in_polygon[0]=";

		Integer equalCount = 0;
	    Integer flatCount  = 0;
	    Integer cianCount  = 0;
	    Integer pointCount = 0;

	    for (String shape :shapes){
		    try {

		        String response = new SendRequestTask().doInBackground(false, generatedUrl+shape+"&_=1455551798781");
		    	
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
					    	        }
									Log.i("CianTask", "JSON: result  " + clossestStation );
					    		}

						    	flat.put("clossestStation", clossestStation);
					    		flat.put("clossestDestantion", clossestDestantion);

					    		handler.check(flat);
					    		objects.add(flat);
					    }

			    	}
			    	
			    }
		    } catch (JSONException e) {
				e.printStackTrace();
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

	private String getClossestStation(String position){
		DecimalFormat dff;
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
        otherSymbols.setDecimalSeparator('.');

		dff = new DecimalFormat("##.######", otherSymbols);
    	dff.setRoundingMode(RoundingMode.DOWN);

		String positionUri  = position.replace(" ", ",");

		Pattern downloadPattern = Pattern.compile("([\\d\\.]+) ([\\d\\.]+)");
        Matcher downloadMatcher = downloadPattern.matcher(position);

 
        if (downloadMatcher.find()){
        	String flatLat = downloadMatcher.group(1);
        	String flatLng = downloadMatcher.group(2);

        	Integer side = 1100;
        	Integer radius = 240;

        	Coordinate currentCoordinate = calcEndPoint(
        			new Coordinate((float) (Float.parseFloat(flatLng)), (float) (Float.parseFloat(flatLat))),
        			(int) Math.round(Math.hypot(side, side)),
        			(double) 45
        			);
        	
        	Integer step = (int) Math.sqrt(radius*radius/2);

		    JSONArray metroObjects = new JSONArray();

		    Log.i("CianTask", "JSON: search center  " + flatLng +","+ flatLat);

        	for (Integer distanceLng = 0; distanceLng < side * 2; distanceLng = distanceLng + step) {
        		if (distanceLng > 0) {
        			currentCoordinate = calcEndPoint(currentCoordinate, step, (double) 180);
        		}
            	for (Integer distanceLat = 0; distanceLat < side * 2; distanceLat = distanceLat + step) {
            		
            		Coordinate linkPoint = currentCoordinate;
            		if (distanceLat > 0) {
            			linkPoint = calcEndPoint(currentCoordinate, distanceLat, (double) 270);
            		}

            		if (distFrom(Float.parseFloat(flatLat), Float.parseFloat(flatLng), (float) linkPoint.getDoubleLat(),(float) linkPoint.getDoubleLon()) < side) {
	            		String response = new SendRequestTask().doInBackground(false, "http://catalog.api.2gis.ru/geo/search?q="+dff.format(linkPoint.getDoubleLon())+","+dff.format(linkPoint.getDoubleLat())+"&types=metro&format=short&limit=10&version=1.3&radius=250&key=" + GIS_API_KEY);
	        		    Log.i("CianTask", "JSON: search point " + dff.format(linkPoint.getDoubleLon())+","+dff.format(linkPoint.getDoubleLat()));
	//        		    Log.i("CianTask", "JSON: metro " + response);
	        			
	        		    try {
	        			    JSONObject metrosObject = new JSONObject(response);
	        			    String responseStatus = metrosObject.getString("response_code");
	
	        			    if (responseStatus.equals("200")) {
	        	
	        	
	//	        			    Log.i("CianTask", "JSON: metro status" + responseStatus);
		        	
		        			    
		        			    JSONArray metroObjectsSphere = metrosObject.getJSONArray("result");
		        			    
		        			    for (Integer i = 0; i < metroObjectsSphere.length(); i++) {
		        			    	metroObjects.put(metroObjectsSphere.get(i));
		        			    }
	        			    }
	
	        		    } catch (JSONException e) {
	        				e.printStackTrace();
	        			}
            		}

            	}
            	
        	}
	
		    float  findDestantion = 5000;
	        String findMetro      = "";

		    try {
			    for (Integer i = 0; i < metroObjects.length(); i++) {
	
			    	JSONObject metroObject = (JSONObject) metroObjects.get(i);
			    	
			    	String metroName = metroObject.getString("short_name");
	
			    	String metroPosition = metroObject.getString("centroid");
				    
			         downloadMatcher = downloadPattern.matcher(metroPosition);

			        if (downloadMatcher.find()){
			        	String metroLat = downloadMatcher.group(2);
			        	String metroLng = downloadMatcher.group(1);
	                
	                	
		            	Log.i("CianTask", "JSON: flat lat " + flatLat + " flat lng " + flatLng);
		            	
		            	float destantion = distFrom(Float.parseFloat(flatLat), Float.parseFloat(flatLng), Float.parseFloat(metroLat), Float.parseFloat(metroLng));
		            	if (destantion < findDestantion) {
		            		findDestantion = destantion;
		            		findMetro      = metroName;
		            	}
		
		            	Log.i("CianTask", "JSON: destantion " + destantion);
		
			        }
				    
	
		    	}
			    
			    
			} catch (JSONException e) {
				e.printStackTrace();
			}
	
	    	DecimalFormat df = new DecimalFormat("##");
	    	df.setRoundingMode(RoundingMode.DOWN);

	    	Log.i("CianTask", "JSON: metro  " + findMetro + " (" + df.format(findDestantion) + "m)");

	    	return findMetro + " (" + df.format(findDestantion) + "m)";
        }
	    
	    return null;
	}
	
	 public static float distFrom(float lat1, float lng1, float lat2, float lng2) {
		    double earthRadius = 6371000; //meters
		    double dLat = Math.toRadians(lat2-lat1);
		    double dLng = Math.toRadians(lng2-lng1);
		    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
		               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
		               Math.sin(dLng/2) * Math.sin(dLng/2);
		    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		    float dist = (float) (earthRadius * c);

		    return dist;
	}

}

//https://static-maps.yandex.ru/1.x/?ll=37.6111,55.8911&z=17&l=map&size=200,200

//02-17 18:14:24.737: I/CianTask(7766):  requestURL http://catalog.api.2gis.ru/geo/search?q=37.602815,55.882818&types=metro&format=short&limit=10&version=1.3&radius=250&key=rusazx2220
//02-17 18:14:24.901: I/CianTask(7766): JSON: metro {"api_version":"1.3","response_code":"200","total":"1","result":[{"id":"4504385606388529","project_id":32,"type":"metro","name":"пїЅпїЅпїЅпїЅпїЅпїЅ, пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ","short_name":"пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ","centroid":"POINT(37.602909 55.884036)","attributes":{"rank":1,"type":"land"},"dist":131}]}
//02-17 18:14:24.902: I/CianTask(7766): JSON: metro status200
//02-17 18:14:30.173: I/CianTask(7766): JSON: flat lat 55.8911 flat lng 37.6111
//02-17 18:14:30.174: I/CianTask(7766): JSON: destantion 937.005
//02-17 18:14:30.175: I/CianTask(7766): JSON: metro  пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ (937m)
