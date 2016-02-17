package info.akosarev.cianrobot;

import info.akosarev.cianrobot.R;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Notification.Builder;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;

//center 55.758563, 37.656984
//
//55.762426,37.650032
//
//55.754554,37.672005


//http://catalog.api.2gis.ru/geo/search?q=37.656984,55.758563&types=metro&format=short&limit=10&version=1.3&radius=250&key=rusazx2220

public class LookCianTask implements Runnable {

	private Builder notificationProgressBuilder;
	private NotificationManager notificationManager;
	private Boolean done;

    private Service downloaderService;
    private String url;
    private Calendar c;

    static SharedPreferences settings;
	static SharedPreferences.Editor editor;

	static String GIS_API_KEY = "rusazx2220";

    private Set<String> taskIdSet = new HashSet<String>();

	LookCianTask(Service downloaderService, String url) {
		
		done = false;

		this.downloaderService = downloaderService;
		this.url               = url;

		c = Calendar.getInstance(); 

		notificationProgressBuilder  = new Notification.Builder(downloaderService)
		.setCategory(Notification.CATEGORY_MESSAGE)
		.setContentTitle("Cian service")
		.setContentText("Started")
		.setSmallIcon(R.drawable.ic_notification)
		.setPriority(Notification.PRIORITY_MIN)
		;
		

		settings = PreferenceManager.getDefaultSharedPreferences(downloaderService);
        editor   = settings.edit();

        taskIdSet = settings.getStringSet("taskId", taskIdSet);

	}

	public void run() {
		Integer seconds = 0;
		notificationManager =
	          	  (NotificationManager) downloaderService.getSystemService(downloaderService.NOTIFICATION_SERVICE);

    	while (true) {
    		try {
	    		c = Calendar.getInstance(); 
	
	    		notificationProgressBuilder.setContentTitle("Cian service " +  c.getTime());
	
	    		Notification notification = notificationProgressBuilder.build();
	    		notification.flags |= Notification.FLAG_NO_CLEAR;
	    		notificationManager.notify(1, notification);
	
	    		if (((Integer)0).equals(((int)seconds % 60))) {
				    Log.i("CianTask", "JSON: uri " + url);
			
			        String response = new SendRequestTask().doInBackground(false, url);
			    	
				    Log.i("CianTask", "JSON: response " + response);
			
				    try {
					    JSONObject flatsObject = new JSONObject(response);
			
					    String responseStatus = flatsObject.getString("status");
			
					    Log.i("CianTask", "JSON: status" + responseStatus);
			
					    flatsObject.getJSONObject("data").getJSONObject("points").keys();
					    for (Iterator<String> pointIterator = flatsObject.getJSONObject("data").getJSONObject("points").keys(); pointIterator.hasNext();) {
					    	String pointPosition = pointIterator.next();
					    	
					    	JSONObject pointObject = flatsObject.getJSONObject("data").getJSONObject("points").getJSONObject(pointPosition);
				
					    	
					    	

					    	JSONArray flatObjects = pointObject.getJSONArray("offers");
					    	String flatAddress = pointObject.getJSONObject("content").getString("text");
					    	String clossestStation = null;

					    	for (Integer i = 0; i < flatObjects.length(); i++) {
			
						    	JSONObject flatObject = (JSONObject) flatObjects.get(i);
						    			
						    	String flatId = flatObject.getString("id");
			
						    	

						    	if (!taskIdSet.contains(flatId)){
						    		if (clossestStation == null) {
						    			clossestStation = getClossestStation(pointPosition, 1000);
						    		}

						    		if (clossestStation != null) {
							    		JSONArray flatData = flatObject.getJSONArray("link_text");
							    		String flatType = flatData.getString(0);
							    		String flatArea = flatData.getString(1);
							    		String flatPrice = flatData.getString(2);
							    		String flatFlat = flatData.getString(3);
							    		String flatUrl  =  "http://www.cian.ru/sale/flat/" + flatId + "/";
					
				
					                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
					                	shareIntent.putExtra(Intent.EXTRA_TEXT , "<a href=\"" + flatUrl + "\">" + flatAddress + "</a>");
					                	shareIntent.setType("text/html");
					                	PendingIntent sharePendingIntent = PendingIntent.getActivity(downloaderService.getApplicationContext(),
					                			Integer.parseInt(flatId), Intent.createChooser(shareIntent, downloaderService.getString(R.string.share)), PendingIntent.FLAG_CANCEL_CURRENT);
					                	
					                	Intent uriIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(flatUrl));
					           	    	PendingIntent uriPendingIntent =
					           	    	        PendingIntent.getActivity(downloaderService, Integer.parseInt(flatId), uriIntent, 0);
					
					           			notification  = new Notification.Builder(downloaderService)
					                  	  .setCategory(Notification.CATEGORY_MESSAGE)
					                  	  .setContentTitle(Html.fromHtml(flatAddress + " " + clossestStation  + " " + flatType + " (" + flatArea + " | " + flatFlat +") " + flatPrice))
					                  	  .setStyle(new Notification.BigTextStyle().bigText(Html.fromHtml(flatAddress + " " + clossestStation  + " " + flatType + " (" + flatArea + " | " + flatFlat +") <b>" + flatPrice + "</b>")))
					                  	  .setSmallIcon(R.drawable.ic_notification)
					                  	  .addAction(R.drawable.ic_download, downloaderService.getString(R.string.open), uriPendingIntent)
					                  	  .addAction(R.drawable.ic_share, downloaderService.getString(R.string.share), sharePendingIntent)
					                  	  .build();
					
					                  	notificationManager.notify("done", Integer.parseInt(flatId), notification);
				
										Log.i("CianTask", "JSON: address " + flatAddress +  " flat " + flatData.getString(0));
				
										editor.clear();
										editor.commit();
							    		taskIdSet.add(flatId);
										editor.putStringSet("taskId", taskIdSet);
										
										
										Log.i("CianTask", "commit " + flatId +  " apply " + editor.commit());
									
							    	} else {
										editor.clear();
										editor.commit();
							    		taskIdSet.add(flatId);
										editor.putStringSet("taskId", taskIdSet);

										Log.i("CianTask", "commit incapatible" + flatId +  " apply " + editor.commit());
							    	}
				
						    	}
						    }
					    }
					} catch (JSONException e) {
						e.printStackTrace();
					}
	    		}
	    		seconds++;
			    try {
	        	    Thread.sleep(1000);
	        	} catch(InterruptedException ex) {
	        		ex.printStackTrace();
	        	}
    		} catch(Exception ex) {
        		ex.printStackTrace();
        	}
    	}
		
	}
	
