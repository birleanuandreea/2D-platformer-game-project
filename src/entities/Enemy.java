package entities;

import gamestates.Playing;
import main.Game;

import java.awt.geom.Rectangle2D;

import static utils.Constants.ANI_SPEED;
import static utils.Constants.Directions.*;
import static utils.Constants.EnemyConstants.*;
import static utils.Constants.GRAVITY;
import static utils.HelpMethods.*;

public abstract class Enemy extends Entity{

    protected int enemyType;
    protected boolean firstUpdate = true;
    protected int walkDir = LEFT;
    protected int tileY;
    protected boolean alive = true;
    protected boolean attackChecked = false;
    protected float attackDistance = Game.TILES_SIZE;
    public Enemy(float x, float y, int width, int height, int enemyType){
        super(x, y, width, height);
        this.enemyType = enemyType;
        maxHealth = GetMaxHealth(enemyType);
        currentHealth = maxHealth;
        walkSpeed = Game.SCALE * 0.35f;
    }
    protected void updateAttackBox() {
        attackBox.x = hitBox.x;
        attackBox.y = hitBox.y;
    }

    protected void updateAttackBoxFlip() {
        if (walkDir == RIGHT)
            attackBox.x = hitBox.x + hitBox.width;
        else
            attackBox.x = hitBox.x;

        attackBox.y = hitBox.y;
    }

    protected void initAttackBox(int w, int h) {
        attackBox = new Rectangle2D.Float(x, y, (int) (w * Game.SCALE), (int) (h * Game.SCALE));
    }

    //verifică dacă inamicul este pe sol la prima actualizare
    protected void firstUpdateCheck(int[][] lvlData){
        if (!IsEntityOnFloor(hitBox, lvlData)) {
            inAir = true;
        }
        //e setează la false, indicând că prima actualizare a fost efectuată
        firstUpdate = false;

    }

    //Această metodă verifică starea inamicului în aer și, dacă nu este lovită (HIT) sau moartă (DEAD), actualizează poziția în aer
    protected void inAirChecks(int[][] lvlData, Playing playing) {
        if (state != HIT && state != DEAD) {
            updateInAir(lvlData);
        }
    }

    //Această metodă gestionează mișcarea inamicului în aer, actualizând poziția pe baza gravitației și verificând coliziunile
    protected void updateInAir(int[][] lvlData){
        if (CanMoveHere(hitBox.x, hitBox.y + airSpeed, hitBox.width, hitBox.height, lvlData)) {
            hitBox.y += airSpeed;
            airSpeed += GRAVITY;    //Reprezintă viteza cu care inamicul cade, influențată de gravitație
        } else {
            inAir = false;
            //Dacă inamicul nu se poate mișca la noua poziție, ajustează poziția pe axa Y pentru a fi corectă (sub acoperiș sau deasupra podelei)
            hitBox.y = GetEntityYPosUnderRoofOrAboveFloor(hitBox, airSpeed);
            tileY = (int) (hitBox.y / Game.TILES_SIZE);
        }
    }

    //Această metodă gestionează mișcarea entității pe axa X, verificând coliziunile și schimbând direcția de mers dacă este necesar
    protected void move(int[][] lvlData) {
        float xSpeed = 0;

        if (walkDir == LEFT)
            xSpeed = -walkSpeed;
        else
            xSpeed = walkSpeed;

        if (CanMoveHere(hitBox.x + xSpeed, hitBox.y, hitBox.width, hitBox.height, lvlData))
            if (IsFloor(hitBox, xSpeed, lvlData)) {
                hitBox.x += xSpeed;
                return;
            }

        changeWalkDir();
    }
    protected void updateAnimationTick() {
        aniTick++;
        if (aniTick >= ANI_SPEED) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(enemyType, state)) {
                aniIndex = 0;

                if (enemyType == POLAR || enemyType == WARRIOR || enemyType == GHOST) {
                    switch (state) {
                        case ATTACK, HIT -> state = IDLE;
                        case DEAD -> alive = false;
                    }
                }
            }
        }
    }

    private void changeWalkDir() {
        if (walkDir == LEFT)
            walkDir = RIGHT;
        else
            walkDir = LEFT;

    }

    public void hurt(int amount){
        currentHealth -= amount;
        if(currentHealth <= 0){
            newState(DEAD);
        }
        else {
            newState(HIT);
            if (walkDir == LEFT)
                pushBackDir = RIGHT;
            else
                pushBackDir = LEFT;

            pushBackOffsetDir = UP;
            pushDrawOffset = 0;
        }

    }
    public void checkPlayerHit(Rectangle2D.Float attackBox, Player player){
        if (attackBox.intersects(player.hitBox)){
            player.changeHealth(-GetEnemyDmg(enemyType), this);
        }
        attackChecked = true;
    }

    //Această metodă schimbă direcția de mers a inamicului pentru a se îndrepta către jucător
    protected void turnTowardsPlayer(Player player) {
        if (player.hitBox.x > hitBox.x)
            walkDir = RIGHT;
        else
            walkDir = LEFT;
    }


    //Această metodă verifică dacă inamicul poate vedea jucătorul în baza datelor nivelului și poziției jucătorului
    protected boolean canSeePlayer(int[][] lvlData, Player player) {
        //Verifică dacă jucătorul este pe același nivel (tile) pe axa Y ca și inamicul
        int playerTileY = (int) (player.getHitBox().y / Game.TILES_SIZE);
        if (playerTileY == tileY)
            if (isPlayerInRange(player)) {
                //Verifică dacă linia de vedere dintre inamic și jucător este liberă de obstacole în datele nivelului (lvlData)
                if (IsSightClear(lvlData, hitBox, player.hitBox, tileY))
                    return true;
            }

        return false;
    }

    //verifică dacă jucătorul este în raza de atac a inamicului
    protected boolean isPlayerInRange(Player player) {
        //Calculează distanța absolută pe axa X între jucător și inamic
        int absValue = (int) Math.abs(player.hitBox.x - hitBox.x);
        return absValue <= attackDistance * 5;
    }

    //verifică dacă jucătorul este suficient de aproape pentru a fi atacat de inamic
    protected boolean isPlayerCloseForAttack(Player player) {
        int absValue = (int) Math.abs(player.hitBox.x - hitBox.x);
        return absValue <= attackDistance;  //indicând că jucătorul este suficient de aproape pentru a fi atacat
    }

    public int getEnemyState(){
        return state;
    }
    public boolean isAlive(){return alive;}

    public void resetEnemy() {
        hitBox.x = x;
        hitBox.y = y;
        firstUpdate = true;
        currentHealth = maxHealth;
        newState(IDLE);
        alive = true;
        airSpeed = 0;

        pushDrawOffset = 0;
    }
    public float getPushDrawOffset() {
        return pushDrawOffset;
    }

}
