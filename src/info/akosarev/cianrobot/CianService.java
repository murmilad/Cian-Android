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

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.Notification.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;

public class CianService extends IntentService {

	BroadcastReceiver receiver;

	private LookCianTask  cianTask ; 
    public CianService() {
		super("cianService");
	}

	private static final String TAG = "CianService";

	static SharedPreferences settings;
	static SharedPreferences.Editor editor;

    private boolean isRunning  = false;
    Context applicationContext;

    @Override
    public void onCreate() {
    	// TODO Auto-generated method stub
    	super.onCreate();
        IntentFilter filter = new IntentFilter("taskId");
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        // Add other actions as needed
		
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
            	Log.i("CianTask", "fraud action " + intent.getAction());
                if (intent.getAction().equals("taskId")) {

    	    		String taskId = intent.getStringExtra("taskId");

    	    		Log.i("CianTask", "fraud received " + taskId);
        			
    				String flatAddress = settings.getString("flatAddress" + taskId, "");
    	    		String flatType = settings.getString("flatType" + taskId, "");
    	    		String flatArea = settings.getString("flatArea" + taskId, "");
    	    		String flatFlat = settings.getString("flatFlat" + taskId, "");

    	    		Log.i("CianTask", "line fraud " + flatAddress + "&" + flatFlat + "&" + flatArea + "&" + flatType);

    	    		if (!"&&&".equals(flatAddress + "&" + flatFlat + "&" + flatArea + "&" + flatType)) {
    	    			cianTask.addFraud(flatAddress + "&" + flatFlat + "&" + flatArea + "&" + flatType);
    	    		}
            		
                }
            }
            

        };

        registerReceiver(receiver, filter);
    }

    @Override
    public void onHandleIntent(final Intent intent) {

    	Log.i(TAG, "Service onStartCommand");
	
    	

    	if (intent != null) {
    	        cianTask = new LookCianTask(this);
    	        
    			cianTask.run();
    	}
    }
    

    @Override
    public void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	unregisterReceiver(receiver);
    }

}
