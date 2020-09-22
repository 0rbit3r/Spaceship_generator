package cz.kakosa.spaceshipgenerator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cz.kakosa.spaceshipgenerator.databinding.ActivityMainBinding;
import cz.kakosa.spaceshipgenerator.generation.Generator;
import cz.kakosa.spaceshipgenerator.generation.Parameters;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
    }

    /**
     * Creates new ship from random seed and outputs the seed in seedTextNumber
     *
     * @param v View that called the method
     */
    public void newRandomShip(View v) {

        lockButtons();
        Generator gen = new Generator(this, new Parameters(100, 100, 10));
        gen.start();
    }

    /**
     * Creates new ship from seed given by user in seedTextNumber
     *
     * @param v View that called the method
     */
    public void newSeededShip(View v) {

        lockButtons();

        TextView seedBox = findViewById(R.id.seedTextNumber);
        String stringSeed = String.valueOf(seedBox.getText());
        if (stringSeed.length() > 7) {
            stringSeed = "9999999";
            seedBox.setText(stringSeed);
        }
        int seed = Integer.valueOf(stringSeed);

        Generator gen = new Generator(this, new Parameters(100, 100, 10), seed);
        gen.start();
    }

    void lockButtons(){
        TextView genIcon = findViewById(R.id.loadingTextView);
        Button seedButton = findViewById(R.id.seedShipButton);
        Button randomButton = findViewById(R.id.newShipButton);

        genIcon.setVisibility(View.VISIBLE);
        seedButton.setEnabled(false);
        randomButton.setEnabled(false);
    }

}