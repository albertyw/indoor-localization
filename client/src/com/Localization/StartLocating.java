package com.Localization;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import org.json.simple.JSONValue;

public class StartLocating extends Activity {

	static Handler dataPushHandler;
	boolean dataPushHandlerActive;
	Button btnStartPushing;
	Button btnStopPushing;

	List<DataProvider> providers;

	final int debug_level = 0;
	
	Runnable statusChecker = new Runnable() {
		@Override 
		public void run() {
			List locationData = getAvailableData();
			String jsonStringified = JSONValue.toJSONString(locationData);
			if (debug_level == 1) {
				Log.d(C.TAG, "JSON encoded data: " + jsonStringified.substring(0, 100) + (jsonStringified.length() > 100 ? "..." : ""));
			} else if (debug_level == 2){
				Log.d(C.TAG, "JSON encoded data: " + jsonStringified);
			}
			
			Networking.postData(C.SERVER + "push", jsonStringified);
			if (dataPushHandlerActive)
				dataPushHandler.postDelayed(this, C.pushIntervalMillis);
		}
	};
	
	private void startPushing() {
		dataPushHandlerActive = true;
		btnStopPushing.setEnabled(true);
		btnStartPushing.setEnabled(false);
		for (DataProvider p : providers) {
			p.onStartPushing();
		}
		statusChecker.run();

	}
	
	private void stopPushing() {
		dataPushHandlerActive = false;
		dataPushHandler.removeCallbacks(statusChecker);
		btnStopPushing.setEnabled(false);
		btnStartPushing.setEnabled(true);
		for (DataProvider p : providers) {
			p.onStopPushing();
		}
	}

	Toast previousToast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		
		setContentView(R.layout.activity_start_locating);
		final Context self = this;
		dataPushHandler = new Handler() {
            public void handleMessage(Message m) {
            	String error = m.getData().getString("error");
            	if (previousToast != null) previousToast.cancel();
                Toast toast = Toast.makeText(self, error, Toast.LENGTH_SHORT);
                toast.show();
                previousToast = toast;
            }
        };
		dataPushHandlerActive = false;
		ErrorReporting.initialize(dataPushHandler);

		// PROVIDERS
		providers = new LinkedList<DataProvider>();
		//providers.add(new SensorsMagic(this, Sensor.TYPE_LINEAR_ACCELERATION));
        //providers.add(new SensorsMagic(this, Sensor.TYPE_MAGNETIC_FIELD));
		providers.add(new SensorsMagic(this));
		providers.add(new WifiMagic(this));
		// END PROVIDERS
		
		btnStartPushing = (Button)findViewById(R.id.start_push);
		btnStopPushing = (Button)findViewById(R.id.stop_pushing);
		btnStopPushing.setEnabled(false);
		btnStartPushing.setEnabled(true);
		Button btnDwa = (Button)findViewById(R.id.dwabtn);
		Button btnWce = (Button)findViewById(R.id.wce);
		Button resetParticles = (Button)findViewById(R.id.reset_particles);
		btnDwa.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(self, WifiAnalyzer.class));

				
			}
		});
		
		resetParticles.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Networking.postData(C.SERVER+"reset", "hi");
				
			}
		});
		
		btnWce.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(self, WifiCorrectionExtractor.class));
				
			}
		});
		
		btnStartPushing.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startPushing();
			}
		});
		
		btnStopPushing.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				stopPushing();
			}
		});

		
    }

	// Adds data to target if data is not null and denotes it as name
	private void addData(List target, String name, Object data) {
		if (data != null) {
			Map dataContainer = new HashMap();
			dataContainer.put("name", name);
			dataContainer.put("data", data);
			target.add(dataContainer);
		}
	}

	protected List getAvailableData() {
		Long start = System.currentTimeMillis();

		List result = new LinkedList();

		for (DataProvider provider : providers) {
			addData(result, provider.getName(), provider.getData());
		}
		
		
		Log.d(C.TAG, "Data extraction complete in " + (System.currentTimeMillis() - start) + " ms");
		return result;
	}

	


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start_locating, menu);
		return true;
	}

}
