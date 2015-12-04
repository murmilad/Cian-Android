package info.akosarev.tracksdownloader;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import android.util.Log;

public class LookDownloadTask implements Runnable {

	private Builder notificationProgressBuilder;
	private NotificationManager notificationManager;
	private Integer second;
	private Boolean done;

    private Service downloaderService;
    private Integer serviceId;
    private Integer taskId;

    private String albumName;
    private String url;

    static SharedPreferences settings;
	static SharedPreferences.Editor editor;

    private Set<String> taskIdSet = new HashSet<String>();

	LookDownloadTask(Service downloaderService, String albumName, String url, Integer serviceId, Integer taskId) {
		
		second = 0;
		done = false;

		this.downloaderService = downloaderService;
		this.albumName         = albumName;
		this.url               = url;
		this.serviceId         = serviceId;
		this.taskId            = taskId;
		
        settings = PreferenceManager.getDefaultSharedPreferences(downloaderService);
        editor = settings.edit();
		
        taskIdSet = settings.getStringSet("taskId", taskIdSet);

	}

	public void run() {
		Log.i("LookDownloadTask", "Background " + taskId);

		notificationProgressBuilder  = new Notification.Builder(downloaderService)
    	  .setCategory(Notification.CATEGORY_PROGRESS)
    	  .setContentTitle(albumName)
    	  .setContentText(albumName + " Download in progress")
    	  .setSmallIcon(R.drawable.ic_notification);

    	notificationManager =
          	  (NotificationManager) downloaderService.getSystemService(downloaderService.NOTIFICATION_SERVICE);

    	notificationManager.notify(taskId, notificationProgressBuilder.build());
    	
    	Integer progress = 0;

    	while (!done && second < 60*60*10) {

        	if (((Integer)0).equals(((int)second % 30))) {

        		Log.i("LookDownloadTask", "Check " + second + " Task " + taskId);

    			String response = new SendRequestTask().doInBackground(false, "http://akosarev.info/" + url);

        		Log.i("LookDownloadTask", "Response " + response);

                Pattern progressPattern = Pattern.compile("value:\\s*(\\d+)");
                Matcher progressMatcher = progressPattern.matcher(response);
                if (progressMatcher.find()){
                	progress = Integer.parseInt(progressMatcher.group(1));
            		Log.i("LookDownloadTask", "Progress " + progress);

            		
                	Log.i("LookDownloadTask", "Progress " + progress + " task " + taskId);
                	notificationProgressBuilder.setProgress(100, progress, false);
                	notificationManager.notify(taskId, notificationProgressBuilder.build());

                } else {
                
	                Pattern downloadPattern = Pattern.compile("All=\\s*<a\\s+href\\s*=\\s*'([^']+)'\\s*>\\s*Download");
	                Matcher downloadMatcher = downloadPattern.matcher(response);
		
	                if (downloadMatcher.find()){
	                
	                	String downloadLink = downloadMatcher.group(1);
	           			Log.i("LookDownloadTask", "Download link " + downloadLink);
	
	                	notificationProgressBuilder.setProgress(0, 0, false);
	                	notificationManager.notify(taskId, notificationProgressBuilder.build());
	                	notificationManager.cancel(taskId); 
	
	
	                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
	                	shareIntent.putExtra(Intent.EXTRA_TEXT , "<a href=\"http://akosarev.info/" + downloadLink + "\">" + albumName + "</a>");
	                	shareIntent.setType("text/html");
	                	PendingIntent sharePendingIntent = PendingIntent.getActivity(downloaderService.getApplicationContext(),
	                			taskId, Intent.createChooser(shareIntent, downloaderService.getString(R.string.share)), PendingIntent.FLAG_CANCEL_CURRENT);
	                	
	                	Intent uriIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://akosarev.info/" + downloadLink));
	           	    	PendingIntent uriPendingIntent =
	           	    	        PendingIntent.getActivity(downloaderService, taskId, uriIntent, 0);
	
	           			Notification notification  = new Notification.Builder(downloaderService)
	                  	  .setCategory(Notification.CATEGORY_MESSAGE)
	                  	  .setContentTitle(albumName)
	                  	  .setContentText("http://akosarev.info/" + downloadLink)
	                  	  .setSmallIcon(R.drawable.ic_notification)
	                  	  .addAction(R.drawable.ic_download, downloaderService.getString(R.string.download), uriPendingIntent)
	                  	  .addAction(R.drawable.ic_share, downloaderService.getString(R.string.share), sharePendingIntent)
	                  	  .build();
	
	                  	notificationManager.notify("done", taskId, notification);
	                  	

	                  	taskIdSet.remove(taskId.toString());

	                  	editor.putStringSet("taskId", taskIdSet);
						editor.remove("ajaxUri" + taskId.toString());
						editor.remove("headerText" + taskId.toString());
						editor.commit();
						done = true;

	                }
                }
        		Log.i("LookDownloadTask", "Tick " + second + " Task " + taskId);
        	}
        	
        	try {
        	    Thread.sleep(1000);
        	} catch(InterruptedException ex) {
        	    Thread.currentThread().interrupt();
        	}
        	second++;

        }
		
	}
}
