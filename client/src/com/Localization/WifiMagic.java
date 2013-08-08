package com.Localization;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

public class WifiMagic {
    WifiManager mainWifi;

    Map<String, String[]> macToName;
    
    public WifiMagic(Context c) {
    	 mainWifi = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
    	 macToName = new HashMap<String, String[]>();
    	 fillMacToName(); 
    }
    
    public List getWifi() {
		mainWifi.startScan();
		List jsonScanResults = new LinkedList();

		for(ScanResult scanResult : mainWifi.getScanResults()) {
			HashMap jsonScanResult = new HashMap();
			String [] name = macToName.get(scanResult.BSSID);
			// Not sure if this is the best way.
			if (name == null || !name[1].equals("1")) continue;
			jsonScanResult.put("label", name[0]);
			jsonScanResult.put("estimatedDistance", strengthToDistance(scanResult.level,scanResult.frequency));
			
			jsonScanResults.add(jsonScanResult);
		}

		return jsonScanResults;
	}
	
	private double strengthToDistance(double level, double freqGhz) {
		double freqHz = freqGhz * 1000000.0;
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
	
	private void fillMacToName() {
	    macToName.put("00:0b:86:74:95:98", new String [] {"AP-1-1", "0" });
	    macToName.put("00:0b:86:74:95:90", new String [] {"AP-1-1", "1"});
	    macToName.put("00:0b:86:74:90:98", new String [] {"AP-1-2", "0"});
	    macToName.put("00:0b:86:74:90:98", new String [] {"AP-1-2", "0"});
	    macToName.put("00:0b:86:74:97:f8", new String [] {"AP-1-3", "0"});
	    macToName.put("00:0b:86:74:97:f0", new String [] {"AP-1-3", "1"});
	    macToName.put("00:0b:86:74:90:88", new String [] {"AP-1-4", "0"});
	    macToName.put("00:0b:86:74:90:80", new String [] {"AP-1-4", "1"});
	    macToName.put("00:0b:86:74:8d:98", new String [] {"AP-1-5", "0"});
	    macToName.put("00:0b:86:74:8d:90", new String [] {"AP-1-5", "1"});
	    macToName.put("00:0b:86:74:90:68", new String [] {"AP-1-6", "0"});
	    macToName.put("00:0b:86:74:90:60", new String [] {"AP-1-6", "1"});
	    macToName.put("00:0b:86:74:98:38", new String [] {"AP-1-7", "0"});
	    macToName.put("00:0b:86:74:98:30", new String [] {"AP-1-7", "1"});
	    macToName.put("00:0b:86:74:8f:48", new String [] {"AP-1-8", "0"});
	    macToName.put("00:0b:86:74:8f:40", new String [] {"AP-1-8", "1"});
	    macToName.put("00:0b:86:74:97:28", new String [] {"AP-1-9", "0"});
	    macToName.put("00:0b:86:74:97:20", new String [] {"AP-1-9", "1"});
	    macToName.put("00:0b:86:74:8f:98", new String [] {"AP-2-1", "0"});
	    macToName.put("00:0b:86:74:8f:90", new String [] {"AP-2-1", "1"});
	    macToName.put("00:0b:86:74:97:e8", new String [] {"AP-2-11", "0"});
	    macToName.put("00:0b:86:74:97:e0", new String [] {"AP-2-11", "1"});
	    macToName.put("00:0b:86:74:8f:78", new String [] {"AP-2-2", "0"});
	    macToName.put("00:0b:86:74:8f:70", new String [] {"AP-2-2", "1"});
	    macToName.put("00:0b:86:74:99:e8", new String [] {"AP-2-3", "0"});
	    macToName.put("00:0b:86:74:99:e0", new String [] {"AP-2-3", "1"});
	    macToName.put("00:0b:86:74:97:88", new String [] {"AP-2-4", "0"});
	    macToName.put("00:0b:86:74:97:80", new String [] {"AP-2-4", "1"});
	    macToName.put("00:0b:86:74:90:08", new String [] {"AP-2-5", "0"});
	    macToName.put("00:0b:86:74:90:00", new String [] {"AP-2-5", "1"});
    }
}
	       