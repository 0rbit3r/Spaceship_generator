package cz.kakosa.spaceshipgenerator.Generation;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.widget.TextView;

import java.util.Random;

import cz.kakosa.spaceshipgenerator.MainActivity;
import cz.kakosa.spaceshipgenerator.R;

public class Generator {

    int width, height;

    int tenthWidth;
    int tenthHeight;

    int halfWidth;
    int halfHeight;

    int leftmostX, upperY, rightmostX, bottomY;

    Bitmap ship;
    Random r;

    MainActivity activity;

    /**
     * Creates new Random and stores the Activity that called the constructor.
     * @param activity Activity that called the constructor (used to get the drawable files and to get access to elements on screen)
     * @param seed Seed from which to generate the spaceship
     */
    public Generator(MainActivity activity, int seed) {
        r = new Random(seed);
        this.activity = activity;
    }

    /**
     * Creates new seed from which it creates new Random Generator. Then outputs the seed into textBox on the main screen. Stores the Activity that called the constructor.
     * @param activity Activity that called the constructor (used to get the drawable files and to get access to elements on screen)
     */
    public Generator(MainActivity activity) {
        Random seedGenerator = new Random();
        int seed = seedGenerator.nextInt(1000000);
        r = new Random(seed);
        this.activity = activity;
        TextView seedBox = activity.findViewById(R.id.seedTextNumber);
        seedBox.setText(String.valueOf(seed));
    }

    /**
     * Creates a new ship with color scheme and dimensions defined in Parameters
     * @return Bitmap with the generated ship on a transparent background
     */
    public Bitmap generateNewShip() {
        width = Parameters.getWidth();
        height = Parameters.getHeight();

        tenthWidth = (int) (width / 10);
        tenthHeight = (int) (height / 10);

        halfWidth = (int) (width / 2);
        halfHeight = (int) (height / 2);

        ship = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        generateShape();

        getShipDimensions();

        addDetails();

        symmetrize();

        repaint();

        upscale(10);
        return ship;
    }

