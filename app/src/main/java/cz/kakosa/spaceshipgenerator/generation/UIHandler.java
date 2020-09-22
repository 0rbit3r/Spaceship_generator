package cz.kakosa.spaceshipgenerator.generation;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import cz.kakosa.spaceshipgenerator.R;

public class UIHandler extends Thread {


    TextView genIcon;
    Button seedButton;
    Button randomButton;
    ImageView imgShip;

    Bitmap ship;

    public UIHandler(Activity activity){
        genIcon = activity.findViewById(R.id.loadingTextView);
        seedButton = activity.findViewById(R.id.seedShipButton);
        randomButton = activity.findViewById(R.id.newShipButton);
        imgShip = activity.findViewById(R.id.imgShip);
    }

    void showAndUnlock(){
        imgShip.setImageBitmap(ship);

        genIcon.setVisibility(View.INVISIBLE);
        randomButton.setEnabled(true);
        seedButton.setEnabled(true);
    }

    public void setShip(Bitmap ship) {
        this.ship = ship;
    }

    public void run(){
        showAndUnlock();
    }

}
