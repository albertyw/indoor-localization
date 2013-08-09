package com.Localization;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.MacSpi;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.Handler;
import android.util.Log;

public class WifiMagic extends DataProvider {
	WifiManager mainWifi;

	static Handler h;

	public WifiMagic(Context c) {
		mainWifi = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
		h = new Handler();
		macToFreqMhz = new HashMap<String,Integer>();
		macToLevel = new HashMap<String, Double>();
		notSeenForXManyReadings = new HashMap<String, Integer>();
	}

	Map<String, Double> macToLevel;
	Map<String, Integer> macToFreqMhz;
	Map<String, Integer> notSeenForXManyReadings;

	private static final double remembering = 0.5;
	private static final int updateLevelsEveryMillis = 200;
	private static final int removeMacAfterNotSeenForMillis = 400;
	private static final int HOW_MANY_NOT_SEEN_BEFORE_REMOVE = (removeMacAfterNotSeenForMillis / updateLevelsEveryMillis);

	Runnable updateReadings = new Runnable() {
		@Override
		public void run() {
			mainWifi.startScan();
			
			List<String> macsSeen = new LinkedList<String>();

			for(ScanResult scanResult : mainWifi.getScanResults()) {
				if (!macToLevel.containsKey(scanResult.BSSID))
					macToLevel.put(scanResult.BSSID, (double)scanResult.level);
				double pastLevel = macToLevel.get(scanResult.BSSID);
				macToLevel.put(scanResult.BSSID, pastLevel*remembering + (1.0 - remembering) * scanResult.level);
				if (!macToFreqMhz.containsKey(scanResult.BSSID))
					macToFreqMhz.put(scanResult.BSSID, scanResult.frequency);	        	
				macsSeen.add(scanResult.BSSID);
			}
			
			List<String> macsToRemove = new LinkedList<String>();
			for (String mac : macToLevel.keySet()) {
				if (macsSeen.contains(mac)) {
					if (notSeenForXManyReadings.containsKey(mac)) {
						notSeenForXManyReadings.remove(mac);
					}
				} else {
					if (!notSeenForXManyReadings.containsKey(mac)) {
						notSeenForXManyReadings.put(mac, 1);
					} else {
						int times = notSeenForXManyReadings.get(mac);
						++times;
						notSeenForXManyReadings.put(mac, times);
						if(times == HOW_MANY_NOT_SEEN_BEFORE_REMOVE) {
							macsToRemove.add(mac);
						}
					}
				}
			}
			
			for (String mac : macsToRemove) {
				macToLevel.remove(mac);
				notSeenForXManyReadings.remove(mac);
			}
			
			h.postDelayed(this, updateLevelsEveryMillis);
		}
	};

	private WifiLock wifiLock;

	public void onStartPushing() {
		if (!canScanWifi()) return;
		assert wifiLock == null;
		wifiLock = mainWifi.createWifiLock("wifi_magic");
		wifiLock.acquire();
		updateReadings.run();
	}

	public void onStopPushing() {
		if(!canScanWifi()) return;
		assert wifiLock.isHeld();
		wifiLock.release();
		wifiLock = null;
		h.removeCallbacks(updateReadings);
		macToLevel.clear();
		notSeenForXManyReadings.clear();
	}

	public String getName() {
		return "wifi";
	}

	public boolean canScanWifi() {
		boolean result = mainWifi.isWifiEnabled();
		/// result = result || mainWifi.isScanAlwaysAvailable();
		return result;
	}

	public List getData() {
		if (!canScanWifi()) {
			ErrorReporting.maybeReportError("wifi not available");
			return null;
		}
		mainWifi.startScan();
		List jsonScanResults = new LinkedList();

		for(String mac : macToLevel.keySet()) {
			HashMap jsonScanResult = new HashMap();
			String [] name = WifiNames.getMacToName().get(mac);
			// Not sure if this is the best way.
			if (name == null || !name[1].equals("1")) continue;
			jsonScanResult.put("label", name[0]);
			jsonScanResult.put("level", macToLevel.get(mac) );
			jsonScanResult.put("freqMhz", macToFreqMhz.get(mac));

			jsonScanResults.add(jsonScanResult);
		}

		return jsonScanResults;
	}

	private double strengthToDistance(double level, double freqMhz) {
		double freqHz = freqMhz * 1000000.0;
		final double a = -0.07363796;
		final double b = -2.52218124;

		//final double a = -0.07192023;
		//final double b = -2.40415772;

		final double C = 299792458.0;
		final double ROUTER_HEIGHT = 2.5;

		double n = Math.max(2, a*level +b);
		level = -level;
		double wavelength = C/freqHz;
		double FSPL = 20.0 * Math.log10(4.0 * Math.PI / wavelength);
		double directDistanceM = Math.pow(10, (level - FSPL)/(10.0 * n));

		directDistanceM = Math.max(directDistanceM, ROUTER_HEIGHT);

		double distancePlaneM = Math.sqrt(Math.pow(directDistanceM,2) - Math.pow(ROUTER_HEIGHT, 2));
		return distancePlaneM;
	}


}