    void upscale(double factor) {
        int newWidth = (int) (Parameters.getWidth() * factor);
        int newHeight = (int) (Parameters.getHeight() * factor);
        Bitmap newShip = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);
        for (int y = 0; y < newHeight; y++) {
            for (int x = 0; x < newWidth; x++) {
                newShip.setPixel(x, y, ship.getPixel((int) (x / factor), (int) (y / factor)));
            }
        }
        ship = newShip;
    }

    void generateBox(int x, int y, int width, int height) {
        for (int i = x; i < x + width; i++) {
            for (int j = y; j < y + height; j++) {
                ship.setPixel(i, j, 0xffff0000);
            }
        }
    }

    void symmetrize() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < halfWidth; x++) {
                ship.setPixel(width - 1 - x, y, ship.getPixel(x, y));
            }
        }
    }

    void rotate90(boolean right) {

        int degs = right ? 90 : -90;
        Matrix matrix = new Matrix();
        matrix.postRotate(degs);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(ship, width, height, true);
        ship = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);

        int temp = width;
        width = height;
        height = temp;

        temp = halfWidth;
        halfWidth = halfHeight;
        halfHeight = temp;

        temp = tenthWidth;
        tenthWidth = tenthHeight;
        tenthHeight = temp;

    }

    void interLineFill(int y0, int x00, int x01, int y1, int x10, int x11){
        int shading = r.nextInt(tenthWidth/2);
        for (int y = y0; y <= y1; y++) {
            //By using double and alpha you could do anti-aliasing
            int startX = ((y-y0)*(x10-x00)/(y1-y0) + x00);
            int endX =   ((y-y0)*(x11-x01)/(y1-y0) + x01);
            for (int x = startX; x <= endX; x++) {
                if (x - startX < shading || endX - x < shading || y - y0 < shading || y1 - y < shading)
                    ship.setPixel(x,y,0xffcccccc);
                else
                    ship.setPixel(x,y,0xffffffff);
            }
            if (r.nextInt(5) == 0)
                shading += r.nextInt(3) - 1;
        }
    }

    void generateShape(){
        generateHull();
        if (r.nextInt(3) == 0)
            generateFringes();

        //if (r.nextInt(3) != 0)
            generateWings();

    }

    void generateHull(){
        int yCurrent =  10 + r.nextInt((int) (0.7 * width));
        int xCurrent =  3 + r.nextInt( tenthWidth * 2);
        int yLast = 0;
        int xLast;
        int segments = 0;
        while (yCurrent < width*9/10 && (segments < 2 || r.nextInt(10) != 0)){
            yLast = yCurrent;
            yCurrent = Math.min (width - 8, yLast + 5 + r.nextInt(tenthWidth * 2));
            if (yCurrent > width - 8)
                break;
            xLast = xCurrent;
            xCurrent = Math.max(8, Math.min(halfWidth - 5, xLast + tenthWidth * 2 - r.nextInt(tenthWidth * 4)));

            interLineFill(yLast, halfWidth - xLast, halfWidth + xLast, yCurrent,halfWidth - xCurrent, halfWidth + xCurrent);
            segments++;
        }


    }

    void generateFringes(){
        int d = r.nextInt(10) > 5 ? 1 : -1;

        int yCurrent = -1;
        int x0Current = 0;

        for (int y = d == 1 ? 0 : height - 1; y != halfWidth + halfWidth * d; y += d) {
            for (int x = 0; x < width; x++) {
                if (ship.getPixel(x,y) == 0xffffffff){
                    yCurrent = y;
                    x0Current = x;
                    break;
                }
            }
            if (yCurrent != -1)
                break;
        }

        d = d = r.nextInt(10) > 5 ? 1 : -1;

        int x1Current = x0Current + 3 + r.nextInt(tenthWidth * 2);

        int yLast ;
        int x0Last;
        int x1Last;

        int segments = 0;
        while (yCurrent < 9*tenthHeight && yCurrent > tenthHeight && (segments < 2 || r.nextInt(10) != 0)){
            yLast = yCurrent;
            yCurrent = yLast + 5 * d + r.nextInt(tenthWidth * 2) * d;
            if (yCurrent < 8 || yCurrent >= height - 8)
                break;

            x0Last = x0Current;
            x1Last = x1Current;
            x0Current = Math.max(8, Math.min(halfWidth - 5, x0Last + tenthWidth - r.nextInt(tenthWidth * 3)));
            x1Current = Math.max(x0Current + 4, Math.min(halfWidth, x0Current + r.nextInt(tenthWidth * 2) - segments * (int) (tenthWidth/5)));


            if (d == -1)
                interLineFill(yCurrent, x0Current, x1Current, yLast, x0Last, x1Last);
            else
                interLineFill(yLast, x0Last, x1Last, yCurrent, x0Current, x1Current);
            segments++;
        }
    }

    void generateWings(){

        rotate90(false);

        int yCurrent = -1;
        int x0Current = 0;
        int x1Current = 0;
        int whites;

        for (int y = height - 1; y > 0; y--) {
            whites = 0;
            for (int x = 0; x < width; x++) {
                if (ship.getPixel(x,y) == 0xffffffff){
                    whites++;
                    if (whites > (int) (tenthWidth) || y == 1) {
                        yCurrent = y;
                        x0Current = x - r.nextInt(tenthWidth * 2) - 5;
                        x1Current = x;
                    }
                }
            }
        }

        int yLast ;
        int x0Last;
        int x1Last;

        int segments = 0;
        while (yCurrent < 9*tenthHeight && yCurrent > tenthHeight && (segments < 3 || r.nextInt(10) != 0)){
            yLast = yCurrent;
            yCurrent = yLast + 5 + r.nextInt(tenthWidth * 2);
            if (yCurrent < 0 || yCurrent >= height || x0Current < 11 || x1Current > width - 11)
                break;

            x0Last = x0Current;
            x1Last = x1Current;
            x0Current = Math.max(8, Math.min(width - 1 , x0Last + tenthWidth - r.nextInt(tenthWidth * 2)));
            x1Current =Math.min(width - 1,  Math.max(x0Current + 4, x1Last + tenthWidth - r.nextInt(tenthWidth * 2)));

            interLineFill(yLast, x0Last, x1Last, yCurrent, x0Current, x1Current);

            segments++;
        }

        rotate90(true);

    }

    void getShipDimensions(){

        upperY = -1;
        leftmostX = 0;
        rightmostX = width;
        bottomY = height;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int currColor = ship.getPixel(x,y);
                if (currColor == 0xffffffff || currColor == 0xffcccccc){
                    if (upperY == -1)
                        upperY = y;
                    if (x < leftmostX)
                        leftmostX = x;
                    if (rightmostX < x)
                        rightmostX = x;
                    if (bottomY < y)
                        bottomY = y;
                }
            }
        }
    }

    void addDetails(){
        for (int i = 0; i < 2; i++) {
            if (r.nextInt(2) == 0)
                addPoint();
        }

        addThruster();
        if(r.nextInt(2) == 0)
            addThruster();

        addSideMount();
        if(r.nextInt(3) == 0)
            addSideMount();

        for (int i = 0; i < 4; i++) {
            if (r.nextInt(2) == 0)
                addInnerDetail();
        }

        addCockpit();
        if(r.nextInt(5) == 0)
            addCockpit();
    }

    void drawOver(Bitmap img, int x0, int y0){
        int xStart = 0;
        if(x0 < 0)
            xStart = -x0;
        int yStart = 0;
        if (y0 < 0)
            yStart = -y0;

        int xEnd = img.getWidth();
        if (x0 + xEnd > width - 1)
            xEnd = width - x0;

        int yEnd = img.getHeight();
        if (y0 + yEnd > height - 1)
            yEnd = height - y0;

        int color;
        for (int y = yStart; y < yEnd; y++) {
            for (int x = xStart; x < xEnd; x++) {
                color = img.getPixel(x,y);
                if (color != 0)
                    ship.setPixel(x0+x, y0+y,color);
            }
        }
    }

    void addPoint(){
        Bitmap point = null;

        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inScaled = false;

        switch (r.nextInt(3)) {
            case 0:
                point = BitmapFactory.decodeResource(activity.getResources(), R.drawable.point_1, o);
                break;
            case 1:
                point = BitmapFactory.decodeResource(activity.getResources(), R.drawable.point_2, o);
                break;
            case 2:
                point = BitmapFactory.decodeResource(activity.getResources(), R.drawable.point_3, o);
                break;
        }
        addVertically(true, point, -5, -9);
    }

    void addThruster(){
        Bitmap point = null;

        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inScaled = false;

        switch (r.nextInt(3)) {
            case 0:
                point = BitmapFactory.decodeResource(activity.getResources(), R.drawable.thruster_1, o);
                break;
            case 1:
                point = BitmapFactory.decodeResource(activity.getResources(), R.drawable.thruster_2, o);
                break;
            case 2:
                point = BitmapFactory.decodeResource(activity.getResources(), R.drawable.thruster_3, o);
                break;
        }
        addVertically(false, point, -5, -1);
    }

    void addSideMount(){
        Bitmap mount = null;

        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inScaled = false;

        switch (r.nextInt(4)) {
            case 0:
                mount = BitmapFactory.decodeResource(activity.getResources(), R.drawable.side_mount_1, o);
                break;
            case 1:
                mount = BitmapFactory.decodeResource(activity.getResources(), R.drawable.side_mount_2, o);
                break;
            case 2:
                mount = BitmapFactory.decodeResource(activity.getResources(), R.drawable.side_mount_3, o);
                break;
            case 3:
                mount = BitmapFactory.decodeResource(activity.getResources(), R.drawable.side_mount_4, o);
                break;
        }
        addHorizontally(mount, -7, -5);
    }

    void addInnerDetail() {
        int y;
        int x;
        int limit = 25;
        do{
            y = upperY + r.nextInt(bottomY - upperY) + 5;
            x = leftmostX + r.nextInt(halfWidth - leftmostX) + 5;
            limit--;
        } while(! checkHull(x - 5, y - 5, 4,6) && limit > 0);

        Bitmap lightStrip = null;

        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inScaled = false;

        switch (r.nextInt(7)) {
            case 0:
                lightStrip = BitmapFactory.decodeResource(activity.getResources(), R.drawable.inner_1, o);
                break;
            case 1:
                lightStrip = BitmapFactory.decodeResource(activity.getResources(), R.drawable.inner_2, o);
                break;
            case 2:
                lightStrip = BitmapFactory.decodeResource(activity.getResources(), R.drawable.inner_3, o);
                break;
            case 3:
                lightStrip = BitmapFactory.decodeResource(activity.getResources(), R.drawable.inner_4, o);
                break;
            case 4:
                lightStrip = BitmapFactory.decodeResource(activity.getResources(), R.drawable.inner_5, o);
                break;
            case 5:
                lightStrip = BitmapFactory.decodeResource(activity.getResources(), R.drawable.inner_6, o);
                break;
            case 6:
                lightStrip = BitmapFactory.decodeResource(activity.getResources(), R.drawable.inner_7, o);
                break;
        }
        drawOver(lightStrip, x - 5, y - 5);
    }

    void addVertically(boolean up, Bitmap img, int offsetX, int offSetY){
        int x0 = leftmostX + r.nextInt(halfWidth - leftmostX);

        int color;

        int yStart = up ? 0 : width - 1;
        int yEnd = up ? width - 1 : 0;
        int d = up ? 1 : -1;

        for (int y = yStart; y != yEnd; y += d) {
            color = ship.getPixel(x0,y);
            if (color == 0xffffffff || color == 0xffcccccc){
                drawOver(img, x0 + offsetX, y + offSetY);
                break;
            }
        }
    }

    void addHorizontally(Bitmap img, int offsetX, int offSetY){
        int y0 = upperY + r.nextInt(bottomY - upperY);

        int color;

        for (int x = 0; x < halfWidth; x++) {
            color = ship.getPixel(x,y0);
            if (color == 0xffffffff || color == 0xffcccccc){
                drawOver(img, x + offsetX, y0 + offSetY);
                break;
            }
        }
    }

    boolean checkHull(int x0, int y0, int width, int height){
        if (x0 < 0 || y0 < 0 || x0 + width >= this.width || y0 + height >= this.height)
            return false;

        for (int y = y0; y < y0 + height; y++) {
            for (int x = x0; x < x0 + width; x++) {
                if (ship.getPixel(x,y) != 0xffffffff && ship.getPixel(x,y) != 0xffcccccc){
                    return false;
                }
            }
        }
        return true;
    }

    void addCockpit(){
        int y;
        int limit = 15;
        do{
            y = upperY + r.nextInt(bottomY - upperY) + 5;
            limit--;
        } while(! checkHull(halfWidth - 3, y, 6,5) && limit > 0);

        Bitmap cockpit = null;

        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inScaled = false;

        switch (r.nextInt(3)) {
            case 0:
                cockpit = BitmapFactory.decodeResource(activity.getResources(), R.drawable.cockpit_1, o);
                break;
            case 1:
                cockpit = BitmapFactory.decodeResource(activity.getResources(), R.drawable.cockpit_2, o);
                break;
            case 2:
                cockpit = BitmapFactory.decodeResource(activity.getResources(), R.drawable.cockpit_3, o);
                break;
        }
        drawOver(cockpit, halfWidth - 5, y - 5);
    }

    int randomColor(){
        int red = r.nextInt(0xff);
        int green = r.nextInt(0xff);
        int blue = r.nextInt(0xff);
        return 0xff000000 + red * 0x10000 + green * 0x100 + blue;
    }

    int modifyColor(int orig, double factor){
        orig = orig - 0xff000000;
        int red = orig / 0x10000;
        int green = (orig - red * 0x10000) / 0x100;
        int blue = (orig - red * 0x10000 - green * 0x100);
        return 0xff000000 + (int) Math.min((red * factor), 0xff) * 0x10000 + (int) Math.min((green * factor), 0xff) * 0x100 + (int) Math.min((blue * factor), 0xff);
    }

    /**
     * Creates a color scheme and saves it in Parameters
     */
    public void generateColors(){

        int color = randomColor();
        color = modifyColor(color, 0.8);
        Parameters.setPrimaryColor(color);
        color = modifyColor(color, 0.5);
        Parameters.setSecondaryColor(color);

        color = randomColor();
        Parameters.setSecondaryEffectColor(color);
        color = modifyColor(color, 2);
        Parameters.setPrimaryEffectColor(color);

        color = randomColor();
        Parameters.setPrimaryGlassColor(color);
        color = modifyColor(color, 0.5);
        Parameters.setSecondaryGlassColor(color);
    }

    void repaint(){
        int primary = Parameters.getPrimaryColor();
        int secondary = Parameters.getSecondaryColor();
        int glass1 = Parameters.getPrimaryGlassColor();
        int glass2 = Parameters.getSecondaryGlassColor();
        int effects1 = Parameters.getPrimaryEffectColor();
        int effects2 = Parameters.getSecondaryEffectColor();


        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                switch (ship.getPixel(x,y)) {
                    case 0xffffffff:
                        ship.setPixel(x,y,primary);
                        break;
                    case 0xffcccccc:
                        ship.setPixel(x,y,secondary);
                        break;
                    case 0xfff600ff:
                        ship.setPixel(x,y,glass1);
                        break;
                    case 0xffff66ff:
                        ship.setPixel(x,y,glass2);
                        break;
                    case 0xffff0000:
                        ship.setPixel(x,y,effects1);
                        break;
                    case 0xffcc0000:
                        ship.setPixel(x,y,effects2);
                        break;
                }
            }
        }
    }
}
























