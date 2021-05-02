package com.example.barcodescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    public static final String ALLERGENS = "com.example.barcodescanner.MESSAGE";
    //  public static final String INGREDIENTS = "com.example.barcodescanner.MESSAGE";
    String jsonData;
    String singleParsed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CheckBox gluten = findViewById(R.id.cbgluten);
        CheckBox peanuts = findViewById(R.id.cbpeanuts);
        CheckBox treeNuts = findViewById(R.id.cbtreenuts);
        CheckBox eggs = findViewById(R.id.cbeggs);
        CheckBox milk = findViewById(R.id.cbmilk);
        CheckBox soy = findViewById(R.id.cbsoy);
        CheckBox sesame = findViewById(R.id.cbsesame);

        ImageButton camera = findViewById(R.id.btnCamera);
        camera.setOnClickListener(view -> {
            boolean glutenBool = gluten.isChecked();
            boolean peanutsBool = peanuts.isChecked();
            boolean treeNutsBool = treeNuts.isChecked();
            boolean eggsBool = eggs.isChecked();
            boolean milkBool = milk.isChecked();
            boolean soyBool = soy.isChecked();
            boolean sesameBool = sesame.isChecked();

            String selectedAllergens = "";
            if (glutenBool) {
                selectedAllergens = selectedAllergens + "\nGluten";
            }
            if (peanutsBool) {
                selectedAllergens = selectedAllergens + "\nPeanuts";
            }
            if (treeNutsBool) {
                selectedAllergens = selectedAllergens + "\nTree Nuts";
            }
            if (eggsBool) {
                selectedAllergens = selectedAllergens + "\nEggs";
            }
            if (milkBool) {
                selectedAllergens = selectedAllergens + "\nMilk";
            }
            if (soyBool) {
                selectedAllergens = selectedAllergens + "\nSoy";
            }
            if (sesameBool) {
                selectedAllergens = selectedAllergens + "\nSesame";
            }

            String allergensSelected = "Scanning for: \n" + selectedAllergens;
            Intent intent = new Intent(view.getContext(), Camera.class);
            intent.putExtra(ALLERGENS, allergensSelected);
            startActivity(intent);
        });
    }
}