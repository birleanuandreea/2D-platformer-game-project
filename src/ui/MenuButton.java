package ui;

import gamestates.Gamestate;
import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import static utils.Constants.UI.Buttons.*;

// utilizată pentr a crea și gestiona butoanele din meniul principal al jocului
// aceasta clasează butoanele în funcție de starea jocului

public class MenuButton {

    private int xPos, yPos, rowIndex, index;

    // Offsetul pentru centrarea butonului pe axa x
    private int xOffsetCenter = B_WIDTH / 2, yOffsetCenter;

    //Stările butonului în funcție de acțiunile mouse-ului
    private boolean mousePressed, mouseOver;
    private Gamestate state;
    private BufferedImage[] imgs;
    private Rectangle bounds;   //zona de coliziune a butonului
    public MenuButton(int xPos, int yPos, int rowIndex, Gamestate state){
        this.xPos = xPos;
        this.yPos = yPos;
        this.rowIndex = rowIndex;
        this.state = state;
        loadImags();
        initBounds();
    }

    private void initBounds() {
        bounds = new Rectangle(xPos - xOffsetCenter, yPos, B_WIDTH, B_HEIGHT);
    }

    private void loadImags() {
        imgs = new BufferedImage[3];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.MENU_BUTTONS);
        for(int i=0; i< imgs.length;i++){
            imgs[i] = temp.getSubimage(i * B_WIDTH_DEFAULT, rowIndex * B_HEIGHT_DEFAULT, B_WIDTH_DEFAULT, B_HEIGHT_DEFAULT);
        }
    }
    public void draw(Graphics g){
        g.drawImage(imgs[index], xPos - xOffsetCenter, yPos, B_WIDTH, B_HEIGHT, null);
    }
    public void update(){

        index = 0;
        if(mouseOver){
            index = 1;
        }
        if(mousePressed){
            index = 2;
        }
    }
    public Rectangle getBounds() {
        return bounds;
    }
    public boolean isMouseOver(){
        return mouseOver;
    }
    public void setMouseOver(boolean mouseOver){
        this.mouseOver = mouseOver;
    }
    public boolean isMousePressed(){
        return mousePressed;
    }
    public void setMousePressed(boolean mousePressed){
        this.mousePressed = mousePressed;
    }
    public void applyGamestate(){
        Gamestate.state = state;
    }
    public void resetBools(){
        mouseOver = false;
        mousePressed = false;
    }
    public Gamestate getState(){return state;}
}
