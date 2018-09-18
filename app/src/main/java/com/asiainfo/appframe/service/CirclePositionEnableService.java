package com.asiainfo.appframe.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class CirclePositionEnableService extends Service {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		
		startForeground(CirclePositionService.instance);
		startForeground(this);
		stopForeground(true);
		stopSelf();
		return super.onStartCommand(intent, flags, startId);
	}
	
	 private static void startForeground(Service service) {
	        @SuppressWarnings("deprecation")
			Notification notification = new Notification.Builder(service).getNotification();
	        service.startForeground(1000, notification);
	    }
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
