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
                List data = scan();
                String jsonStringified = JSONValue.toJSONString(data);
		        
		        Log.d(C.TAG, "JSON encoded data: " + jsonStringified);
				Networking.postData(C.SERVER + "push", jsonStringified);
			}
		});
        
    }

    protected List scan() {
        mainWifi.startScan();
        List<ScanResult> wifiList = mainWifi.getScanResults();
        List<HashMap> data = new LinkedList<HashMap>();
        for(int i = 0; i < wifiList.size(); i++){
            HashMap datum = new HashMap();
            datum.put("name", wifiList.get(i).BSSID);
            datum.put("bssid", wifiList.get(i).BSSID);
            datum.put("level", wifiList.get(i).level);
            data.add(datum);
        }
        return data;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start_locating, menu);
        return true;
    }

}
