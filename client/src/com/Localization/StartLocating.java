package com.Localization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import org.json.simple.JSONValue;

// See http://www.androidsnippets.com/scan-for-wireless-networks

public class StartLocating extends Activity {
    WifiManager mainWifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_locating);
        mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        Button b = (Button)findViewById(R.id.ping_server);
        ErrorReporting.initialize(this);
        
        b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				List locationData = getAvailableData();
                String jsonStringified = JSONValue.toJSONString(locationData);
		        
		        Log.d(C.TAG, "JSON encoded data: " + jsonStringified);
				Networking.postData(C.SERVER + "push", jsonStringified);
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
    	
    	// Wifi
    	addData(result, "wifi", getWifi());
    	
    	Log.d(C.TAG, "Data extraction complete in " + (System.currentTimeMillis() - start) + " ms");
    	return result;
    }

    protected List getWifi() {
    	mainWifi.startScan();
    	List jsonScanResults = new LinkedList();
        
        for(ScanResult scanResult : mainWifi.getScanResults()) {
            HashMap jsonScanResult = new HashMap();
            jsonScanResult.put("bssid", scanResult.BSSID);
            jsonScanResult.put("level", scanResult.level);
            jsonScanResult.put("frequency", scanResult.frequency);
            jsonScanResults.add(jsonScanResult);
        }
        
        return jsonScanResults;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start_locating, menu);
        return true;
    }

}
