
package entities;

import static utils.Constants.Directions.RIGHT;
import static utils.Constants.EnemyConstants.*;
import static utils.HelpMethods.IsFloor;
import gamestates.Playing;


public class Polar extends Enemy {

    public Polar(float x, float y) {
        super(x, y, POLAR_WIDTH, POLAR_HEIGHT, POLAR);
        initHitBox(34, 19);
        initAttackBox(14, 21);
    }


    public void update(int[][] lvlData, Playing playing) {
        updateBehavior(lvlData, playing);
        updateAnimationTick();
        updateAttackBoxFlip();
    }

    //gestionează comportamentul inamicului în joc în funcție de diferite stări și condiții
    private void updateBehavior(int[][] lvlData, Playing playing) {
        //Dacă este prima actualizare, se face o verificare inițială pentru a vedea dacă inamicul este pe sol (firstUpdateCheck), după care firstUpdate este setat la false
        if (firstUpdate)
            firstUpdateCheck(lvlData);

        if (inAir)
            inAirChecks(lvlData, playing);
        else {
            switch (state) {
                case IDLE:
                    if (IsFloor(hitBox, lvlData))
                        newState(RUNNING);
                    else
                        // setează inAir la true
                        inAir = true;
                    break;
                case RUNNING:
                    if (canSeePlayer(lvlData, playing.getPlayer())) {
                        turnTowardsPlayer(playing.getPlayer());
                        //Dacă jucătorul este suficient de aproape pentru un atac
                        if (isPlayerCloseForAttack(playing.getPlayer()))
                            newState(ATTACK);
                    }

                    move(lvlData);
                    break;
                case ATTACK:
                    if (aniIndex == 0)
                        attackChecked = false;
                    if (aniIndex == 1 && !attackChecked)    // 2
                        checkPlayerHit(attackBox, playing.getPlayer());
                    break;
                case HIT:
                    if (aniIndex <= GetSpriteAmount(enemyType, state) - 2)
                        pushBack(pushBackDir, lvlData, 1f);
                    //Actualizează decalajul de desenare a împingerii
                    updatePushBackDrawOffset();
                    break;
            }
        }
    }
    public int flipX() {
        if (walkDir == RIGHT)
            return width;
        else
            return 0;   //sprite-ul va fi desenat de la stânga spre dreapta
    }

    public int flipW() {
        if (walkDir == RIGHT)
            return -1;
        else
            return 1;
    }

}
