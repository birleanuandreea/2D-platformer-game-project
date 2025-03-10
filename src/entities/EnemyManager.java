package entities;

import gamestates.Playing;
import levels.Level;
import utils.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import static utils.Constants.EnemyConstants.*;

public class EnemyManager {

    private Playing playing;
    private BufferedImage[][] warriorArray;
    private BufferedImage[][] ghostArray;
    private BufferedImage[][] polarArray;
    private ArrayList<Polar> polars = new ArrayList<>();
    private ArrayList<Warrior> warriors = new ArrayList<>();
    private ArrayList<Ghost> ghosts = new ArrayList<>();

    public EnemyManager(Playing playing){
        this.playing = playing;
        loadEnemyImgs();
    }

    public void loadEnemies(Level level) {
        warriors = level.getWarriors();
        ghosts = level.getGhosts();
        polars = level.getPolars();
        //System.out.println("SIze of bears: " + bears.size());
    }

    // Nivel complet
    public void update(int[][] lvlData, Player player){ // optional player
        boolean isAnyActive = false;

        for (Ghost g : ghosts)
            if (g.isAlive()) {
                g.update(lvlData, playing);
                isAnyActive = true;
            }
        for (Warrior w : warriors)
            if (w.isAlive()) {
                w.update(lvlData, playing);
                isAnyActive = true;
           }
        for (Polar p : polars)
            if (p.isAlive()) {
                p.update(lvlData, playing);
                isAnyActive = true;
            }
        if(!isAnyActive)
            playing.setLevelCompleted(true);
    }
    public void draw(Graphics g, int xLvlOffset){
        drawWarriors(g, xLvlOffset);
        drawGhosts(g, xLvlOffset);
        drawPolars(g, xLvlOffset);
    }
    private void drawPolars(Graphics g, int xLvlOffset) {
        for (Polar p : polars)
            if (p.isAlive()) {
                g.drawImage(polarArray[p.getState()][p.getAniIndex()], (int) p.getHitBox().x - xLvlOffset - POLAR_DRAWOFFSET_X + p.flipX(), (int) p.getHitBox().y - POLAR_DRAWOFFSET_Y,
                        POLAR_WIDTH * p.flipW(), POLAR_HEIGHT, null);

                //p.drawHitBox(g, xLvlOffset);
                //p.drawAttackBox(g, xLvlOffset);
            }

    }
   private void drawWarriors(Graphics g, int xLvlOffset) {
        for (Warrior w : warriors){
            if (w.isAlive()) {
                //g.drawImage(crabbyArray[c.getEnemyState()][c.getAniIndex()], (int) c.getHitBox().x - xLvlOffset, (int) c.getHitBox().y, CRABBY_WIDTH, CRABBY_HEIGHT, null);

                g.drawImage(warriorArray[w.getState()][w.getAniIndex()], (int) w.getHitBox().x  - xLvlOffset - WARRIOR_DRAWOFFSET_X + w.flipX(), (int) w.getHitBox().y - WARRIOR_DRAWOFFSET_Y,
                        WARRIOR_WIDTH * w.flipW(), WARRIOR_HEIGHT, null);

                //w.drawHitBox(g, xLvlOffset);
                //w.drawAttackBox(g, xLvlOffset);
            }
           }

    }
    private void drawGhosts(Graphics g, int xLvlOffset) {
        for (Ghost a : ghosts)
            if (a.isAlive()) {
                g.drawImage(ghostArray[a.getState()][a.getAniIndex()], (int) a.getHitBox().x - xLvlOffset - GHOST_DRAWOFFSET_X + a.flipX(), (int) a.getHitBox().y - GHOST_DRAWOFFSET_Y, GHOST_WIDTH * a.flipW(), GHOST_HEIGHT, null);
                //a.drawHitBox(g, xLvlOffset);
            }

    }
    public void checkEnemyHit(Rectangle2D.Float attackBox){
        for (Warrior w : warriors)
            if (w.isAlive())
                if(w.currentHealth > 0)
                    if (attackBox.intersects(w.getHitBox())) {
                        w.hurt(8);
                        return;
                    }
        for (Ghost g : ghosts)
            if (g.isAlive())
                if(g.currentHealth > 0)
                    if (attackBox.intersects(g.getHitBox())) {
                        g.hurt(6);
                        return;
                    }
        for (Polar p : polars)
            if (p.isAlive())
                if(p.currentHealth > 0)
                    if (attackBox.intersects(p.getHitBox())) {
                        p.hurt(5);
                        return;
                    }
    }
    private void loadEnemyImgs() {
        polarArray = new BufferedImage[5][4];
        BufferedImage temp5 = LoadSave.GetSpriteAtlas(LoadSave.BEAR_SPRITE);
        for (int j = 0; j < polarArray.length; j++)
            for (int i = 0; i < polarArray[j].length; i++)
                polarArray[j][i] = temp5.getSubimage(i * POLAR_WIDTH_DEFAULT, j * POLAR_HEIGHT_DEFAULT, POLAR_WIDTH_DEFAULT, POLAR_HEIGHT_DEFAULT);

        warriorArray = new BufferedImage[5][8];
        BufferedImage temp2 = LoadSave.GetSpriteAtlas(LoadSave.WARRIOR_SPRITE);
        for (int j = 0; j < warriorArray.length; j++)
            for (int i = 0; i < warriorArray[j].length; i++)
                warriorArray[j][i] = temp2.getSubimage(i * WARRIOR_WIDTH_DEFAULT, j * WARRIOR_HEIGHT_DEFAULT, WARRIOR_WIDTH_DEFAULT, WARRIOR_HEIGHT_DEFAULT);

        ghostArray = new BufferedImage[5][6];
        BufferedImage temp3 = LoadSave.GetSpriteAtlas(LoadSave.GHOST_SPRITE);
        for (int j = 0; j < ghostArray.length; j++)
            for (int i = 0; i < ghostArray[j].length; i++)
                ghostArray[j][i] = temp3.getSubimage(i * GHOST_WIDTH_DEFAULT, j * GHOST_HEIGHT_DEFAULT, GHOST_WIDTH_DEFAULT, GHOST_HEIGHT_DEFAULT);

    }
    public void resetAllEnemies() {
        for (Warrior w : warriors)
            w.resetEnemy();
        for (Ghost g : ghosts)
            g.resetEnemy();
        for (Polar p : polars)
            p.resetEnemy();
    }
}
