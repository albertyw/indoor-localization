package com.Localization;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import org.json.simple.JSONValue;

public class StartLocating extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_locating);
        
        Button b = (Button)findViewById(R.id.ping_server);
        ErrorReporting.initialize(this);
        
        b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// BEGIN ugly Java JSON example
				List data = new LinkedList();
		        Map wifi = new HashMap();
		        wifi.put("name","wifi");
		        wifi.put("data", "this_is_what_i_have");
		        data.add(wifi);
		        
		        String jsonStringified = JSONValue.toJSONString(data);
		        // END ugly Java JSON example
		        
		        Log.d(C.TAG, "JSON encoded data: " + jsonStringified);
				Networking.postData(C.SERVER + "push", jsonStringified);
			}
		});
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start_locating, menu);
        return true;
    }

}
