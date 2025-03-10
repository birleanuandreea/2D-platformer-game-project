package Objects;

import main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utils.Constants.ObjectConstants.*;
import static utils.LoadSave.GetSpriteAtlas;

// poate fi deschis de jucător pentru a elibera poțiuni
// gestionează animația deschiderii cufărului și actualizează starea obiectului în funcție de acțiunile jucătorului
// SINGLETON
public class Chest extends GameObject{

    private static Chest chest;
    private Chest(int x, int y, int objType) {
        super(x, y, objType);
        initHitBox(32, 22);
        xDrawOffset = (int)(3 * Game.SCALE);
        yDrawOffset = (int)(2 * Game.SCALE);
    }
    public static Chest GetChest(){

        if(chest == null){
            chest = new Chest(900, 294, CHEST);
        }
        return chest;
    }

    public void update(){
        if(doAnimation)
            updateAnimationTick();
    }
}
