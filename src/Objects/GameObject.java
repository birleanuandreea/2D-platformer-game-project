package Objects;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static utils.Constants.ANI_SPEED;
import static utils.Constants.ObjectConstants.*;

// reprezintă obiectele generice din joc, precum poțiuni, cufăr și obiecte periculoase
// gestionează aspecte comune ale acestor obiecte, cum ar fi actualizarea animației și dezactivarea

public class GameObject {

    //definesc poziția obiectului pe ecran si tipul acestuia
    protected int x, y, objType;

    //definește zona de coliziune a obiectului
    protected Rectangle2D.Float hitBox;

    //doAnimation indică dacă obiectul trebuie să fie animat, iar active indică dacă obiectul este activ în joc
    protected boolean doAnimation, active = true;

    //aniTick este un contor pentru actualizarea frame-urilor animației, iar aniIndex indică frame-ul curent al animației
    protected int aniTick, aniIndex;

    //Offset-urile pentru desenarea corectă a imaginii obiectului pe ecran
    protected int xDrawOffset, yDrawOffset;

    public GameObject(int x, int y, int objType){
        this.x = x;
        this.y = y;
        this.objType = objType;
    }


    // actualizează animația obiectului, incrementând aniTick și trecând la următorul frame al animației dacă aniTick depășește ANI_SPEED
    protected void updateAnimationTick(){
        aniTick++;
        if(aniTick >= ANI_SPEED){
            aniTick = 0;
            aniIndex++;
            if(aniIndex >= GetSpriteAmount(objType)){
                aniIndex = 0;
                // animația se oprește și obiectul devine inactiv după ce se termină
                if(objType == CHEST){
                    doAnimation = false;
                    active = false;
                }
            }
        }
    }

    public void reset(){
        aniIndex = 0;
        aniTick = 0;
        active = true;

        // Pentru cufăr, animația este dezactivată, iar pentru celelalte obiecte, animația este activată
        if(objType == CHEST)
            doAnimation = false;
        else
            doAnimation = true;

    }
    protected void initHitBox(int width, int height) {
        hitBox = new Rectangle2D.Float(x, y,width, height);
    }
   public void drawHitBox(Graphics g, int xLvlOffset){
        // for debugging the hitBox
        g.setColor(Color.PINK);
        g.drawRect((int)hitBox.x - xLvlOffset, (int)hitBox.y, (int)hitBox.width, (int)hitBox.height);
    }
    public int getObjType() {
        return objType;
    }

    public Rectangle2D.Float getHitbox() {
        return hitBox;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setAnimation(boolean doAnimation) {
        this.doAnimation = doAnimation;
    }

    public int getxDrawOffset() {
        return xDrawOffset;
    }

    public int getyDrawOffset() {
        return yDrawOffset;
    }

    public int getAniIndex() {
        return aniIndex;
    }
}
