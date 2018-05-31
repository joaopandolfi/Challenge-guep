package dersus.challenge_guep;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import dersus.challenge_guep.constants.BundleConstants;
import dersus.challenge_guep.constants.PermissionsConstants;
import dersus.challenge_guep.controller.MapController;
import dersus.challenge_guep.controller.PermissionsController;
import dersus.challenge_guep.model.persistence.DAO.GasStationDao;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener  {

    private GoogleMap mMap;
    private MapController mapController;
    private PermissionsController permissionsController;
    private LocationManager locationManager;

    private Location currentLocation;
    private Dialog dialogWithOutLocation = null;
    private boolean uiUpdated = false;

    private GoogleApiClient mGoogleApiClient;
    private LocationManager mLocationManager;


    public static final int LOCATION_UPDATE_MIN_DISTANCE = 10;
    public static final int LOCATION_UPDATE_MIN_TIME = 5000;

    private boolean mLocationPermissionGranted = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        initializeMap();


        //initializePermissions();


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        mGoogleApiClient.connect();

    }


    @Override
    public void onConnected(Bundle connectionHint) {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                updateUI();
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                currentLocation = null;
                initializePermissions();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (mLocationPermissionGranted) {
            currentLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);
            updateUI();
        }
    }



    private void onCreateWithPermission() {
        initializeLocation();
    }

    private void initializePermissions() {
        permissionsController = new PermissionsController();
        if (permissionsController.checkPermissions(this, PermissionsConstants.MAP_PERMISSIONS))
            mLocationPermissionGranted = true;
    }


    private void initializeMap() {

        int googlePlayStatus = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (googlePlayStatus != ConnectionResult.SUCCESS) {
            GooglePlayServicesUtil.getErrorDialog(googlePlayStatus, this, -1).show();
            finish();
        } else {
            if (mMap != null) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                mMap.getUiSettings().setAllGesturesEnabled(true);
            }
        }
    }


    private void initializeController() {
        mapController = new MapController(mMap);
    }

    private void updateUI(){
        Log.v("LOG","UI UPDATED");
        if(!uiUpdated && currentLocation != null) {
            Log.v("LOG","ENTROU");
            LatLng currLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            mapController.moveMap(currLatLng);
            mapController.drawCircle(currLatLng);
            mapController.addGasStations(currLatLng, GasStationDao.getInstance(this).query());
            uiUpdated = true;
        }
    }


    //GPS
    private void initializeLocation(){
        try{
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            // getting GPS status
            Boolean isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            Boolean isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                showSettingsAlert();
            }
        }catch (Exception e){}
    }



    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     * */
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle(R.string.location_title);

        // Setting Dialog Message
        alertDialog.setMessage(R.string.location_message);

        // On pressing Settings button
        alertDialog.setPositiveButton(R.string.location_config, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton(R.string.location_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        dialogWithOutLocation = alertDialog.create();
        // Showing Alert Message
        //alertDialog.show();
        try{
            dialogWithOutLocation.show();
        } catch (Exception e){
            Log.v("Error", "Erro no dialog");
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode == BundleConstants.RESPONSE_INTENT_PERMISSIONS) {
            boolean flag = true;
            for (int i = 0; i < permissions.length; i++)
                if (permissions[i].equalsIgnoreCase(Manifest.permission.READ_EXTERNAL_STORAGE) && grantResults[i] != PackageManager.PERMISSION_GRANTED)
                    flag = false;

            if(flag)
                onCreateWithPermission();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        updateUI();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        initializeMap();
        initializeLocation();
        initializeController();
        updateLocationUI();
        getDeviceLocation();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
