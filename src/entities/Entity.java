package entities;

import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static utils.Constants.Directions.*;
import static utils.HelpMethods.CanMoveHere;

public abstract class Entity {

    protected float x, y;
    protected int width, height;
    protected Rectangle2D.Float hitBox;
    protected Rectangle2D.Float attackBox;

    // utilizat pentru a controla viteza de animare a sprite-urilor și pentru a asigura sincronizarea între diferitele cadre de animație
    protected int aniTick;
    protected int  aniIndex;
    protected int state;
    protected float airSpeed;
    protected boolean inAir = false;
    protected int maxHealth;
    protected int currentHealth;
    protected float walkSpeed;
    protected int pushBackDir;
    protected float pushDrawOffset;
    protected int pushBackOffsetDir = UP;
    public Entity(float x, float y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    // responsabilă pentru actualizarea offset-ului de desenare a împingerii înapoi a jucătorului
    protected void updatePushBackDrawOffset() {
        float speed = 0.95f;
        float limit = -30f;

        if (pushBackOffsetDir == UP) {
            pushDrawOffset -= speed;
            if (pushDrawOffset <= limit)
                pushBackOffsetDir = DOWN;
        } else {
            pushDrawOffset += speed;
            if (pushDrawOffset >= 0)
                pushDrawOffset = 0;
        }
    }

    // pentru a simula împingerea jucătorului într-o anumită direcție (LEFT sau RIGHT) în urma unui atac a unui inamic
    protected void pushBack(int pushBackDir, int[][] lvlData, float speedMulti) {
        float xSpeed = 0;
        if (pushBackDir == LEFT)
            xSpeed = -walkSpeed;    // deplasare la stanga
        else
            xSpeed = walkSpeed;     // deplasare la dreapta

        if (CanMoveHere(hitBox.x + xSpeed * speedMulti, hitBox.y, hitBox.width, hitBox.height, lvlData))
            hitBox.x += xSpeed * speedMulti;    // ajustarea pozitiei pe X
    }

    protected void drawHitBox(Graphics g, int xLvlOffset){
        // for debugging the hitBox
        g.setColor(Color.PINK);
        g.drawRect((int)hitBox.x - xLvlOffset, (int)hitBox.y, (int)hitBox.width, (int)hitBox.height);
    }
    protected void drawAttackBox(Graphics g, int xLvlOffset) {
        g.setColor(Color.red);
        g.drawRect((int) (attackBox.x - xLvlOffset), (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
    }

    protected void initHitBox(int width, int height) {
        hitBox = new Rectangle2D.Float(x, y, (int) (width * Game.SCALE), (int) (height * Game.SCALE));
    }
    public Rectangle2D.Float getHitBox(){
        return hitBox;
    }
    public int getAniIndex(){
        return aniIndex;
    }
    public int getState() {
        return state;
    }
    protected void newState(int state) {
        this.state = state;
        aniTick = 0;
        aniIndex = 0;
    }
}
