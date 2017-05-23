package org.app.alice.service;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class VerificarConexionService {
	private Context context;

	public VerificarConexionService(Context context) {
		super();
		this.context = context;
	}

	public VerificarConexionService() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Boolean estaConectado(){
		if(conectadoWifi()){
			return true;
		}else{
			if(conectadoRedMovil()){
				return true;
			}else{
				return false;
			}
		}
	}
	protected Boolean conectadoWifi(){
		
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (info != null) {
				if (info.isConnected()) {
					return true;
				}
			}
		}
		return false;
	}
	 
	protected Boolean conectadoRedMovil(){
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (info != null) {
				if (info.isConnected()) {
					return true;
				}
			}
		}
		return false;
	}
}
