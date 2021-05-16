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
    public static final String ALLERGENS = "com.example.barcodescanner.MESSAGE1";

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
                selectedAllergens = selectedAllergens + "Gluten\n";
            }
            if (peanutsBool) {
                selectedAllergens = selectedAllergens + "Peanuts\n";
            }
            if (treeNutsBool) {
                selectedAllergens = selectedAllergens + "Tree Nuts\n";
            }
            if (eggsBool) {
                selectedAllergens = selectedAllergens + "Eggs\n";
            }
            if (milkBool) {
                selectedAllergens = selectedAllergens + "Milk\n";
            }
            if (soyBool) {
                selectedAllergens = selectedAllergens + "Soy\n";
            }
            if (sesameBool) {
                selectedAllergens = selectedAllergens + "Sesame\n";
            }

            Intent intent = new Intent(view.getContext(), Camera.class);
            intent.putExtra(ALLERGENS, selectedAllergens);
            startActivity(intent);
        });
    }
}