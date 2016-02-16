package info.akosarev.cianrobot;

import info.akosarev.cianrobot.R;

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

public class LookCianTask implements Runnable {

	private Builder notificationProgressBuilder;
	private NotificationManager notificationManager;
	private Boolean done;

    private Service downloaderService;
    private String url;
    private Calendar c;

    static SharedPreferences settings;
	static SharedPreferences.Editor editor;

    private Set<String> taskIdSet = new HashSet<String>();

	LookCianTask(Service downloaderService, String url) {
		
		done = false;

		this.downloaderService = downloaderService;
		this.url               = url;

		c = Calendar.getInstance(); 

		notificationProgressBuilder  = new Notification.Builder(downloaderService)
		.setCategory(Notification.CATEGORY_MESSAGE)
		.setContentTitle("Cian service")
		.setContentText("Started " +  c.getTime())
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
    		c = Calendar.getInstance(); 

    		notificationProgressBuilder.setContentText("Started " +  c.getTime());

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
		
				    	for (Integer i = 0; i < flatObjects.length(); i++) {
		
					    	JSONObject flatObject = (JSONObject) flatObjects.get(i);
					    			
					    	String flatId = flatObject.getString("id");
		
					    	if (!taskIdSet.contains(flatId)){
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
			                  	  .setContentTitle(Html.fromHtml(flatAddress + " " + flatType + " (" + flatArea + " | " + flatFlat +") " + flatPrice))
			                  	  .setStyle(new Notification.BigTextStyle().bigText(Html.fromHtml(flatAddress + " " + flatType + " (" + flatArea + " | " + flatFlat +") <b>" + flatPrice + "</b>")))
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
    	}
		
	}
}
