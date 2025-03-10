

package entities;

import static utils.Constants.Directions.RIGHT;
import static utils.Constants.EnemyConstants.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;

import static utils.Constants.Directions.*;
import static utils.HelpMethods.CanMoveHere;
import static utils.HelpMethods.IsFloor;

import gamestates.Playing;
import main.Game;

public class Warrior extends Enemy {

    public Warrior(float x, float y) {
        super(x, y, WARRIOR_WIDTH, WARRIOR_HEIGHT, WARRIOR);
        initHitBox(71, 30); // 24  30
        initAttackBox(16, 19);
    }


    public void update(int[][] lvlData, Playing playing) {
        updateBehavior(lvlData, playing);
        updateAnimationTick();
        updateAttackBoxFlip();
    }
    private void updateBehavior(int[][] lvlData, Playing playing) {  //Player player
        if (firstUpdate)
            firstUpdateCheck(lvlData);

        if (inAir)
            inAirChecks(lvlData, playing);
            //updateInAir(lvlData);
        else {
            switch (state) {
                case IDLE:
                    if (IsFloor(hitBox, lvlData))
                        newState(RUNNING);
                    else
                        inAir = true;
                    break;
                case RUNNING:
                    if (canSeePlayer(lvlData, playing.getPlayer())) {
                        turnTowardsPlayer(playing.getPlayer());
                        if (isPlayerCloseForAttack(playing.getPlayer()))
                            newState(ATTACK);
                    }
                    move(lvlData);
                    break;
                case ATTACK:
                    if (aniIndex == 0)
                        attackChecked = false;
                    if (aniIndex == 2 && !attackChecked)
                        checkPlayerHit(attackBox, playing.getPlayer());
                    break;
                case HIT:
                    if (aniIndex <= GetSpriteAmount(enemyType, state) - 2)
                        pushBack(pushBackDir, lvlData, 1f);
                    updatePushBackDrawOffset();
                    break;
            }
        }
    }

    public int flipX() {
        if (walkDir == RIGHT)
            return width;
        else
            return 0;
    }

    public int flipW() {
        if (walkDir == RIGHT)
            return -1;
        else
            return 1;
    }
}


