package com.Localization;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class CopyOfSensorsMagic extends DataProvider {

	SensorManager sensorManager;
	Sensor accSensor;
	List accSensorReadings;
	boolean isPushing;
	
	public CopyOfSensorsMagic(Context c) {
		isPushing = false;
        // Sensors
        sensorManager = (SensorManager) c.getSystemService(Context.SENSOR_SERVICE);
        accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        accSensorReadings = new LinkedList();
        sensorManager.registerListener(new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if(!isPushing) return;
                HashMap reading = new HashMap();
                reading.put("x", event.values[0]);
                reading.put("y", event.values[1]);
                reading.put("z", event.values[2]);
                accSensorReadings.add(reading);
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy){}
        }, accSensor, SensorManager.SENSOR_DELAY_FASTEST);
	}
	
	@Override
	public String getName() {
		return "sensors";
	}

	@Override
	public Object getData() {
        List jsonScanResults = new LinkedList(accSensorReadings);
        accSensorReadings.clear();
        return jsonScanResults;
	}
	
	@Override
	public void onStartPushing() {
		super.onStartPushing();
		accSensorReadings.clear();
		isPushing = true;
	}
	
	@Override
	public void onStopPushing() {
		super.onStopPushing();
		isPushing = false;
		
	}

}
