package info.akosarev.tracksdownloader;

import java.util.HashMap;
import java.util.HashSet;
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

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

public class DownloaderService extends Service {


	int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
	
    private static final String TAG = "DownloaderService";

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
        
        for (String taskId : taskIdSet) {
        	Log.i(TAG, "Service onCreate tid " + taskId);
        	executor.execute(new LookDownloadTask(DownloaderService.this, settings.getString("headerText" + taskId, ""), settings.getString("ajaxUri" + taskId, ""), 1, Integer.parseInt(taskId)));
        }
        isRunning = true;
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, final int startId) {

    	
    	
    	Log.i(TAG, "Service onStartCommand " + startId);

    	if (intent != null && intent.getStringExtra("uri") != null) {
	        String sharedText = intent.getStringExtra("uri");
	
	        Pattern uriPattern = Pattern.compile("8tracks: (.+)(http:[^ ]+) ");
	        Matcher uriMatcher = uriPattern.matcher(sharedText);
	        
	        if (uriMatcher.find()) {
	        	final String headerText = uriMatcher.group(1);
	        	String uri = uriMatcher.group(2);
	        	
	            // Create a new HttpClient and Post Header
	            HashMap<String, String> postDataParams = new HashMap<String, String>();
	            postDataParams.put("interface", "task");
	            postDataParams.put("form", "8tracks_downloader_task");
	            postDataParams.put("current_action", "A-u");
	            postDataParams.put("next_form", "");
	            postDataParams.put("helper_enubled", "false");
	            postDataParams.put("link", uri);
	            postDataParams.put("submit_button", "Отправить");
	            postDataParams.put("token", "2");
	            
	            SendRequestTask sendRequestTask = new SendRequestTask();
	            sendRequestTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,true,"http://akosarev.info/engine/", postDataParams); 
	
				String response = "";
				try {
					response = sendRequestTask.get();
				} catch (InterruptedException e) {
					Log.i("SendRequestTask", "sendRequest error: " + e.getStackTrace());
				} catch (ExecutionException e) {
					Log.i("SendRequestTask", "sendRequest error: " + e.getStackTrace());
				}
	
			    Log.i("SendRequestTask", "Link: response " + response);
	
			    Pattern ajaxPattern = Pattern.compile("url: \"([^\"]+)\"");
			    Matcher ajaxMatcher = ajaxPattern.matcher(response);
			    
			    if (ajaxMatcher.find()) {
	
			    	String ajaxUri = ajaxMatcher.group(1);
	
	
				    Pattern taskIdPattern = Pattern.compile("task_id=(\\d+)");
				    Matcher taskIdMatcher = taskIdPattern.matcher(ajaxUri);
				    if (taskIdMatcher.find()) {
				    	Log.i("SendRequestTask", "Header: " + ajaxUri);
				    	Integer taskId = Integer.parseInt(taskIdMatcher.group(1));


						taskIdSet.add(taskId.toString());

						editor.putStringSet("taskId", taskIdSet);
						editor.putString("ajaxUri" + taskId.toString(), ajaxUri);
						editor.putString("headerText" + taskId.toString(), headerText);
						editor.commit();

						executor.execute(new LookDownloadTask(DownloaderService.this, headerText, ajaxUri, startId, taskId));
				    	
				    }
			    	
			    }
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
