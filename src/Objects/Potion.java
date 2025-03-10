package Objects;

import main.Game;

// pot fi colectate de către jucător pentru a-și îmbunătății viața și power attack
// realizează o animație de „plutire” pentru poțiuni și le actualizează pozițiile în timpul jocului

public class Potion extends GameObject{


    //Offset-ul de plutire verticală al poțiunii
    private float hoverOffset;

    //Amplitudinea maximă a mișcării de plutire și direcția de mișcare
    private int maxHoverOffset, hoverDir = 1;

    public Potion(int x, int y, int objType) {
        super(x, y, objType);
        //trebuie să aibă o animație
        doAnimation = true;
        initHitBox(7, 14);  // 9

        //Offset-ul pentru desenarea imaginii poțiunii, scalate conform dimensiunii jocului
        xDrawOffset = (int)(3 * Game.SCALE);
        yDrawOffset = (int)(2 * Game.SCALE);
        maxHoverOffset = (int) (10 * Game.SCALE);
    }

    //actualizează starea poțiunii prin actualizarea animației și a mișcării de plutire
    public void update() {
        updateAnimationTick();
        updateHover();
    }

    private void updateHover() {
        //modificat în funcție de hoverDir, care determină direcția mișcării (sus sau jos)
        hoverOffset += (0.075f * Game.SCALE * hoverDir);

        //Dacă hoverOffset atinge maxHoverOffset sau scade sub 0, direcția mișcării este inversată
        if (hoverOffset >= maxHoverOffset)
            hoverDir = -1;
        else if (hoverOffset < 0)
            hoverDir = 1;

        //hitBox.y este actualizat pentru a reflecta noua poziție verticală a poțiunii
        hitBox.y = y + hoverOffset;
    }
}
