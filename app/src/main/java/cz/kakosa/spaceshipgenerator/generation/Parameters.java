package cz.kakosa.spaceshipgenerator.generation;

public class Parameters {

    int width;
    int height;
    double scaleFactor;

    int primaryColor = 0xff717171;
    int secondaryColor = 0xff353535;
    int primaryGlassColor = 0xff35fffd;
    int secondaryGlassColor = 0xff87fffe;
    int primaryEffectColor = 0xffff0000;
    int secondaryEffectColor = 0xffcc0000;

    public Parameters(int w, int h, double sf) {
        this.width = w;
        this.height = h;
        this.scaleFactor = sf;

        if (width > 200) {
            width = 200;
        } else if (width < 50) {
            width = 50;
        }

        if (height > 200) {
            height = 200;
        } else if (height < 50) {
            height = 50;
        }

        if (scaleFactor < 1) {
            scaleFactor = 1;
        }
        if (scaleFactor > 20) {
            scaleFactor = 20;
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public double getScaleFactor() {
        return scaleFactor;
    }

    public int getPrimaryColor() {
        return primaryColor;
    }

    public int getSecondaryColor() {
        return secondaryColor;
    }

    public int getPrimaryGlassColor() {
        return primaryGlassColor;
    }

    public int getSecondaryGlassColor() {
        return secondaryGlassColor;
    }

    public int getPrimaryEffectColor() {
        return primaryEffectColor;
    }

    public int getSecondaryEffectColor() {
        return secondaryEffectColor;
    }

    public void setPrimaryColor(int primaryColor) {
        this.primaryColor = primaryColor;
    }

    public void setSecondaryColor(int secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public void setPrimaryGlassColor(int primaryGlassColor) {
        this.primaryGlassColor = primaryGlassColor;
    }

    public void setSecondaryGlassColor(int secondaryGlassColor) {
        this.secondaryGlassColor = secondaryGlassColor;
    }

    public void setPrimaryEffectColor(int primaryEffectColor) {
        this.primaryEffectColor = primaryEffectColor;
    }

    public void setSecondaryEffectColor(int secondaryEffectColor) {
        this.secondaryEffectColor = secondaryEffectColor;
    }
}
