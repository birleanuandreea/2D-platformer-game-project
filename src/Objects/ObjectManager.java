
package Objects;

import entities.Player;
import gamestates.Playing;
import levels.Level;
import main.Game;
import utils.Constants;
import utils.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import utils.Constants.*;

import static Objects.Chest.GetChest;
import static utils.Constants.ObjectConstants.*;

// se ocupă de gestionarea și actualizarea obiectelor din joc, verifică coliziunile dintre jucător și diverse obiecte,
// desenează obiectele pe ecran și le actualizează pozițiile și stările

public class ObjectManager {

    private Playing playing;

    //Matrice de imagini pentru poțiuni și cufere
    private BufferedImage[][] potionImgs, chestImgs;
    private BufferedImage spikeImg;
    private ArrayList<Potion> potions;
    private ArrayList<Spike> spikes;

    public ObjectManager(Playing playing) {
        this.playing = playing;
        loadImgs();
        GetChest();
    }

    //Verificarea coliziunilor cu capcane
    public void checkSpikesTouched(Player p){
        for(Spike s : spikes)
            if(s.getHitbox().intersects(p.getHitBox()))
                p.kill();

    }


    //Verificarea coliziunilor cu obiecte(potiuni)
    public void checkObjectTouched(Rectangle2D.Float hitBox) {
        for (Potion p : potions)
            if (p.isActive()) {
                if (hitBox.intersects(p.getHitbox())) {
                    p.setActive(false);
                    applyEffectToPlayer(p);
                }
            }
    }

    //Aplicarea efectelor poțiunilor asupra jucătorului
    public void applyEffectToPlayer(Potion p) {
        if (p.getObjType() == RED_POTION)
            playing.getPlayer().changeHealth(RED_POTION_VALUE);
        else
            playing.getPlayer().changePower(BLUE_POTION_VALUE + 30);
    }

    //Verificarea coliziunilor cu cufere
    public void checkObjectHit(Rectangle2D.Float attackBox) {
        if (GetChest().isActive() && !GetChest().doAnimation) {
            if (GetChest().getHitbox().intersects(attackBox)) {
                GetChest().setAnimation(true);
                potions.add(new Potion((int) (GetChest().getHitbox().x + GetChest().getHitbox().width / 2), (int) (GetChest().getHitbox().y - GetChest().getHitbox().height / 2), RED_POTION));
            }
        }
    }

    //incărcarea obiectelor dintr-un nivel
    public void loadObjects(Level newLevel) {
        potions = new ArrayList<>(newLevel.getPotions());
        spikes = newLevel.getSpikes();
    }
    private void loadImgs() {
        potionImgs = new BufferedImage[2][7];
        BufferedImage potionSprite = LoadSave.GetSpriteAtlas(LoadSave.POTION_ATLAS);

        for (int j = 0; j < potionImgs.length; j++) {
            for (int i = 0; i < potionImgs[j].length; i++) {
                potionImgs[j][i] = potionSprite.getSubimage(12 * i, 16 * j, 12, 16);//12
            }
        }

        BufferedImage chestSprite = LoadSave.GetSpriteAtlas(LoadSave.CHEST_ATLAS);
        chestImgs = new BufferedImage[1][5];

        for (int j = 0; j < chestImgs.length; j++) {
            for (int i = 0; i < chestImgs[j].length; i++) {
                chestImgs[j][i] = chestSprite.getSubimage(48 * i, 29 * j, 48, 29);
            }
        }

        spikeImg = LoadSave.GetSpriteAtlas(LoadSave.TRAP_ATLAS);
    }

    public void update() {
       for (Potion p : potions) {
            if (p.isActive()) {
                p.update();
            }
       }
       if (GetChest().isActive()) {
            GetChest().update();
        }
    }

    public void draw(Graphics g, int xLvlOffset) {
        drawPotions(g, xLvlOffset);
        drawChests(g, xLvlOffset);
        drawTraps(g, xLvlOffset);
    }

    private void drawTraps(Graphics g, int xLvlOffset) {
        for(Spike s : spikes)
            g.drawImage(spikeImg, (int)(s.getHitbox().x - xLvlOffset), (int)(s.getHitbox().y - s.getyDrawOffset()), SPIKE_WIDTH, SPIKE_HEIGHT, null);
    }

    private void drawPotions(Graphics g, int xLvlOffset) {
        for (Potion p : potions) {
            if (p.isActive()) {
                int type = 0;
                if (p.getObjType() == RED_POTION)
                    type = 1;
                g.drawImage(potionImgs[type][p.getAniIndex()], (int) (p.getHitbox().x - p.getxDrawOffset() - xLvlOffset), (int) (p.getHitbox().y - p.getyDrawOffset()), POTION_WIDTH, POTION_HEIGHT, null);
            }
        }
    }

    private void drawChests(Graphics g, int xLvlOffset) {
        if (GetChest().isActive()) {
            g.drawImage(chestImgs[0][GetChest().getAniIndex()], (int) (GetChest().getHitbox().x - GetChest().getxDrawOffset() - xLvlOffset), (int) (GetChest().getHitbox().y - GetChest().getyDrawOffset()), CHEST_WIDTH, CHEST_HEIGHT, null);
        }
    }
    public void resetAllObjects() {
        //System.out.println("Size of arrays: " + potions.size());
        loadObjects(playing.getLevelManager().getCurrentLevel());

        for (Potion p : potions)
            p.reset();

        //System.out.println("Size of arrays: " + potions.size());
    }

}
