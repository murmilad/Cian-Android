package info.akosarev.cianrobot;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.*;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.Notification.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

public class CianService extends Service {


	private Builder notificationProgressBuilder;
	private NotificationManager notificationManager;

	int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
	
    private static final String TAG = "CianService";

	static SharedPreferences settings;
	static SharedPreferences.Editor editor;

    private boolean isRunning  = false;
    private ThreadPoolExecutor executor;
    

    private Set<String> taskIdSet = new HashSet<String>();
   
    @Override
    public void onCreate() {
        Log.i(TAG, "Service onCreate");

        settings = PreferenceManager.getDefaultSharedPreferences(this);
        editor = settings.edit();

        executor = new ThreadPoolExecutor(
    		    NUMBER_OF_CORES*2,
    		    NUMBER_OF_CORES*2,
    		    60L,
    		    TimeUnit.SECONDS,
    		    new LinkedBlockingQueue<Runnable>()
    	);

        
        taskIdSet = settings.getStringSet("taskId", taskIdSet);
        
        isRunning = true;
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, final int startId) {

    	
    	Log.i(TAG, "Service onStartCommand " + startId);
	
	  	notificationManager =
	        	  (NotificationManager) this.getSystemService(this.NOTIFICATION_SERVICE);
	

    	if (intent != null && intent.getStringExtra("uri") != null) {
	        String sharedText = intent.getStringExtra("uri");
	
            
            SendRequestTask sendRequestTask = new SendRequestTask();
            sendRequestTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,false,sharedText); 

		    Log.i("CianTask", "JSON: uri " + sharedText);

			String response = "";
			try {
				response = sendRequestTask.get();
			} catch (InterruptedException e) {
				Log.i("CianTask", "sendRequest error: " + e.getStackTrace());
			} catch (ExecutionException e) {
				Log.i("CianTask", "sendRequest error: " + e.getStackTrace());
			}

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
		                	PendingIntent sharePendingIntent = PendingIntent.getActivity(this.getApplicationContext(),
		                			Integer.parseInt(flatId), Intent.createChooser(shareIntent, this.getString(R.string.share)), PendingIntent.FLAG_CANCEL_CURRENT);
		                	
		                	Intent uriIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(flatUrl));
		           	    	PendingIntent uriPendingIntent =
		           	    	        PendingIntent.getActivity(this, Integer.parseInt(flatId), uriIntent, 0);
		
		           			Notification notification  = new Notification.Builder(this)
		                  	  .setCategory(Notification.CATEGORY_MESSAGE)
		                  	  .setContentTitle(flatAddress)
		                  	  .setStyle(new Notification.BigTextStyle().bigText(flatAddress + " " + flatType + " (" + flatArea + " | " + flatFlat +") " + flatPrice))
		                  	  .setSmallIcon(R.drawable.ic_notification)
		                  	  .addAction(R.drawable.ic_download, this.getString(R.string.open), uriPendingIntent)
		                  	  .addAction(R.drawable.ic_share, this.getString(R.string.share), sharePendingIntent)
		                  	  .build();
		
		                  	notificationManager.notify("done", Integer.parseInt(flatId), notification);

							Log.i("CianTask", "JSON: address " + flatAddress +  " flat " + flatData.getString(0));

				    		taskIdSet.add(flatId);
							editor.putStringSet("taskId", taskIdSet);
							editor.commit();
		
				    	}
				    }
			    }
			} catch (JSONException e) {
				e.printStackTrace();
			}
		    
        }
        return Service.START_STICKY;
    }


    @Override
    public IBinder onBind(Intent arg0) {
        Log.i(TAG, "Service onBind");
        return null;
    }

    @Override
    public void onDestroy() {

    	
        isRunning = false;
        Log.i(TAG, "Service onDestroy");
    }

}
