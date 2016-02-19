package info.akosarev.cianrobot;

import info.akosarev.cianrobot.R;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
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

    private LinkedList<String> shapes = new LinkedList<String>();

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

        shapes.add("55.846410_37.661133,55.867991_37.680016,55.889561_37.683449,55.895529_37.675209,55.897454_37.644310,55.893796_37.641563,55.882629_37.639503,55.861248_37.634010,55.852577_37.634354");
        shapes.add("55.852192_37.634354,55.845831_37.660789,55.794333_37.652893,55.790666_37.636070,55.791824_37.617188,55.822309_37.613068,55.849686_37.615471,55.851999_37.632637");
        shapes.add("55.903036_37.610321,55.885903_37.625427,55.867606_37.628860,55.853733_37.626114,55.849879_37.612381,55.851421_37.567062,55.908809_37.559853,55.910734_37.584572");
        shapes.add("55.851228_37.566719,55.816523_37.548180,55.792403_37.572899,55.790666_37.614098,55.795877_37.618217,55.851035_37.618561");
        shapes.add("55.873770_37.455826,55.882436_37.459259,55.886480_37.481232,55.861056_37.501144,55.836386_37.538910,55.823466_37.523460,55.815944_37.547150,55.791245_37.573586,55.779855_37.556763,55.798965_37.502518,55.827902_37.475739,55.841398_37.470245,55.871651_37.452393");
        shapes.add("55.778697_37.556763,55.767303_37.539597,55.769042_37.469215,55.792403_37.451706,55.809385_37.442093,55.812665_37.419777,55.824045_37.410507,55.856817_37.401581,55.871651_37.416344");
        shapes.add("55.871651_37.415314,55.778697_37.558479,55.877237_37.441063,55.867991_37.428703");
        shapes.add("55.828287_37.398148,55.809192_37.431107,55.798965_37.446213,55.779469_37.446899,55.779469_37.402267,55.794912_37.398148,55.799544_37.384071,55.828095_37.395401");
        shapes.add("55.759770_37.534447,55.745473_37.532043,55.738709_37.536163,55.728850_37.531700,55.725563_37.497025,55.711640_37.472992,55.716668_37.447929,55.716282_37.421150,55.727883_37.419090,55.736969_37.388191,55.754361_37.380638,55.763827_37.398491,55.768655_37.417717,55.758418_37.433167,55.740448_37.454796,55.759964_37.493591,55.760929_37.530327");
        shapes.add("55.726917_37.547493,55.712607_37.579079,55.705063_37.597275,55.693842_37.595558,55.652411_37.554703,55.643112_37.545776,55.678359_37.475739,55.696551_37.481575,55.726143_37.545776");
        shapes.add("55.682617_37.467499,55.640980_37.551956,55.600463_37.506294,55.665580_37.430763,55.682811_37.465782");
        shapes.add("55.638849_37.688942,55.628577_37.620277,55.647955_37.551613,55.599881_37.504578,55.576404_37.593498,55.575433_37.624741,55.608996_37.636757,55.612874_37.675209,55.637880_37.687569");
        shapes.add("55.611905_37.675896,55.592509_37.711601,55.605312_37.757607,55.620048_37.768250,55.629353_37.783699,55.646793_37.780266,55.639430_37.743530,55.640980_37.696152,55.638267_37.689972");
        shapes.add("55.662869_37.716064,55.642530_37.716064,55.641755_37.747650,55.648924_37.774773,55.670615_37.758980,55.663063_37.717438");
        shapes.add("55.759577_37.799149,55.739095_37.791595,55.739095_37.755547,55.748565_37.746277,55.741414_37.703705,55.756873_37.696495,55.770394_37.746964,55.772132_37.789536");
        shapes.add("55.766724_37.693748,55.772904_37.736664,55.778311_37.760353,55.780049_37.799492,55.793368_37.808075,55.801474_37.803955,55.801474_37.788506,55.794333_37.764130,55.795684_37.739754,55.786033_37.684135,55.779469_37.676582,55.767497_37.691345");
        shapes.add("55.826166_37.711945,55.815944_37.740440,55.807648_37.755203,55.803211_37.754860,55.794719_37.743187,55.785068_37.685852,55.779083_37.673836,55.786999_37.663193,55.797035_37.667656,55.807841_37.710915,55.810542_37.719498,55.818259_37.696152");
        shapes.add("55.771359_37.598991,55.773677_37.626457,55.770200_37.643967,55.759964_37.657356,55.752815_37.655983,55.753781_37.693748,55.779083_37.682419,55.793561_37.659760,55.796842_37.637787,55.796842_37.624054,55.797421_37.596931,55.795877_37.581139,55.773290_37.595558");
        shapes.add("55.796070_37.593155,55.770587_37.603111,55.760929_37.587662,55.748371_37.586288,55.741028_37.588005,55.736582_37.597275,55.717055_37.576332,55.738322_37.529297,55.758611_37.530670,55.770587_37.533417,55.791631_37.568779");
        shapes.add("55.732910_37.548523,55.728077_37.541656,55.712027_37.575302,55.703322_37.654266,55.724403_37.711601,55.737162_37.696838,55.747212_37.699242,55.754361_37.695808,55.753588_37.653236,55.740448_37.650490,55.732716_37.638130,55.731363_37.615471,55.737356_37.593155,55.721116_37.574615,55.731943_37.550926");
        shapes.add("55.740255_37.708855,55.738902_37.691345,55.759770_37.680359,55.763827_37.704735");

        shapes.add("55.704870_37.596245,55.701001_37.611351,55.705644_37.621651,55.705063_37.622681,55.673906_37.627144,55.663063_37.617188,55.665000_37.601051,55.675649_37.569122,55.704290_37.595901");
        shapes.add("55.662482_37.618904,55.632454_37.619934,55.630709_37.618904,55.636136_37.574959,55.642337_37.549210,55.646405_37.537537,55.675068_37.567406,55.676810_37.571526,55.665580_37.612038");
        shapes.add("55.683198_37.624054,55.687262_37.676926,55.689972_37.685509,55.675649_37.694778,55.666742_37.653923,55.663450_37.620964,55.674293_37.625084,55.682230_37.623711");
        shapes.add("55.663838_37.619247,55.667323_37.648773,55.668098_37.662506,55.654929_37.654953,55.652411_37.659416,55.643305_37.680359,55.641562_37.685509,55.633423_37.658386,55.631291_37.620964,55.633810_37.616844,55.661126_37.616844,");

	}

	
	public void run() {
		Integer seconds = 0;
		notificationManager =
	          	  (NotificationManager) downloaderService.getSystemService(downloaderService.NOTIFICATION_SERVICE);

		downloaderService.startForeground(2, notificationProgressBuilder.build());

		while (true) {
    		try {
	
	    		if (((Integer)0).equals(((int)seconds % 60*2))) {
				    Log.i("CianTask", "JSON: uri " + url);
			
			        String response = new SendRequestTask().doInBackground(false, url);
			    	
				    Log.i("CianTask", "JSON: response " + response);
			
				    JSONObject flatsObject = new JSONObject(response);
				    Log.i("CianTask", "JSON: offers count " + flatsObject.getJSONObject("data").getInt("offers_count"));

				    Integer equalCount = 0;
				    Integer flatCount  = 0;
				    Integer cianCount  = 0;
				    for (String shape :shapes){
					    try {

					        response = new SendRequestTask().doInBackground(false, "http://map.cian.ru/ajax/map/roundabout/?deal_type=2&flats=yes&maxprice=8000000&currency=2&room2=1&room3=1&minkarea=8&mintarea=48&minfloor=2&minfloorn=6&engine_version=2&in_polygon[0]="+shape+"&_=1455551798781");
					    	
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

//							    try {
//
//							    	if (flatsObject.getJSONObject("data").getJSONObject("points").getJSONObject(pointPosition)!= null){
//							    		equalCount++;
//							    	}
//							    } catch (JSONException e) {
//									e.printStackTrace();
//								}
							    
						    	flatsObject.getJSONObject("data").getJSONObject("points").put(pointPosition, pointObject);
						    	
						    }
					    } catch (JSONException e) {
							e.printStackTrace();
						}
				    }
				    
				    Log.i("CianTask", "JSON: equal count " + equalCount);
				    Log.i("CianTask", "JSON: flat count " + flatCount);
				    Log.i("CianTask", "JSON: cian count " + cianCount);
				    

				    try {
				    	Integer pointCount = 0;
					    for (Iterator<String> pointIterator = flatsObject.getJSONObject("data").getJSONObject("points").keys(); pointIterator.hasNext();) {
					    	String pointPosition = pointIterator.next();
					    	
					    	JSONObject pointObject = flatsObject.getJSONObject("data").getJSONObject("points").getJSONObject(pointPosition);
				
					    	
					    	

					    	JSONArray flatObjects = pointObject.getJSONArray("offers");
					    	String flatAddress = pointObject.getJSONObject("content").getString("text");
					    	String clossestStation = null;

					    	if (flatObjects.length() < 50) { //не новостройки
						    	for (Integer i = 0; i < flatObjects.length(); i++) {
				
							    	JSONObject flatObject = (JSONObject) flatObjects.get(i);
							    			
							    	String flatId = flatObject.getString("id");
							    	pointCount++;

							    	
	
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
						
						           			Notification notification  = new Notification.Builder(downloaderService)
						                  	  .setCategory(Notification.CATEGORY_MESSAGE)
						                  	  .setContentTitle(Html.fromHtml(flatAddress + " " + clossestStation  + " " + flatType + " (" + flatArea + " | " + flatFlat +") " + flatPrice))
						                  	  .setStyle(new Notification.BigTextStyle().bigText(Html.fromHtml(flatAddress + " " + clossestStation  + " " + flatType + " (" + flatArea + " | " + flatFlat +") <b>" + flatPrice + "</b>")))
						                  	  .setSmallIcon(R.drawable.ic_notification)
						                  	  .addAction(R.drawable.ic_download, downloaderService.getString(R.string.open), uriPendingIntent)
						                  	  .addAction(R.drawable.ic_share, downloaderService.getString(R.string.share), sharePendingIntent)
						                  	  .build();
						
						                  	
						                  	notificationManager.notify("done", Integer.parseInt(flatId), notification);
						                  	//W/NotificationManager(30368): notify: id corrupted: sent 26874598, got back 0

						                  	
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
					    }
					    Log.i("CianTask", "operate count " + pointCount);
					    
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
