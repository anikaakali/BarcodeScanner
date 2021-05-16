package com.example.barcodescanner;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class AllergenResult extends AppCompatActivity {
    private final String openFoodURL = "https://world.openfoodfacts.org/api/v0/product/";
    // Initialize Arrays for allergen ingredients.
    private final String[] glutenIngredients = {"barley", "wheat", "kamut", "bulgur", "durum", "malt", "rye", "oat", "atta", "einkorn", "emmer", "farina", "faro", "matzo", "brewer's yeast"};
    private final String[] peanutIngredients = {"peanut", "arachic oil", "arachis"};
    private final String[] treeNutIngredients = {"hazelnut", "almond", "brazil nut", "cashew", "pecan", "pistachio", "walnut"};
    private final String[] eggIngredients = {"egg", "albumin", "globulin", "lecithin", "lysozyme", "ovalbumin", "ovovitellin"};
    private final String[] milkIngredients = {"buttermilk", "milk", "casein", "caseinate", "whey"};
    private final String[] soyIngredients = {"soybean", "soy", "edamame", "miso", "nato", "tamari", "tempeh", "tvp", "tofu"};
    private final String[] sesameIngredients = {"sesame"};
    private final String GLUTEN = "Gluten";
    private final String PEANUTS = "Peanuts";
    private final String TREENUTS = "Tree Nuts";
    private final String EGGS = "Eggs";
    private final String MILK = "Milk";
    private final String SOY = "Soy";
    private final String SESAME = "Sesame";

    private String upc = "";
    private String upcURL = "";
    private String allergens = "";

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
        allergens = toResult.getStringExtra(Camera.ALLERGENSFORWARDED);
        String[] listOfAllergens = allergens.split("\n");
        AtomicReference<String> badIngredients = new AtomicReference<>("");
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
                HashMap<String, ArrayList<String>> allergenIngredientsMap = new HashMap<String, ArrayList<String>>();
                for (int i = 0; i < listOfAllergens.length; i++) {
                    ArrayList<String> allergenFound = checkForAllergen(listOfAllergens[i], ingredientsText);
                    if (allergenFound.size() != 0) {
                        allergenIngredientsMap.put(listOfAllergens[i], allergenFound);
                    }
                }
                badIngredients.set(resultText(allergenIngredientsMap));
                System.out.printf("list of badIngredients = %s\n", badIngredients.get());
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                resultFromFoodFacts = e.toString();
            }
            handler.post(() -> {
                // UI Thread work here
                TextView tvAllergens = findViewById(R.id.safeOrNot);
                tvAllergens.setText(badIngredients.get());
            });
        });
    }

    public ArrayList<String> checkForAllergen(String allergen, String ingredientsText) {
        // ArrayList to contain list of ingredients that contain allergen.
        ArrayList allergenFound = new ArrayList<String>();

        // String array of list of allergens for the given allergy.
        String[] allergenIngredients = null;

        // Change case of ingredients text to lowercase. -- TODO: immediately lowercase, no need to do every time
        ingredientsText = ingredientsText.toLowerCase();

        // Method for scanning through ingredients for allergen
        // TODO: Consider using switch.
        if (allergen.equals(GLUTEN)) {
            allergenIngredients = glutenIngredients;
        } else if (allergen.equals(PEANUTS)) {
            allergenIngredients = peanutIngredients;
        } else if (allergen.equals(TREENUTS)) {
            allergenIngredients = treeNutIngredients;
        } else if (allergen.equals(EGGS)) {
            allergenIngredients = eggIngredients;
        } else if (allergen.equals(MILK)) {
            allergenIngredients = milkIngredients;
        } else if (allergen.equals(SOY)) {
            allergenIngredients = soyIngredients;
        } else if (allergen.equals(SESAME)) {
            allergenIngredients = sesameIngredients;
        }

        for (int i = 0; i < allergenIngredients.length; i++) {
            if (ingredientsText.contains(allergenIngredients[i])) {
                allergenFound.add(allergenIngredients[i]);
            }
        }

        return allergenFound;
    }

    private String resultText(HashMap<String, ArrayList<String>> allergenIngredientsMap) {
        if (allergenIngredientsMap.size() == 0) {
            return "This food item is safe to eat!";
        }

        String outStr = "This food item is NOT safe to eat! It contains the following ingredients:\n";
        String badIngredients = "";
        for (Map.Entry<String, ArrayList<String>> entry : allergenIngredientsMap.entrySet()) {
            if (badIngredients.equals("")) {
                badIngredients = entry.getKey() + ": ";
            } else {
                badIngredients = "\n" + badIngredients + entry.getKey() + ": ";
            }
            for (int i = 0; i < (entry.getValue()).size(); i++) {
                if (i == 0) {
                    badIngredients = badIngredients + entry.getValue().get(i);
                } else {
                    badIngredients = badIngredients + ", " + entry.getValue().get(i);
                }
            }
            badIngredients = badIngredients + "\n";
        }

        return outStr + badIngredients;
    }

}
      /*  String ingredientInFood = "This food item is not safe to eat, as it contains ";
        for(int i = 0; i<allergenIngredientsMap.size(); i++){
            ingredientInFood = ingredientInFood + i;
        }
        return ingredientInFood; */