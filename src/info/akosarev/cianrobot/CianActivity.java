package info.akosarev.cianrobot;


import java.util.LinkedList;

import info.akosarev.cianrobot.R;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class CianActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


		Intent serviceIntent = new Intent(this, CianService.class);
		startService(serviceIntent);

		this.finish();
    }

}
// 
