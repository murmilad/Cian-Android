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
import android.text.Html;
import android.util.Log;

public class CianService extends Service {


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
	
	

    	if (intent != null && intent.getStringExtra("uri") != null) {
	        final String sharedText = intent.getStringExtra("uri");
	
	        executor.execute(new LookCianTask(this, sharedText));
	        
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
