package ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import utils.LoadSave;
import static utils.Constants.UI.URMButtons.*;

// pentru a crea butoane speciale: revenire la meniu principal, reluarea nivelului, resetarea nivelului

public class UrmButton extends PauseButton {
    private BufferedImage[] imgs;
    private int rowIndex, index;
    private boolean mouseOver, mousePressed;

    public UrmButton(int x, int y, int width, int height, int rowIndex) {
        super(x, y, width, height);
        this.rowIndex = rowIndex;
        loadImgs();
    }

    //  încărcarea imaginilor butonului dintr-un fișier de atlas de sprite-uri
    private void loadImgs() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.URM_BUTTONS);
        imgs = new BufferedImage[3];
        for (int i = 0; i < imgs.length; i++)
            imgs[i] = temp.getSubimage(i * URM_DEFAULT_SIZE, rowIndex * URM_DEFAULT_SIZE, URM_DEFAULT_SIZE, URM_DEFAULT_SIZE);

    }

    // actualizează indexul imaginii care trebuie desenată în funcție de starea butonului (dacă mouse-ul este deasupra butonului sau dacă butonul este apăsat)
    // Indexul este utilizat pentru a selecta imaginea corespunzătoare din tabloul imgs
    public void update() {
        index = 0;
        if (mouseOver)
            index = 1;
        if (mousePressed)
            index = 2;

    }

    // Imaginea desenată este cea corespunzătoare indexului calculat în metoda update()
    public void draw(Graphics g) {
        g.drawImage(imgs[index], x, y, URM_SIZE, URM_SIZE, null);
    }


    //resetate între cadrele de desenare
    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }

    //  indică dacă mouse-ul este deasupra butonului sau nu
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

}