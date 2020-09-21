package cz.kakosa.spaceshipgenerator;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cz.kakosa.spaceshipgenerator.Generation.Generator;
import cz.kakosa.spaceshipgenerator.Generation.Parameters;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imgShip = findViewById(R.id.imgShip);
        Parameters.setScreenWidth(imgShip.getWidth());
        Parameters.setScreenHeight(imgShip.getHeight());
    }

    public void newRandomShip(View v){

        ImageView imgShip = findViewById(R.id.imgShip);

        Generator gen = new Generator(this);
        gen.generateColors();

        Bitmap ship = gen.generateNewShip();
        imgShip.setImageBitmap(ship);
    }

    public void newSeededShip(View v){


        ImageView imgShip = findViewById(R.id.imgShip);
        TextView seedBox = findViewById(R.id.seedTextNumber);
        String stringSeed = String.valueOf(seedBox.getText());
        if (stringSeed.length() > 7) {
            stringSeed = "9999999";
            seedBox.setText(stringSeed);
        }
        int seed = Integer.valueOf(stringSeed);
        Generator gen = new Generator(this, seed);

        gen.generateColors();

        Bitmap ship = gen.generateNewShip();
        imgShip.setImageBitmap(ship);
    }
}