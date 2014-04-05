package com.wangdanqing.map_staff;


import java.io.IOException;
import java.util.List;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
//import com.google.android.gms.internal.r;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.wangdanqing.Map_Staff.R;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
//import android.app.Activity;
import android.app.Dialog;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v4.app.FragmentActivity;

public class MainActivity extends FragmentActivity  {

//GooglePlayServicesClient.ConnectionCallbacks,
//GooglePlayServicesClient.OnConnectionFailedListener{
	
	  
	private static final int GPS_ERRORDIALOG_REQUEST = 9001;
	GoogleMap mMap;
 
	
	@SuppressWarnings("unused")
			private static final double 
			SEATTLE_LAT = 47.60621,
			SEATTLE_LNG =-122.33207,
			SYDENY_LAT = -33.867487,
			SYDNEY_LNG = 151.20699,
			NEWYORK_LAT = 40.785555,
			NEWYORK_LNG = -73.988377;
	
	private static final float DEFAULTZOOM= 4;
	
	Marker marker;
	 
	//LocationClient mLocationClient;
	 
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if (servicesOK()) {
        	setContentView(R.layout.activity_map);
        	
        	if(initMap()){
        		Toast.makeText(this, "Ready to Map", Toast.LENGTH_SHORT).show();
        		gotoLocation(NEWYORK_LAT, NEWYORK_LNG, DEFAULTZOOM);
        		mMap.setMyLocationEnabled(true); 
        		//mLocationClient = new LocationClient(this,this, this);
        		//mLocationClient.connect();
        		
        	}
        	else{
        		Toast.makeText(this, "Map not available!", Toast.LENGTH_SHORT).show();
        	}
        }
        	else{
        		setContentView(R.layout.activity_map);
        	}
            	
    }   	
            	
   private void gotoLocation(double lat, double lng,
			float zoom) {	
	   LatLng ll = new LatLng(lat,lng);
	    	CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll,zoom);
	    	mMap.moveCamera(update);
		
	}

@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public boolean servicesOK(){
    	int isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
    	
    	if(isAvailable == ConnectionResult.SUCCESS){
    		return true;
    	}
    	else if(GooglePlayServicesUtil.isUserRecoverableError(isAvailable)){
    		Dialog dialog = GooglePlayServicesUtil.getErrorDialog(isAvailable, this, GPS_ERRORDIALOG_REQUEST);
    		dialog.show();
    	}
    	else{
    		Toast.makeText(this, "Can't connect to Google Play services", Toast.LENGTH_SHORT).show();
    		
    	}
    	return false;
    	
    }

    
    private boolean initMap(){
    	if(mMap == null){
    	  SupportMapFragment mapFrag =
    			  (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
    	  mMap= mapFrag.getMap();
    	}
    	return(mMap!=null);
    }
    
    private void gotoLocation(double lat, double lng){
    	
    	LatLng ll = new LatLng(lat,lng);
    	CameraUpdate update = CameraUpdateFactory.newLatLng(ll);
    	mMap.moveCamera(update);
    	
    }
    @SuppressWarnings("unused")
	public void geoLocate(View v) throws IOException {
		
		
		EditText et = (EditText) findViewById(R.id.editText1);
		String location = et.getText().toString();
		//if(location.length()==0){
		//	Toast.makeText(this, "Please enter a location", Toast.LENGTH_SHORT).show();
		//	return;
	//}
		
		
		hideSoftKeyboard(v);
		
		Geocoder gc = new Geocoder(this);
		List<Address> list= gc.getFromLocationName(location, 1);
		Address add = list.get(0);
		String locality = add.getLocality();
		Toast.makeText(this, locality, Toast.LENGTH_LONG).show();
		double lat = add.getLatitude();
		double lng = add.getLongitude();
	
		gotoLocation(lat, lng, DEFAULTZOOM);

		MarkerOptions options = new MarkerOptions()
		.title(locality)
		.position(new LatLng(lat,lng))
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.default_image));
		 mMap.addMarker(options);
	}
    
	
	private void icon(BitmapDescriptor defaultMarker) {
		
	}

	private void hideSoftKeyboard(View v) {
		InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}
	
	@Override
	
 protected void onStop(){
		super. onStop();
		MapStateManager mgr= new MapStateManager(this);
		mgr.saveMapState(mMap);
		
	}
	@Override
	protected void onResume(){
		super.onResume();
		MapStateManager mgr = new MapStateManager(this);
		CameraPosition position = mgr.getSavedCameraPosition();
		if ( position != null){
			CameraUpdate update = CameraUpdateFactory.newCameraPosition(position);
			mMap.moveCamera(update);
		}
	}
		
		//private void setMaker(String locality, double lat, double lng){

	//	MarkerOptions options = new MarkerOptions()
	//	.title(locality)
	//	.position(new LatLng(lat,lng))
	//	.icon(BitmapDescriptorFactory.defaultMarker(
	//			BitmapDescriptorFactory.HUE_ROSE));
		
	//	marker =  mMap.addMarker(options);
		
	//}

	

}
	
	//protected void gotoCurrentLocation(){
	//	Location currentLocation = mLocationClient.getLastLocation();
		//if(currentLocation ==null){
//			Toast.makeText(this,"Current location is not available", Toast.LENGTH_SHORT).show();
//		}
//		else{
//			LatLng ll = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
//			CameraUpdate update= CameraUpdateFactory.newLatLngZoom(ll, DEFAULTZOOM);
//			mMap.animateCamera(update);
//		}



