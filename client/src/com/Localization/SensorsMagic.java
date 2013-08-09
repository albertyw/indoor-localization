package com.Localization;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class SensorsMagic extends DataProvider {
	/*
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
	 */


	SensorManager sensorManager;
	
	Double [] readings;
	int pointer;
	
	boolean isPushing =false;
	
	public SensorsMagic(Context c) {
		sensorManager = (SensorManager) c.getSystemService(Context.SENSOR_SERVICE);
		Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
		final SensorsMagic self = this;
		isPushing = false;
		sensorManager.registerListener(new SensorEventListener() {
			@Override
			public void onSensorChanged(SensorEvent event) {
					if(!isPushing) return;
					double deg = ((2.0*Math.asin(event.values[2])/(2*Math.PI)*360) + 180.0 );
					sofar++;
					readings[pointer++] = deg;
					if (pointer == SAMPLES) {
						pointer = 0;
					}
				
			}
			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy){}
		}, sensor, SensorManager.SENSOR_DELAY_FASTEST);
	}
	@Override
	public String getName() {
		return "sensors";
	}

	@Override
	public  Object getData() {
		Map<String,Double> jsonScanResults = new HashMap<String, Double>();
		
		jsonScanResults.put("dheading", consume());
		
		return jsonScanResults;
	}
	
	double lastOne = -1.0;
	
	double get_difference(double a1, double a2) {
		double diff = a2-a1;
		double r = 360.0;
		for(int i=-1; i<=1; ++i) {
			r = Math.min(Math.abs(diff + 360.0*i),r);
		}
		return r;
		
		
	}
	
	double consume() {
		if (sofar < SAMPLES) return 0.0;
		double sum = 0;
		for (Double d : readings) {
			sum += d;
		}
		double r1 = sum/SAMPLES;
		double r2 = sum/SAMPLES -180.0;
		if (r2<0) r2+=360.0;
		
		double v1=0.0,v2=0.0;
		for (double d :readings) {
			v1+= get_difference(r1,d);
			v2+= get_difference(r2,d);
		}
		double r = (v1<v2) ? r1 : r2;
		double ret = 0.0;
		if (lastOne != -1.0) ret = r - lastOne;
		lastOne = r;
		// Log.d(C.TAG, "Change: "+ ret);
		return ret;
	}
	
	
	final int SAMPLES = 20;
	int sofar = 0;
	@Override
	public  void onStartPushing() {
		readings = new Double [SAMPLES];
		pointer = 0;
		sofar = 0;
		lastOne = -1.0;
		isPushing = true;
	}
	@Override
	public  void onStopPushing() {
		
		isPushing = false;
	}

}
