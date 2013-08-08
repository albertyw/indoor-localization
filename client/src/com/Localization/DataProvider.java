package com.Localization;

public abstract class DataProvider {
	public abstract String getName();
	public abstract Object getData();
	public void onStartPushing() {}
	public void onStopPushing() {}
}
