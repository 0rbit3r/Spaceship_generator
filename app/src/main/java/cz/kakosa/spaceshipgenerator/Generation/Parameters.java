package cz.kakosa.spaceshipgenerator.Generation;

public class Parameters {

    public static int width  = 100;
    public static int height = 100;

    public static int screenWidth;
    public static int screenHeight;

    public static int primaryColor;
    public static int secondaryColor;
    public static int primaryGlassColor;
    public static int secondaryGlassColor;
    public static int primaryEffectColor;
    public static int secondaryEffectColor;


    public static int getPrimaryColor() {
        return primaryColor;
    }

    public static void setPrimaryColor(int primaryColor) {
        Parameters.primaryColor = primaryColor;
    }

    public static int getSecondaryColor() {
        return secondaryColor;
    }

    public static int getPrimaryGlassColor() {
        return primaryGlassColor;
    }

    public static void setSecondaryColor(int secondaryColor) {
        Parameters.secondaryColor = secondaryColor;
    }

    public static void setSecondaryGlassColor(int secondaryGlassColor) {
        Parameters.secondaryGlassColor = secondaryGlassColor;
    }

    public static int getSecondaryEffectColor() {
        return secondaryEffectColor;
    }

    public static void setPrimaryGlassColor(int primaryGlassColor) {
        Parameters.primaryGlassColor = primaryGlassColor;
    }

    public static int getSecondaryGlassColor() {
        return secondaryGlassColor;
    }

    public static int getPrimaryEffectColor() {
        return primaryEffectColor;
    }

    public static void setPrimaryEffectColor(int primaryEffectColor) {
        Parameters.primaryEffectColor = primaryEffectColor;
    }

    public static void setSecondaryEffectColor(int secondaryEffectColor) {
        Parameters.secondaryEffectColor = secondaryEffectColor;
    }

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static void setScreenWidth(int screenWidth) {
        Parameters.screenWidth = screenWidth;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }

    public static void setScreenHeight(int screenHeight) {
        Parameters.screenHeight = screenHeight;
    }

    public static int getWidth() {
        return width;
    }

    public static void setWidth(int width) {
        if (width >= 50)
            Parameters.width = width;
    }

    public static int getHeight() {
        return height;
    }

    public static void setHeight(int height) {
        if (height >= 50)
            Parameters.height = height;
    }
}
