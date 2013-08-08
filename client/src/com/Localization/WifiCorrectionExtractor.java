package com.Localization;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONValue;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class WifiCorrectionExtractor extends Activity{

	Map<String, Double> macToLevel;
	TextView text;

	List<String> macs;

	WifiManager mainWifi;
	static Handler h;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wce);

		mainWifi=  (WifiManager) getSystemService(Context.WIFI_SERVICE);

		h = new Handler();
		
		macToLevel = new HashMap<String, Double>();

		final double factor = 0.90;

		macs = new LinkedList<String>();

		console = new LinkedList<String>();
		
		Runnable r = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mainWifi.startScan();


				for(ScanResult scanResult : mainWifi.getScanResults()) {
					if (!macToLevel.containsKey(scanResult.BSSID)) {
						macToLevel.put(scanResult.BSSID, (double)scanResult.level);
					} else {
						double prev = macToLevel.get(scanResult.BSSID);
						macToLevel.put(scanResult.BSSID, factor * prev + (1.0-factor) * (double)scanResult.level);
					}

					if (!macs.contains(scanResult.BSSID)) {
						macs.add(scanResult.BSSID);
					}

				}

				updateResults();


				h.postDelayed(this, 200);
			}
		};

		
		r.run();

	}
	
	final int maxLines = 10;
	List<String> console;
	
	private void printLine(String line) {
		console.add(line);
		int start = Math.max(0, console.size()-maxLines);
		String result = "";
		for(int i=start; i<console.size(); ++i) {
			result += console.get(i) +"\n";
		}
		
		TextView con = (TextView)findViewById(R.id.extractor_text);
		
		con.setText(result);
	}

	private double sumLevel = 0;
	private int noScans;
	private final int scansRequired = 100;
	
	DecimalFormat f = new DecimalFormat("###.00");

	
	public void scan(final String mac, final String name) {
		printLine("Scanning " + name + " (mac: " + mac + ")");
		printLine("Please keep your phone at your pocket level");
		sumLevel = 0.0;
		noScans = 0;
		Runnable scan = new Runnable() {
			
			@Override
			public void run() {
				mainWifi.startScan();
				double result = 0.0;
				for(ScanResult scanResult : mainWifi.getScanResults()) {
					if (scanResult.BSSID.equals(mac)) {
						++noScans;
						sumLevel += scanResult.level;
						result = sumLevel/noScans;
						printLine("" + noScans + "/" + scansRequired + " scans complete (current result: " + f.format(result) );
					}
				}
				if (noScans == scansRequired) {
					printLine("Extraction complete.");
					printLine("Posting (" + name + "," + f.format(result) +") to the server");
					
					Map<String, Object> data = new HashMap<String, Object>();
					
					data.put("name", name);
					data.put("base_level", result);
					
					String jsonStringified = JSONValue.toJSONString(data);
					
					
					Networking.postData(C.SERVER + "update_base_level", jsonStringified);
				} else {
					h.postDelayed(this, 50);
				}
			}
		};
		
		scan.run();
	}





	public void updateResults() {
		double best = -10000.0;
		String bestone = null;
		String bestname = null;
		for (String mac : macs) {
			  String [] name = WifiNames.getMacToName().get(mac);
	            if (name != null && name[1].equals("1")) {
					if (macToLevel.get(mac)>best) {
						best = macToLevel.get(mac);
						bestname = name[0];
						bestone = mac;
					}
	            }
		}
		Button btn = (Button)findViewById(R.id.start_extraction);
		
		if (bestone == null) {
			btn.setText("unavailable");
			btn.setEnabled(false);
		} else {
			btn.setEnabled(true);
			btn.setText("Scan " + bestname);
			final String winner = bestone;
			final String winnerName = bestname;
			btn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					scan(winner,winnerName);
					
				}
			});
		}
		
	}




}
