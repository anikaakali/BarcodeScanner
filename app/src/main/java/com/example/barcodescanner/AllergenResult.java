package com.example.barcodescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AllergenResult extends AppCompatActivity {
    private final String openFoodURL = "https://world.openfoodfacts.org/api/v0/product/";
    private String upc = "";
    private String upcURL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allergen_result);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent toResult = getIntent();
        upc = toResult.getStringExtra(Camera.UPC);
        upcURL = openFoodURL + upc + ".json";

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            // Background work here
            URL url;
            String resultFromFoodFacts = "";
            try {
                url = new URL(upcURL);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
                String stringBuffer;
                while ((stringBuffer = bufferedReader.readLine()) != null) {
                    resultFromFoodFacts = String.format("%s%s", resultFromFoodFacts, stringBuffer);
                }
                bufferedReader.close();

                JSONObject jsonObject = new JSONObject(resultFromFoodFacts);
                String ingredientsText = jsonObject.getJSONObject("product").getString("ingredients_text");

                //JSONObject jsonObject = (JSONObject) new JSONParser().parse(resultFromFoodFacts);
                //ArrayList<String> ingredientList = unpackFoodIngredientsJSON(result);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                resultFromFoodFacts = e.toString();
            }
            handler.post(() -> {
                // UI Thread work here
            });
        });
    }

    /*
    ArrayList<String> unpackFoodIngredientsJSON(String result) throws IOException, ParseException {
        // parsing input string result
        // Object obj = new JSONParser().parse(new StringReader(result));

        // typecasting obj to JSONObject
        // JSONObject jo = (JSONObject) obj;
        JSONParser jsonParser = new JSONParser();
        StringReader reader = new StringReader(result);
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) jsonParser.parse(reader);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String ingredients = (String) jsonObject.get("allergens_from_ingredients");
        return null;
    }

     */
}
