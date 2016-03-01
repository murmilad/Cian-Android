package info.akosarev.cianrobot;

import info.akosarev.cianrobot.R;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Calendar;
import java.util.Currency;
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
    private String maxprice;
    private String url;
    private Calendar c;
    private Integer messageId = 1;
    
    private LinkedList<String> shapes = new LinkedList<String>();

    static SharedPreferences settings;
	static SharedPreferences.Editor editor;

    private Set<String> taskIdSet = new HashSet<String>();

	LookCianTask(Service downloaderService, String url) {
		
		done = false;

		this.downloaderService = downloaderService;
		this.url               = url;

        Pattern maxpricePattern = Pattern.compile("maxprice=(\\d+)");
        Matcher maxpriceMatcher = maxpricePattern.matcher(url);

        if (maxpriceMatcher.find()){
        
        	String maxprice = maxpriceMatcher.group(1);
		
        	this.maxprice = maxprice;
        }

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

		downloaderService.startForeground(2, notificationProgressBuilder.build());

		SearchCian cian         = new SearchCian();
		SearchDomofond domofond = new SearchDomofond();

		CheckHandler handler = new CheckHandler(){
			public void check(HashMap<String, Object> flat) {
		    	String flatType = (String) flat.get("flatType");
		    	String flatFlat = (String) flat.get("flatFlat");
		    	String flatArea = (String) flat.get("flatArea");
		    	String flatId = (String) flat.get("flatId");
		    	String flatAddress = (String) flat.get("flatAddress");
		    	String clossestStation = (String) flat.get("clossestStation");
		    	String flatUrl = (String) flat.get("flatUrl");
		    	Long flatPrice = (Long) flat.get("flatPrice");

		    	
		    	DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
		        otherSymbols.setCurrencySymbol("р.");
		        otherSymbols.setGroupingSeparator(' ');

		        DecimalFormat dff = new DecimalFormat("###,###,###.##", otherSymbols);

		    	Long clossestDestantion = (Long) flat.get("clossestDestantion");

		    	Long oldFlatPrice = settings.getLong("price" + flatId, new Long(0));

		    	if (
		    			(!taskIdSet.contains(flatId) || !oldFlatPrice.equals(flatPrice))
		    			&& clossestDestantion <= 1000
		    	){
				
			
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                	shareIntent.putExtra(Intent.EXTRA_TEXT , "<a href=\"" + flatUrl + "\">" + flatAddress + "</a>");
                	shareIntent.setType("text/html");
                	PendingIntent sharePendingIntent = PendingIntent.getActivity(downloaderService.getApplicationContext(),
                			messageId++, Intent.createChooser(shareIntent, downloaderService.getString(R.string.share)), PendingIntent.FLAG_CANCEL_CURRENT);
                	
                	Intent uriIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(flatUrl));
           	    	PendingIntent uriPendingIntent =
           	    	        PendingIntent.getActivity(downloaderService, messageId, uriIntent, 0);
				
           			Notification notification  = new Notification.Builder(downloaderService)
                  	  .setCategory(Notification.CATEGORY_MESSAGE)
                  	  .setContentTitle(Html.fromHtml(flatAddress + " " + clossestStation  + " " + flatType + " (" + flatArea + " | " + flatFlat +") " + dff.format(flatPrice)))
                  	  .setStyle(new Notification.BigTextStyle().bigText(Html.fromHtml(flatAddress + " " + clossestStation  + " " + flatType + " (" + flatArea + " | " + flatFlat +") <b>" + dff.format(flatPrice) + "</b>" + (!oldFlatPrice.equals(flatPrice) && !new Long(0).equals(oldFlatPrice)
                  	  	? "<br> старая цена: " + dff.format(oldFlatPrice)
                  	  	: ""
                  	  ))))
                  	  .setSmallIcon(R.drawable.ic_notification)
                  	  .addAction(R.drawable.ic_download, downloaderService.getString(R.string.open), uriPendingIntent)
                  	  .addAction(R.drawable.ic_share, downloaderService.getString(R.string.share), sharePendingIntent)
                  	  .build();
				
				                  	
                  	notificationManager.notify("done", messageId, notification);
                  	//W/NotificationManager(30368): notify: id corrupted: sent 26874598, got back 0

				                  	
					Log.i("CianTask", "JSON: address " + flatAddress +  " flat " + flatType);
			
		    		taskIdSet.add(flatId);
		    		editor.putLong("price" + flatId, flatPrice);
					editor.putStringSet("taskId", taskIdSet);
					Log.i("CianTask", "commit " + flatId +  " apply " + editor.commit());
								
		    	} else {
		    		taskIdSet.add(flatId);
		    		editor.putLong("price" + flatId, flatPrice);
					editor.putStringSet("taskId", taskIdSet);

					Log.i("CianTask", "commit incapatible" + flatId +  " apply " + editor.commit());
		    	}

		    }    
				
		};

		while (true) {
    		try {
	
	    		if (((Integer)0).equals(((int)seconds % 60*2))) {
			
	    			List<HashMap<String, Object>> flats = new LinkedList<HashMap<String, Object>>();
	    			flats = cian.lookForFlats(settings, editor, handler);
	    			flats = domofond.lookForFlats(settings, editor, handler);
				    								    	
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

}


