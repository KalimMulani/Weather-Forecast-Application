package com.example.weatherforcast;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.awt.font.TextAttribute;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    final String API_Id = "25222603858a9a3d1757d3b14dd8233c";
    final String Weather_URl = "https://api.openweathermap.org/data/2.5/weather";

    final long MIN_Time = 5000;
    final float MIN_Distance = 1000;
    int request_code = 101;


    String location_provider = LocationManager.GPS_PROVIDER;
    TextView NameofCity, weatherState, Temperature;
    ImageView weatherIcon;
    RelativeLayout CityFinder;
    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weatherState = findViewById(R.id.weatherCondition);
        Temperature = findViewById(R.id.temperature);
        weatherIcon = findViewById(R.id.weatherIcon);
        CityFinder = findViewById(R.id.cityFinder);
        NameofCity = findViewById(R.id.cityName);

        CityFinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CityFinder.class);
                startActivity(intent);
            }
        });
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        getWeatherForCurrentLocation();
//    }


    @Override
    protected void onResume() {
        super.onResume();
        Intent intent=getIntent();
        String city =intent.getStringExtra("City");
        if(city!=null){
            getWeatherforNewCity(city);

        }else{
            getWeatherForCurrentLocation();
        }
    }

    private void getWeatherforNewCity(String city){
        RequestParams params =new RequestParams();
        params.put("q",city);
        params.put("appid",API_Id);
        letsdoSomeNetworking(params);
    }

    private void getWeatherForCurrentLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                String Latitude = String.valueOf(location.getLatitude());
                String Longitude = String.valueOf(location.getLongitude());

                RequestParams params = new RequestParams();
                params.put("lat",Latitude);
                params.put("lon",Longitude);
                params.put("appid",API_Id);
                letsdoSomeNetworking(params);
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},request_code);
            return;
        }
        locationManager.requestLocationUpdates(location_provider, MIN_Time, MIN_Distance, locationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==request_code){
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Location got Successffully.", Toast.LENGTH_SHORT).show();
                getWeatherForCurrentLocation();
            }
            else {

            }
        }
    }

    private void letsdoSomeNetworking(RequestParams params){
        AsyncHttpClient client=new AsyncHttpClient();
        client.get(Weather_URl,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                super.onSuccess(statusCode, headers, response);
                Toast.makeText(MainActivity.this,"Data get Success",Toast.LENGTH_SHORT).show();
                Weather_Data wd =Weather_Data.fromJson(response);
                updateUI(wd);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

    }
    private void updateUI(Weather_Data wd){
        Temperature.setText(wd.getTemp());
        NameofCity.setText(wd.getCity());
        weatherState.setText(wd.getWeather_Type());
        int resourceID=getResources().getIdentifier(wd.getIcon(),"drawable",getPackageName());
        weatherIcon.setImageResource(resourceID);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(locationManager!=null){
            locationManager.removeUpdates(locationListener);
        }
    }
}