package entities;

import static utils.Constants.Directions.RIGHT;
import static utils.Constants.EnemyConstants.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;

import static utils.Constants.Directions.*;
import static utils.HelpMethods.IsFloor;

import gamestates.Playing;
import main.Game;

public class Ghost extends Enemy {

    public Ghost(float x, float y) {
        super(x, y, GHOST_WIDTH, GHOST_HEIGHT, GHOST);
        initHitBox(28, 25);
    }


    public void update(int[][] lvlData, Playing playing) {
        updateBehavior(lvlData,playing);
        updateAnimationTick();
    }

private void updateBehavior(int[][] lvlData, Playing playing) {  //
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
                if (aniIndex == 1 && !attackChecked)
                    checkPlayerHit(hitBox, playing.getPlayer());
                break;
            case HIT:
                if (aniIndex <= GetSpriteAmount(enemyType, state) - 2)
                    pushBack(pushBackDir, lvlData, 2f);
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
