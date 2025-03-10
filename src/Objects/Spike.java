package Objects;

import main.Game;

// capcanele din joc, care pot răni jucătorul dacă intră în contact cu ele

public class Spike extends GameObject{

    public Spike(int x, int y, int objType){
        super(x, y, objType);

        initHitBox(32, 16);
        xDrawOffset = 0;
        yDrawOffset = (int)(Game.SCALE * 16);
        hitBox.y += yDrawOffset;
    }
}
