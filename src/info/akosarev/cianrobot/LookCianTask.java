package info.akosarev.cianrobot;

import info.akosarev.cianrobot.R;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
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
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

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
    private Set<String> fraudIdSet = new HashSet<String>();

    LookCianTask(Service downloaderService) {
		
		done = false;

		this.downloaderService = downloaderService;

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
        fraudIdSet = settings.getStringSet("fraudId", fraudIdSet);
//        // delete last 150 items        
//        int i = 0;
//        int size = taskIdSet.size();
//        Set<String> taskIdSetDelete = new HashSet<String>();
//        for (String taskId: taskIdSet){
//        	if (i > size - 150 ) {
//        		taskIdSetDelete.add(taskId);
//        	}
//        	
//        	i++;
//        }
//        for (String taskId: taskIdSetDelete){
//    		taskIdSet.remove(taskId);
//        }
	}

	void addFraud (String fraudString) {
		if (!fraudIdSet.contains(fraudString)) {
			fraudIdSet.add(fraudString);
			editor.putStringSet("fraudId", fraudIdSet);

			Log.i("CianTask", "commit fraud " + fraudString + " " + editor.commit());
		}
	}
	
	public void run() {
		Integer seconds = 0;
		notificationManager =
	          	  (NotificationManager) downloaderService.getSystemService(downloaderService.NOTIFICATION_SERVICE);

		downloaderService.startForeground(2, notificationProgressBuilder.build());

		SearchYandex yandex     = null;
		SearchCian cian         = null;
		SearchDomofond domofond = null;

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

		    	Long oldFlatPrice = settings.getLong("price" + flatId,  Long.valueOf(0));

	    		Long disappearedFlat = settings.getLong("flatDisappeared" + (flatAddress + "&" + flatFlat + "&" + flatArea + "&" + flatType + "&" + flatPrice).replaceAll("\\s",""), 0);
	    		
		    	if (
		    			(!taskIdSet.contains(flatId) || !oldFlatPrice.equals(flatPrice))
		    			&& !fraudIdSet.contains((flatAddress).replaceAll("\\s",""))
		    			&&  Long.valueOf(0).equals(disappearedFlat)
		    	){
				
			
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                	shareIntent.putExtra(Intent.EXTRA_TEXT , "<a href=\"" + flatUrl + "\">" + flatAddress + "</a>");
                	shareIntent.setType("text/html");
                	PendingIntent sharePendingIntent = PendingIntent.getActivity(downloaderService.getApplicationContext(),
                			++messageId, Intent.createChooser(shareIntent, downloaderService.getString(R.string.share)), PendingIntent.FLAG_CANCEL_CURRENT);


                	Intent fraudIntent = new Intent();
                	fraudIntent.setAction("taskId");
                	fraudIntent.addCategory(Intent.CATEGORY_DEFAULT);
                	fraudIntent.putExtra("taskId", flatId);
                	PendingIntent fraudPendingIntent = PendingIntent.getBroadcast(downloaderService,
                			messageId, fraudIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                	Intent uriIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(flatUrl));
           	    	PendingIntent uriPendingIntent =
           	    	        PendingIntent.getActivity(downloaderService, messageId, uriIntent, 0);
				
           			Notification notification  = new Notification.Builder(downloaderService)
                  	  .setCategory(Notification.CATEGORY_MESSAGE)
                  	  .setContentTitle(Html.fromHtml(flatAddress + " " + clossestStation  + " " + flatType + " (" + flatArea + " | " + flatFlat +") " + dff.format(flatPrice)))
                  	  .setStyle(new Notification.BigTextStyle().bigText(Html.fromHtml(flatAddress + " " + clossestStation  + " " + flatType + " (" + flatArea + " | " + flatFlat +") <b>" + dff.format(flatPrice) + "</b>" + (!oldFlatPrice.equals(flatPrice) && ! Long.valueOf(0).equals(oldFlatPrice)
                  	  	? "<br> старая цена: " + dff.format(oldFlatPrice)
                  	  	: ""
                  	  ))))
                  	  .setSmallIcon(R.drawable.ic_notification)
                  	  .addAction(R.drawable.ic_download, downloaderService.getString(R.string.open), uriPendingIntent)
                  	  .addAction(R.drawable.ic_share, downloaderService.getString(R.string.share), sharePendingIntent)
                	  .addAction(R.drawable.ic_fraud, downloaderService.getString(R.string.fraud), fraudPendingIntent)
                  	  .build();
				
				                  	
                  	notificationManager.notify("done", messageId, notification);
                  	//W/NotificationManager(30368): notify: id corrupted: sent 26874598, got back 0

				                  	
					Log.i("CianTask", "JSON: address " + flatAddress +  " flat " + flatType);
			
		    		taskIdSet.add(flatId);
		    		editor.putLong("price" + flatId, flatPrice);
		    		editor.putString("flatAddress" + flatId, flatAddress);
		    		editor.putString("flatClossestStation" + flatId, clossestStation);
		    		editor.putString("flatType" + flatId, flatType);
		    		editor.putString("flatUrl" + flatId, flatUrl);
		    		editor.putString("flatArea" + flatId, flatArea);
		    		editor.putString("flatFlat" + flatId, flatFlat);
					editor.putStringSet("taskId", taskIdSet);
					Log.i("CianTask", "commit " + flatId +  " apply " + editor.commit());

				    try {
		        	    Thread.sleep(5000);
		        	} catch(InterruptedException ex) {
		        		ex.printStackTrace();
		        	}

		    	} else {
		    		taskIdSet.add(flatId);
		    		editor.putLong("price" + flatId, flatPrice);
		    		editor.putString("flatAddress" + flatId, flatAddress);
		    		editor.putString("flatClossestStation" + flatId, clossestStation);
		    		editor.putString("flatType" + flatId, flatType);
		    		editor.putString("flatUrl" + flatId, flatUrl);
		    		editor.putString("flatArea" + flatId, flatArea);
		    		editor.putString("flatFlat" + flatId, flatFlat);

					if (! Long.valueOf(0).equals(disappearedFlat)) {
						String disappearedFlatId = settings.getString("flatIdDisappeared" + (flatAddress + "&" + flatFlat + "&" + flatArea + "&" + flatType + "&" + flatPrice).replaceAll("\\s",""), "");
						if (!"".equals(disappearedFlatId)) {
							taskIdSet.remove(disappearedFlatId);
	                    	editor.remove("price" + disappearedFlatId);
	    		    		editor.remove("flatAddress" + disappearedFlatId);
	    		    		editor.remove("flatClossestStation" + disappearedFlatId);
	    		    		editor.remove("flatType" + disappearedFlatId);
	    		    		editor.remove("flatArea" + disappearedFlatId);
	    		    		editor.remove("flatFlat" + disappearedFlatId);
	    		    		editor.remove("flatUrl" + disappearedFlatId);
	    		    		editor.remove("flatDisappeared" + (flatAddress + "&" + flatFlat + "&" + flatArea + "&" + flatType + "&" + oldFlatPrice).replaceAll("\\s",""));
	    		    		editor.remove("flatIdDisappeared" + (flatAddress + "&" + flatFlat + "&" + flatArea + "&" + flatType + "&" + oldFlatPrice).replaceAll("\\s",""));
						}
					}

					editor.putStringSet("taskId", taskIdSet);
					Log.i("CianTask", "commit incapatible " + flatId +  " apply " + editor.commit());
		    	}

		    }    
				
		};

		while (true) {
	
    		if (((Integer)0).equals(((int)seconds % 60*4))) {
    			Set<String> flats = new HashSet<String>();
    			try {
		
        			if (yandex == null) {
        				yandex     = new SearchYandex();
        			}
	    			flats.addAll(yandex.lookForFlats(settings, editor, handler));

	    			if (cian == null) {
        				cian     = new SearchCian();
        			}
	    			flats.addAll(cian.lookForFlats(settings, editor, handler));

        			if (domofond == null) {
        				domofond = new SearchDomofond();
        			}
	    			flats.addAll(domofond.lookForFlats(settings, editor, handler));
	    		} catch(final IOException ex) {
	    			Log.w("CianTask", ex.toString());
	    			Handler h = new Handler(Looper.getMainLooper());

    			    h.post(new Runnable() {
    			        public void run() {
    			             Toast.makeText(downloaderService, "Cian service IO error: " + ex.getMessage() + "!", Toast.LENGTH_LONG).show();
    			        }
    			    });
	    			
	        		ex.printStackTrace();
	        		flats = taskIdSet;
	        	} catch(final Exception ex) {
	    			Log.w("CianTask", ex.toString());
	    			Handler h = new Handler(Looper.getMainLooper());

    			    h.post(new Runnable() {
    			        public void run() {
    			             Toast.makeText(downloaderService, "Cian service error: " + ex.getMessage() + "!", Toast.LENGTH_LONG).show();
    			        }
    			    });
	        		ex.printStackTrace();
	        		flats = taskIdSet;
	        	}

	    			
	    			Set<String> newTaskIdSet = new HashSet<String> ();

	    			for (String flatId : taskIdSet) {
	    				if (flats.contains(flatId)) {
	    					newTaskIdSet.add(flatId);
	    				} else {
	    			    	DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
	    			        otherSymbols.setCurrencySymbol("р.");
	    			        otherSymbols.setGroupingSeparator(' ');

	    			        DecimalFormat dff = new DecimalFormat("###,###,###.##", otherSymbols);

	    					String flatAddress = settings.getString("flatAddress" + flatId, "");
	    		    		String clossestStation = settings.getString("flatClossestStation" + flatId, "");
	    		    		String flatType = settings.getString("flatType" + flatId, "");
	    		    		String flatArea = settings.getString("flatArea" + flatId, "");
	    		    		String flatFlat = settings.getString("flatFlat" + flatId, "");
	    		    		String flatUrl = settings.getString("flatUrl" + flatId, "");
	    			    	Long oldFlatPrice = settings.getLong("price" + flatId,  Long.valueOf(0));

	    			    	if (!fraudIdSet.contains((flatAddress).replaceAll("\\s",""))) {

	    			    		Long disappearedFlat = settings.getLong("flatDisappeared" + (flatAddress + "&" + flatFlat + "&" + flatArea + "&" + flatType + "&" + oldFlatPrice).replaceAll("\\s",""), 0);

	    			    		Log.i("CianTask", "flatDisappeared " + (flatAddress + "&" + flatFlat + "&" + flatArea + "&" + flatType + "&" + oldFlatPrice).replaceAll("\\s","") +  " " + disappearedFlat + " time "  + new java.util.Date().getTime());

	    			    		if (
	    			    				new java.util.Date().getTime() - 60 * 60 * 24 * 2 * 1000 > disappearedFlat
	    			    				&& !Long.valueOf(0).equals(disappearedFlat)
	    			    		) {
		    			    		Log.i("CianTask", "flatDisappeared message " + (flatAddress + "&" + flatFlat + "&" + flatArea + "&" + flatType + "&" + oldFlatPrice).replaceAll("\\s","") +  " " + disappearedFlat + " time "  + new java.util.Date().getTime());

//	    			    			Intent shareIntent = new Intent(Intent.ACTION_SEND);
//			                    	shareIntent.putExtra(Intent.EXTRA_TEXT , "<a href=\"" + flatUrl + "\">" + flatAddress + "</a>");
//			                    	shareIntent.setType("text/html");
//			                    	PendingIntent sharePendingIntent = PendingIntent.getActivity(downloaderService.getApplicationContext(),
//			                    			++messageId, Intent.createChooser(shareIntent, downloaderService.getString(R.string.share)), PendingIntent.FLAG_CANCEL_CURRENT);
//		
//		
//			                    	Intent uriIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(flatUrl));
//			               	    	PendingIntent uriPendingIntent =
//			               	    	        PendingIntent.getActivity(downloaderService, messageId, uriIntent, 0);
//		
//			    					Notification notification  = new Notification.Builder(downloaderService)
//			                    	  .setCategory(Notification.CATEGORY_MESSAGE)
//			                    	  .setContentTitle(Html.fromHtml("Не актуальна " + flatAddress + " " + clossestStation  + " " + flatType + " (" + flatArea + " | " + flatFlat +")"))
//			                    	  .setStyle(new Notification.BigTextStyle().bigText(Html.fromHtml(flatAddress + " " + clossestStation  + " " + flatType + " (" + flatArea + " | " + flatFlat +") <b> не актуальна с " + new SimpleDateFormat("dd.MM.yyyy HH:mm").format(new java.util.Date(disappearedFlat)) + "</b><br> старая цена: " + dff.format(oldFlatPrice)
//			                    	  )))
//			                    	  .setSmallIcon(R.drawable.ic_notification)
//			                    	  .addAction(R.drawable.ic_download, downloaderService.getString(R.string.open), uriPendingIntent)
//			                    	  .addAction(R.drawable.ic_share, downloaderService.getString(R.string.share), sharePendingIntent)
//			                    	  .build();
//
//			    					
//			  				                  	
//			                    	notificationManager.notify("done", messageId, notification);

			                    	editor.remove("price" + flatId);
			    		    		editor.remove("flatAddress" + flatId);
			    		    		editor.remove("flatClossestStation" + flatId);
			    		    		editor.remove("flatType" + flatId);
			    		    		editor.remove("flatArea" + flatId);
			    		    		editor.remove("flatFlat" + flatId);
			    		    		editor.remove("flatUrl" + flatId);
			    		    		editor.remove("flatDisappeared" + (flatAddress + "&" + flatFlat + "&" + flatArea + "&" + flatType + "&" + oldFlatPrice).replaceAll("\\s",""));
			    		    		editor.remove("flatIdDisappeared" + (flatAddress + "&" + flatFlat + "&" + flatArea + "&" + flatType + "&" + oldFlatPrice).replaceAll("\\s",""));
			    		    		Log.i("CianTask", "commit old " + flatId +  " apply " + editor.commit());

	    			    		} else if ( Long.valueOf(0).equals(disappearedFlat)) {
	    			    			editor.putLong("flatDisappeared" + (flatAddress + "&" + flatFlat + "&" + flatArea + "&" + flatType + "&" + oldFlatPrice).replaceAll("\\s",""), new java.util.Date().getTime());
	    			    			editor.putString("flatIdDisappeared" + (flatAddress + "&" + flatFlat + "&" + flatArea + "&" + flatType + "&" + oldFlatPrice).replaceAll("\\s",""), flatId);
		    			    		Log.i("CianTask", "flatDisappeared new " + (flatAddress + "&" + flatFlat + "&" + flatArea + "&" + flatType + "&" + oldFlatPrice).replaceAll("\\s","") +  " " + disappearedFlat + " time "  + new java.util.Date().getTime());
		    			    		
	    			    			newTaskIdSet.add(flatId);
	    			    		} else {
		    			    		Log.i("CianTask", "flatDisappeared old " + (flatAddress + "&" + flatFlat + "&" + flatArea + "&" + flatType + "&" + oldFlatPrice).replaceAll("\\s","") +  " " + disappearedFlat + " time "  + new java.util.Date().getTime());

	    			    			newTaskIdSet.add(flatId);
	    			    		}
	    			    	} else {

		    		    		editor.remove("price" + flatId);
		    		    		editor.remove("flatAddress" + flatId);
		    		    		editor.remove("flatClossestStation" + flatId);
		    		    		editor.remove("flatType" + flatId);
		    		    		editor.remove("flatArea" + flatId);
		    		    		editor.remove("flatFlat" + flatId);
		    		    		editor.remove("flatUrl" + flatId);
		    		    		Log.i("CianTask", "commit old " + flatId +  " apply " + editor.commit());
		    		    		
		    		    		newTaskIdSet.add(flatId);
	    			    	}

	    					

	    					try {
	    		        	    Thread.sleep(5000);
	    		        	} catch(InterruptedException ex) {
	    		        		ex.printStackTrace();
	    		        	}
	    					
	    				}
	    			}

	    			taskIdSet = newTaskIdSet;
					editor.putStringSet("taskId", newTaskIdSet);
					Log.i("CianTask", "commit " + editor.commit());


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