//	55.898307, 37.587010
//	55.898356, 37.582869
//	
//	0,004141 = 250
//	0,000016564 = 1м
	
//	Нижний левый
	private String getClossestStation(String position, float maxDestantion){
		String positionUri  = position.replace(" ", ",");

		Pattern downloadPattern = Pattern.compile("([\\d\\.]+) ([\\d\\.]+)");
        Matcher downloadMatcher = downloadPattern.matcher(position);

        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
        otherSymbols.setDecimalSeparator('.');
        DecimalFormat dff = new DecimalFormat("##.######", otherSymbols);
    	dff.setRoundingMode(RoundingMode.DOWN);

        if (downloadMatcher.find()){
        	String flatLat = downloadMatcher.group(1);
        	String flatLng = downloadMatcher.group(2);

        	Integer side = 1500;

        	float currentLat = (float) (Float.parseFloat(flatLat) - 1500/2 * 0.000016564);
        	float currentLng = (float) (Float.parseFloat(flatLng) - 1500/2 * 0.000016564);

		    JSONArray metroObjects = new JSONArray();

        	for (Integer distanceLng = 0; distanceLng < side; distanceLng = distanceLng + 250) {
            	for (Integer distanceLat = 0; distanceLat < side; distanceLat = distanceLat + 250) {
            		
                	String response = new SendRequestTask().doInBackground(false, "http://catalog.api.2gis.ru/geo/search?q="+dff.format(currentLng+distanceLng*0.000016564)+","+dff.format(currentLat + distanceLat*0.000016564)+"&types=metro&format=short&limit=10&version=1.3&radius=250&key=" + GIS_API_KEY);
        		    Log.i("CianTask", "JSON: metro " + response);
        			
        		    try {
        			    JSONObject metrosObject = new JSONObject(response);
        			    String responseStatus = metrosObject.getString("response_code");

        			    if (responseStatus.equals("200")) {
        	
        	
	        			    Log.i("CianTask", "JSON: metro status" + responseStatus);
	        	
	        			    
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
	
		    if (findDestantion <= maxDestantion) {
		    	DecimalFormat df = new DecimalFormat("##");
		    	df.setRoundingMode(RoundingMode.DOWN);
	
		    	Log.i("CianTask", "JSON: metro  " + findMetro + " (" + df.format(findDestantion) + "m)");
	
		    	return findMetro + " (" + df.format(findDestantion) + "m)";
		    }
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
//02-17 18:14:24.901: I/CianTask(7766): JSON: metro {"api_version":"1.3","response_code":"200","total":"1","result":[{"id":"4504385606388529","project_id":32,"type":"metro","name":"Москва, Бибирево","short_name":"Бибирево","centroid":"POINT(37.602909 55.884036)","attributes":{"rank":1,"type":"land"},"dist":131}]}
//02-17 18:14:24.902: I/CianTask(7766): JSON: metro status200
//02-17 18:14:30.173: I/CianTask(7766): JSON: flat lat 55.8911 flat lng 37.6111
//02-17 18:14:30.174: I/CianTask(7766): JSON: destantion 937.005
//02-17 18:14:30.175: I/CianTask(7766): JSON: metro  Бибирево (937m)
