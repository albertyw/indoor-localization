package com.Localization;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Path.FillType;

public class WifiNames {
	private static Map<String,String[]> macToName;

	public static Map<String, String[]> getMacToName() {
		if (macToName == null) {
			macToName = new HashMap<String,String[]>();
			fillMacToName();
		}
		return macToName;
	}
	
	private static void fillMacToName() {
	    macToName.put("00:0b:86:74:95:98", new String [] {"AP-1-1", "0" });
	    macToName.put("00:0b:86:74:95:90", new String [] {"AP-1-1", "1"});
	    macToName.put("00:0b:86:74:90:98", new String [] {"AP-1-2", "0"});
	    macToName.put("00:0b:86:74:90:98", new String [] {"AP-1-2", "1"});
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
