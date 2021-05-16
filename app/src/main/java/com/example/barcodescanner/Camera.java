package com.example.barcodescanner;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class Camera extends AppCompatActivity {
    public static final String UPC = "com.example.barcodescanner.MESSAGE1";
    public static final String ALLERGENSFORWARDED = "com.example.barcodescanner.MESSAGE2";
    private String allergens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera1);

        Intent intent = getIntent();
        allergens = intent.getExtras().getString(MainActivity.ALLERGENS);

        TextView tvAllergens = findViewById(R.id.tvAllergens);
        tvAllergens.setText(allergens);
        // Scan the barcode.
        scanCode();
    }

    // Then unpack string using split function.
    private void scanCode() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning Code");
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                Intent toResult = new Intent(this, AllergenResult.class);
                toResult.putExtra(UPC, result.getContents());
                toResult.putExtra(ALLERGENSFORWARDED, allergens);
                startActivity(toResult);
            } else {
                Toast.makeText(this, "No Results", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
