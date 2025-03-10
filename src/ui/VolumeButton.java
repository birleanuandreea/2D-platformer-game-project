package ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import utils.LoadSave;
import static utils.Constants.UI.VolumeButtons.*;

// pentru a crea butoane speciale: un controler de volum
public class VolumeButton extends PauseButton {

    private BufferedImage[] imgs;

    //O imagine reprezentând un cursor pentru ajustarea volumului
    private BufferedImage slider;
    private int index = 0;
    private boolean mouseOver, mousePressed;

    //Poziția minimă și maximă pe axa orizontală la care se poate deplasa butonul
    private int buttonX, minX, maxX;

    //Valoarea float care reprezintă volumul curent, exprimată ca un procentaj între 0 și 1
    private float floatValue = 0f;

    public VolumeButton(int x, int y, int width, int height) {
        super(x + width / 2, y, VOLUME_WIDTH, height);
        bounds.x -= VOLUME_WIDTH / 2;
        buttonX = x + width / 2;
        this.x = x;
        this.width = width;
        minX = x + VOLUME_WIDTH / 2;
        maxX = x + width - VOLUME_WIDTH / 2;
        loadImgs();
    }

    // Încarcă imaginile butonului de volum și a cursorului dintr-un atlas de sprite-uri
    private void loadImgs() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.VOLUME_BUTTONS);
        imgs = new BufferedImage[3];
        for (int i = 0; i < imgs.length; i++)
            imgs[i] = temp.getSubimage(i * VOLUME_DEFAULT_WIDTH, 0, VOLUME_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);

        slider = temp.getSubimage(3 * VOLUME_DEFAULT_WIDTH, 0, SLIDER_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);

    }

    public void update() {
        index = 0;
        if (mouseOver)
            index = 1;
        if (mousePressed)
            index = 2;

    }

    public void draw(Graphics g) {

        g.drawImage(slider, x, y, width, height, null);
        g.drawImage(imgs[index], buttonX - VOLUME_WIDTH / 2, y, VOLUME_WIDTH, height, null);

    }

    //Actualizează poziția butonului pe axa orizontală în funcție de coordonata x specificată, ținând cont de limitele minime și maxime
    public void changeX(int x) {
        if (x < minX)
            buttonX = minX;
        else if (x > maxX)
            buttonX = maxX;
        else
            buttonX = x;

        updateFloatValue();
        bounds.x = buttonX - VOLUME_WIDTH / 2;

    }

    //volumul curent în funcție de poziția butonului pe axa orizontală
    private void updateFloatValue() {
        float range = maxX - minX;
        float value = buttonX - minX;
        floatValue = value/range;
    }

    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }
    public float getFloatValue(){return floatValue;}
}