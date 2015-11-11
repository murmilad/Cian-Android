package info.akosarev.tracksdownloader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Notification.Builder;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

public class LookDownloadTask extends AsyncTask<Object, Integer, Void> {

	private Builder notificationProgressBuilder;
	private NotificationManager notificationManager;
	private Integer second;
	private Boolean done;

    private Service downloaderService;
    private Integer serviceId;
    private Integer taskId;

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		
		second = 0;
		done = false;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);

		Integer progress = values[0];
    	Log.i("LookDownloadTask", "Progress " + progress + " task " + taskId);
    	notificationProgressBuilder.setProgress(100, progress, false);
    	notificationManager.notify(taskId, notificationProgressBuilder.build());
    	
	}
	
	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
		downloaderService.stopSelf(serviceId);
	}

	@Override
	protected Void doInBackground(Object...  objects) {

		downloaderService = (Service) objects[0];
		serviceId         = (Integer) objects[3];
		taskId            = (Integer) objects[4];

		String  albumName         = (String) objects[1];
		String  url               = (String) objects[2];

		Log.i("LookDownloadTask", "Background " + taskId);

		notificationProgressBuilder  = new Notification.Builder(downloaderService)
    	  .setCategory(Notification.CATEGORY_PROGRESS)
    	  .setContentTitle(albumName)
    	  .setContentText("Download in progress")
    	  .setSmallIcon(R.drawable.ic_notification);

    	notificationManager =
          	  (NotificationManager) downloaderService.getSystemService(downloaderService.NOTIFICATION_SERVICE);

    	notificationManager.notify(taskId, notificationProgressBuilder.build());
    	
    	Integer progress = 0;

    	while (!done && second < 60*60*10) {

        	try {
        	    Thread.sleep(1000);
        	} catch(InterruptedException ex) {
        	    Thread.currentThread().interrupt();
        	}
        	second++;

        	if (((Integer)0).equals(((int)second % 30))) {

        		Log.i("LookDownloadTask", "Check " + second + " Task " + taskId);

    			String response = new SendRequestTask().doInBackground(false, "http://akosarev.info/" + url);

        		Log.i("LookDownloadTask", "Response " + response);

                Pattern progressPattern = Pattern.compile("value:\\s*(\\d+)");
                Matcher progressMatcher = progressPattern.matcher(response);
                if (progressMatcher.find()){
                	progress = Integer.parseInt(progressMatcher.group(1));
            		Log.i("LookDownloadTask", "Progress " + progress);
            		publishProgress(progress);
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
	                  	done = true;
	            	}
                }
        		Log.i("LookDownloadTask", "Tick " + second + " Task " + taskId);
        	}
        }

		return null;
     }
}
