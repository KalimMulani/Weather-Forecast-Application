package com.example.weatherforcast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class CityFinder extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_finder);
        final EditText edittext=findViewById(R.id.searchCity);
        ImageView iv=findViewById(R.id.backButton);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        edittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String newCity=edittext.getText().toString();
                Intent in =new Intent(CityFinder.this,MainActivity.class);
                in.putExtra("City",newCity);
                startActivity(in);
                return false;
            }
        });
    }
}