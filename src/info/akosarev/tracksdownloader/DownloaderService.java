package info.akosarev.tracksdownloader;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.IBinder;
import android.text.Html;
import android.util.Log;

public class DownloaderService extends Service {

    private static final String TAG = "DownloaderService";

	static SharedPreferences settings;

    private boolean isRunning  = false;
    private HashMap<Integer, Boolean> runningTasks = new HashMap<Integer, Boolean>() ;
   
  	private NotificationManager notificationManager;

    
    @Override
    public void onCreate() {
        Log.i(TAG, "Service onCreate");


        isRunning = true;
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, final int startId) {
    	
    	Log.i(TAG, "Service onStartCommand " + startId);

    	runningTasks.put(startId, true);
    	notificationManager =
          	  (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    	
        String sharedText = intent.getStringExtra("uri");

        
//        String downloadLink = "";
//        String headerTextd = "Test";
//        
//        
//        Intent resultIntent = new Intent(Intent.ACTION_VIEW);
//        resultIntent.setData(Uri.parse("http://akosarev.info/"));
//        PendingIntent pending = PendingIntent.getActivity(this, 0, resultIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
//        
//        Intent shareIntent = new Intent(Intent.ACTION_SEND);
//    	shareIntent.putExtra(Intent.EXTRA_TEXT , "<a href=\"http://akosarev.info/" + downloadLink + "\">" + headerTextd + "</a>");
//    	shareIntent.setType("text/html");
//    	
//    	PendingIntent sharePendingIntent = PendingIntent.getActivity(getApplicationContext(),
//    	    0, Intent.createChooser(shareIntent, getString(R.string.share)), PendingIntent.FLAG_CANCEL_CURRENT);
//    	
//    	Intent uriIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://akosarev.info/" + downloadLink));
//	    	PendingIntent uriPendingIntent =
//	    	        PendingIntent.getActivity(DownloaderService.this, 0, uriIntent, 0);
//
//			Notification notification  = new Notification.Builder(DownloaderService.this)
//      	  .setCategory(Notification.CATEGORY_MESSAGE)
//      	  .setContentTitle(headerTextd)
//      	  .setContentText("http://akosarev.info/" + downloadLink)
//      	  .setSmallIcon(R.drawable.ic_notification)
//      	  .addAction(R.drawable.ic_download, getString(R.string.download), uriPendingIntent)
//      	  .addAction(R.drawable.ic_share, getString(R.string.share), sharePendingIntent)
//      	  .setContentIntent(pending)
//      	  .build();
//          	final NotificationManager notificationManager =
//                	  (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//                	notificationManager.notify(startId+100, notification);
//                	
//                	
                	
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
            new SendRequestTask().execute(true, new Handler(){

				public void handle(String response) {

		            Log.i("SendRequestTask", "Link: response " + response);

		            Pattern ajaxPattern = Pattern.compile("url: \"([^\"]+)\"");
		            Matcher ajaxMatcher = ajaxPattern.matcher(response);
		            if (ajaxMatcher.find()) {
		            	final String ajaxUri = ajaxMatcher.group(1);

		            	Log.i("SendRequestTask", "Header: " + ajaxUri);
		            	

		            	new Thread(new Runnable() {
			                  public void run() {
			                  	Integer second = 0;

			                  	final Builder notificationProgressBuilder  = new Notification.Builder(DownloaderService.this)
			                  	  .setCategory(Notification.CATEGORY_PROGRESS)
		                      	  .setContentTitle(headerText)
		                      	  .setContentText("Download in progress")
		                      	  .setSmallIcon(R.drawable.ic_notification)
		                      	  .setProgress(100, 0, false);

		                      	notificationManager.notify(startId+100, notificationProgressBuilder.build());
				                  
			                	while (runningTasks.containsKey(startId) && second < 60*60*10) {
				
			                		Log.i("SendRequestTask", "Wait: " + second + " service: " + startId);
				                	try {
				                	    Thread.sleep(1000);
				                	} catch(InterruptedException ex) {
				                	    Thread.currentThread().interrupt();
				                	}
				                	second++;

				                	if (second % 30 == 0) {
				                		new SendRequestTask().execute(false, new Handler(){
	
											public void handle(String response) {
						                    	if (runningTasks.containsKey(startId)) {
								                    Pattern progressPattern = Pattern.compile("value:\\s*(\\d+)");
								                    Matcher progressMatcher = progressPattern.matcher(response);
								                    if (progressMatcher.find()){
								                    	Integer progress = Integer.parseInt(progressMatcher.group(1));
								                		Log.i("SendRequestTask", "Prigress " + progress);
								                    	notificationProgressBuilder.setProgress(100, progress, false);
								                    	notificationManager.notify(startId+100, notificationProgressBuilder.build());
								                    }
								                    
	
						                    		Pattern downloadPattern = Pattern.compile("All=\\s*<a\\s+href\\s*=\\s*'([^']+)'\\s*>\\s*Download");
								                    Matcher downloadMatcher = downloadPattern.matcher(response);
									
								                    if (downloadMatcher.find()){
									                    	String downloadLink = downloadMatcher.group(1);
								                   			Log.i("SendRequestTask", "Download link " + downloadLink);
	
									                    	notificationProgressBuilder.setProgress(0, 0, false);
									                    	notificationManager.notify(startId+100, notificationProgressBuilder.build());
									                    	notificationManager.cancel(startId+100); 
	
	
									                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
									                    	shareIntent.putExtra(Intent.EXTRA_TEXT , "<a href=\"http://akosarev.info/" + downloadLink + "\">" + headerText + "</a>");
									                    	shareIntent.setType("text/html");
									                    	PendingIntent sharePendingIntent = PendingIntent.getActivity(getApplicationContext(),
									                    	    0, Intent.createChooser(shareIntent, getString(R.string.share)), PendingIntent.FLAG_CANCEL_CURRENT);
									                    	
									                    	Intent uriIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://akosarev.info/" + downloadLink));
								                   	    	PendingIntent uriPendingIntent =
								                   	    	        PendingIntent.getActivity(DownloaderService.this, 0, uriIntent, 0);
			
								                   			Notification notification  = new Notification.Builder(DownloaderService.this)
									                      	  .setCategory(Notification.CATEGORY_MESSAGE)
									                      	  .setContentTitle(headerText)
									                      	  .setContentText("http://akosarev.info/" + downloadLink)
									                      	  .setSmallIcon(R.drawable.ic_notification)
									                      	  .addAction(R.drawable.ic_download, getString(R.string.download), uriPendingIntent)
									                      	  .addAction(R.drawable.ic_share, getString(R.string.share), sharePendingIntent)
									                      	  .build();
			
									                      	notificationManager.notify(startId, notification );
											                    	
								                   			Log.i("SendRequestTask", "Stop " + startId + " Running " + runningTasks.size());
	
								                   			runningTasks.remove(startId);
								                   			if (runningTasks.size() == 0) {
								                   				stopSelf();
								                   			}
								                    }
						                    	}
											}
				                			
				                		}, "http://akosarev.info/" + ajaxUri);
				                	 }
				                  }
			                  }
			               }).start();
		                	
		                } else {
		                	Log.e("SendRequestTask", "Wrong API");
		                }
					
				}
            	
            }, "http://akosarev.info/engine/", postDataParams);
            
        }
        
     // Register the receiver to get data


//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//            	
//
//                for (int i = 0; i < 5; i++) {
//                    try {
//                        Thread.sleep(1000);
//                    } catch (Exception e) {
//                    }
//
//                    if(isRunning){
//                        Log.i(TAG, "Service running");
//                    }
//                }
//
//                stopSelf();
//            }
//        }).start();

        return Service.START_NOT_STICKY;
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
