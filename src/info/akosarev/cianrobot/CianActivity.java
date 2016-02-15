package info.akosarev.cianrobot;


import info.akosarev.tracksdownloader.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class CianActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloader);

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
            	Intent serviceIntent = new Intent(this, CianService.class);
            	if (intent.getStringExtra(Intent.EXTRA_TEXT) != null) {
            		serviceIntent.putExtra("uri", intent.getStringExtra(Intent.EXTRA_TEXT));

            		startService(serviceIntent); // Handle text being sent
            	} else {
                	Toast.makeText(this, "Wrong url!", Toast.LENGTH_LONG).show();
                }
            } 
        }

        this.finish();
    }

}
