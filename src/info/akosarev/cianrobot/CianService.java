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
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;

public class CianService extends IntentService {

    public CianService() {
		super("cianService");
	}

	private static final String TAG = "CianService";

	static SharedPreferences settings;
	static SharedPreferences.Editor editor;

    private boolean isRunning  = false;
    

    private Set<String> taskIdSet = new HashSet<String>();
   
    @Override
    public void onHandleIntent(final Intent intent) {

    	Log.i(TAG, "Service onStartCommand");
	

    	if (intent != null && intent.getStringExtra("uri") != null) {
	        final String sharedText = intent.getStringExtra("uri");

	        final LookCianTask task = new LookCianTask(this, sharedText);
	        
	        task.run();
	        
        }
    }

}
