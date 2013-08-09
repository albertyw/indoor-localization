package com.Localization;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SensorsMagic extends DataProvider {
    String sensorName;
    SensorManager sensorManager;
	Sensor sensor;
	List sensorReadings;
	boolean isPushing;

	public SensorsMagic(Context c, int sensorType) {
        if(sensorType == Sensor.TYPE_LINEAR_ACCELERATION)
            sensorName = "Linear Accelerometer";
        if(sensorType == Sensor.TYPE_MAGNETIC_FIELD)
            sensorName = "Magnetometer";
		isPushing = false;
        // Sensors
        sensorManager = (SensorManager) c.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(sensorType);
        sensorReadings = new LinkedList();
        sensorManager.registerListener(new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if(!isPushing) return;
                HashMap reading = new HashMap();
                reading.put("x", event.values[0]);
                reading.put("y", event.values[1]);
                reading.put("z", event.values[2]);
                sensorReadings.add(reading);
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy){}
        }, sensor, SensorManager.SENSOR_DELAY_FASTEST);
	}
	
	@Override
	public String getName() {
        return sensorName;
	}

	@Override
	public Object getData() {
        List jsonScanResults = new LinkedList(sensorReadings);
        sensorReadings.clear();
        return jsonScanResults;
	}
	
	@Override
	public void onStartPushing() {
		super.onStartPushing();
		sensorReadings.clear();
		isPushing = true;
	}
	
	@Override
	public void onStopPushing() {
		super.onStopPushing();
		isPushing = false;
		
	}

}
