package entities;

import audio.AudioPlayer;
import gamestates.Playing;
import main.Game;
import utils.LoadSave;


import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static utils.Constants.ANI_SPEED;
import static utils.Constants.Directions.*;
import static utils.Constants.GRAVITY;
import static utils.Constants.PlayerConstants.*;
import static utils.Constants.PlayerConstants.GetSpriteAmount;
import static utils.HelpMethods.*;

public class Player extends Entity{

    private BufferedImage[][] animations;
    private boolean left,  right,  jump;
    private boolean moving = false, attacking = false;
    private int[][] lvlData;
    private float xDrawOffset = 17 * Game.SCALE;
    private float yDrawOffset = 24 * Game.SCALE;
    private float jumpSpeed = -2.25f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE;

    private int flipX = 0;
    private int flipW = 1;
    private int tileY = 0;

    // StatusBarUI
    private BufferedImage statusBarImg;
    private int statusBarWidth = (int) (192 * Game.SCALE);
    private int statusBarHeight = (int) (58 * Game.SCALE);

    // coordonatele x si y ale barei de stare pe ecran
    private int statusBarX = (int) (10 * Game.SCALE);
    private int statusBarY = (int) (10 * Game.SCALE);
    private int healthBarWidth = (int) (150 * Game.SCALE);
    private int healthBarHeight = (int) (4 * Game.SCALE);

    //Coordonatele x și y ale începutului barei de stare pentru viață pe ecran
    private int healthBarXStart = (int) (34 * Game.SCALE);
    private int healthBarYStart = (int) (14 * Game.SCALE);
    private int powerBarWidth = (int) (104 * Game.SCALE);
    private int powerBarHeight = (int) (2 * Game.SCALE);
    private int powerBarXStart = (int) (44 * Game.SCALE);
    private int powerBarYStart = (int) (34 * Game.SCALE);
    private int powerWidth = powerBarWidth;
    private int powerMaxValue = 200;
    private int powerValue = powerMaxValue;
    private int healthWidth = healthBarWidth;



    private Playing playing;
    private boolean attackCheck;
    private boolean powerAttackActive;

    //contor pentru a urmări timpul scurs de la activarea atacului special
    private int powerAttackTick;

    //Viteza cu care puterea jucătorului crește atunci când este activat atacul special
    private int powerGrowSpeed = 15;

    //contor pentru a urmări timpul și a gestiona creșterea puterii în timpul atacului special
    private int powerGrowTick;
    public Player(float x, float y, int width, int height, Playing playing){
        super(x, y, width, height);
        this.playing = playing;
        this.state = IDLE;
        this.maxHealth = 150;
        this.currentHealth = maxHealth;
        this.walkSpeed = Game.SCALE * 1.0f;
        loadAnimations();
        initHitBox(20,28);
        initAttackBox();
    }
    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int)(17 * Game.SCALE), (int)(10 * Game.SCALE));    // 20
        resetAttackBox();
    }

    public void update(){

        //Actualizează starea barelor de sănătate și putere
        updateHealthBar();
        updatePowerBar();

        //Gestionarea stării de moarte
        if(currentHealth <= 0){
            if(state != DEAD){
                state = DEAD;
                aniTick = 0;
                aniIndex = 0;
                playing.setPlayerDying(true);
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.DIE);

                // Check if player died in air
                if (!IsEntityOnFloor(hitBox, lvlData)) {
                    inAir = true;
                    airSpeed = 0;
                }
            }
            //Check the last sprite and the last aniTick, animația de moarte s-a încheiat
            else if(aniIndex == GetSpriteAmount(DEAD) - 1 && aniTick >= ANI_SPEED - 1){
                playing.setGameOver(true);
                playing.getGame().getAudioPlayer().stopSong();
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.GAMEOVER);
            }else{

                //n caz contrar, se actualizează animația și, dacă jucătorul este în aer, se verifică dacă poate să cadă și se actualizează poziția pe axa Y

                updateAnimationTick();


                // Fall if in air
                if (inAir)
                    if (CanMoveHere(hitBox.x, hitBox.y + airSpeed, hitBox.width, hitBox.height, lvlData)) {
                        hitBox.y += airSpeed;
                        airSpeed += GRAVITY;
                    } else
                        inAir = false;
            }
            return;
        }

        updateAttackBox();

        if (state == HIT) {
            if (aniIndex <= GetSpriteAmount(state) - 2)
                pushBack(pushBackDir, lvlData, 1.25f);
            updatePushBackDrawOffset();
        } else
            updatePos();


        if(moving){
            checkPotionTouched();
            checkSpikesTouched();
            tileY = (int) (hitBox.y / Game.TILES_SIZE);
            if (powerAttackActive) {
                powerAttackTick++;
                //se resetează atacul puternic după un anumit timp
                if (powerAttackTick >= 35) {
                    powerAttackTick = 0;
                    powerAttackActive = false;
                }
            }
        }
        if(attacking || powerAttackActive)
            checkAttack();

        updateAnimationTick();
        setAnimation();
    }
    private void updatePos() {

        moving = false;
        if(jump){
            jump(); //starea de săritură
        }
        if(!inAir)
            if (!powerAttackActive)
                if((!left && !right) || (right && left))
                    return; // jucătorul nu se mișcă

        //Calcularea vitezei orizontale
        float xSpeed = 0;

        if(left && !right){
            xSpeed -= walkSpeed;
            flipX = width;
            flipW = -1;
        }
        if(right && !left){
            xSpeed += walkSpeed;
            flipX = 0;
            flipW = 1;
        }


        //Gestionarea vitezei în timpul atacului puternic
        if (powerAttackActive) {
            if ((!left && !right) || (left && right)) {
                if (flipW == -1)
                    xSpeed = -walkSpeed;
                else
                    xSpeed = walkSpeed;
            }
            xSpeed *= 2;
        }


        //Verificarea stării de aer și actualizarea poziției
        if(!inAir){
            if(!IsEntityOnFloor(hitBox, lvlData)){
                inAir = true;   // daca n-i pe podea se seteaza la true
            }
        }
        if(inAir && !powerAttackActive){
            //Se verifică dacă jucătorul poate să se miște în jos fără coliziuni
            if(CanMoveHere(hitBox.x, hitBox.y + airSpeed, hitBox.width, hitBox.height, lvlData)){
                hitBox.y += airSpeed;
                airSpeed += GRAVITY;
                updateXPos(xSpeed);
            }else{
                hitBox.y = GetEntityYPosUnderRoofOrAboveFloor(hitBox, airSpeed);
                if(airSpeed > 0){   // daca cade
                    resetInAir();// going down, resetare stare de aer
                }
                else{
                    //Dacă jucătorul se deplasează în sus, se setează airSpeed la o viteză de cădere predefinită
                    airSpeed = fallSpeedAfterCollision;
                }
                updateXPos(xSpeed);
            }
        }else{
            //Dacă jucătorul nu este în aer, se actualizează direct poziția pe axa X
            updateXPos(xSpeed);
        }
        //Setarea stării de mișcare
        moving = true;

    }

    private void jump() {
        if(inAir){
            return;
        }
        playing.getGame().getAudioPlayer().playEffect(AudioPlayer.JUMP);
        inAir = true;
        airSpeed = jumpSpeed;   //jucătorul începe să se miște în sus, simulând efectul de săritură
    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    //gestionarea mișcării orizontale a jucătorului și pentru tratarea coliziunilor cu obstacolele din mediul de joc
    private void updateXPos(float xSpeed) {
        if(CanMoveHere(hitBox.x + xSpeed,hitBox.y,hitBox.width,hitBox.height,lvlData)){
            hitBox.x += xSpeed;
        }else{
            hitBox.x = GetEntityXPosNextToWall(hitBox, xSpeed);
            if(powerAttackActive){
                powerAttackActive = false;
                powerAttackTick = 0;
            }
        }
    }

    private void updateHealthBar() {
        healthWidth = (int)((currentHealth / (float)(maxHealth)) * healthBarWidth);
    }

    //actualizarea continuă a barei de putere și creșterea acesteia pe măsură ce jucătorul acumulează putere în joc
    private void updatePowerBar() {
        powerWidth = (int) ((powerValue / (float) powerMaxValue) * powerBarWidth);

        powerGrowTick++;
        if (powerGrowTick >= powerGrowSpeed) {
            powerGrowTick = 0;
            changePower(1);
        }
    }

    //
    public void changeHealth(int value) {
        if (value < 0) {    // pierde sanatate
            if (state == HIT)
                return;
            else
                newState(HIT);
        }

        currentHealth += value;

        //Se folosește Math.max(Math.min(currentHealth, maxHealth), 0) pentru a asigura că sănătatea jucătorului rămâne între 0 și valoarea maximă a sănătății
        currentHealth = Math.max(Math.min(currentHealth, maxHealth), 0);
    }
    public void changeHealth(int value, Enemy e) {
        if (state == HIT)
            return;
        changeHealth(value);
        pushBackOffsetDir = UP;
        pushDrawOffset = 0;

        if (e.getHitBox().x < hitBox.x)
            pushBackDir = RIGHT;
        else
            pushBackDir = LEFT;
    }
    public void changePower(int value) {
        powerValue += value;
        if (powerValue >= powerMaxValue)
            powerValue = powerMaxValue;
        else if (powerValue <= 0)
            powerValue = 0;
    }
    public void powerAttack() {
        // nu sunt declanșate mai multe atacuri speciale simultan
        if (powerAttackActive)
            return;
        if (powerValue >= 100) { // 60 powers to do a power attack
            powerAttackActive = true;
            changePower(-100);
        }

    }
    private void checkSpikesTouched() {
        playing.checkSpikesTouched(this);
    }

    private void checkPotionTouched() {
        playing.checkPotionTouched(hitBox);
    }

    private void checkAttack() {
        if(attackCheck || aniIndex != 3){   // 1
            return;
        }

        //verificarea atacului este în desfășurare
        attackCheck = true;

        //pentru a evita verificarea atacului în timpul activării atacului special
        if(powerAttackActive)
            attackCheck = false;

        playing.checkEnemyHit(attackBox);
        playing.checkObjectHit(attackBox);
        playing.getGame().getAudioPlayer().playAttackSound();
    }



    public void render(Graphics g, int lvlOffset){

        g.drawImage(animations[state][aniIndex], (int)(hitBox.x - xDrawOffset) - lvlOffset + flipX, (int)(hitBox.y - yDrawOffset), width * flipW, height, null);
        //drawHitBox(g, lvlOffset);
        //drawAttackBox(g, lvlOffset);
        drawUI(g);
    }


    private void drawUI(Graphics g) {
        // Background ui
        g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);

        // Health bar
        g.setColor(Color.red);
        g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);

        // Power Bar
        g.setColor(Color.yellow);
        g.fillRect(powerBarXStart + statusBarX, powerBarYStart + statusBarY, powerWidth, powerBarHeight);
    }


    public void kill() {
        currentHealth = 0;
    }

    private void setAnimation() {
        int startAni = state;

        if (state == HIT)
            return;

        if(moving){
            state = RUNNING;
        }
        else{
            state = IDLE;
        }

        if(inAir){
            if(airSpeed < 0){
                state = JUMP;
            }
            else{
                state = FALLING;
            }
        }

        if (powerAttackActive) {
            state = ATTACK;
            aniIndex = 3;
            aniTick = 0;
            return;
        }

        if(attacking){
            state = ATTACK;
            if(startAni != ATTACK){
                aniIndex = 3;   // 1
                aniTick = 0;
                return;
            }
        }
        if(startAni != state){
            resetAniTick();
        }
    }


    private void setAttackBoxOnRightSide() {
        attackBox.x = hitBox.x + hitBox.width - (int) (Game.SCALE * 5);
    }

    private void setAttackBoxOnLeftSide() {
        attackBox.x = hitBox.x - hitBox.width - (int) (Game.SCALE * 10);
    }

    private void updateAttackBox() {
        if (right && left) {
            if (flipW == 1) {
                setAttackBoxOnRightSide();
            } else {
                setAttackBoxOnLeftSide();
            }

        } else if (right || (powerAttackActive && flipW == 1))
            setAttackBoxOnRightSide();
        else if (left || (powerAttackActive && flipW == -1))
            setAttackBoxOnLeftSide();

        attackBox.y = hitBox.y + (Game.SCALE * 10);
    }
    private void resetAttackBox() {
        if (flipW == 1)
            setAttackBoxOnRightSide();
        else
            setAttackBoxOnLeftSide();
    }
    private void loadAnimations() {

        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS_1);

        animations = new BufferedImage[11][8];
        for (int i = 0; i < animations.length; i++) {
            for (int j = 0; j < animations[i].length; j++) {
                animations[i][j] = img.getSubimage(j * 56, i * 56, 56, 56);
            }
        }

        statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
    }
    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= ANI_SPEED) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(state)) {
                aniIndex = 0;
                attacking = false;
                attackCheck = false;
                if (state == HIT) {
                    newState(IDLE);
                    airSpeed = 0f;
                    if (!IsFloor(hitBox, 0, lvlData))
                        inAir = true;
                }
            }
        }
    }

    public void loadLvlData(int[][] lvlData){
        this.lvlData = lvlData;

        if(!IsEntityOnFloor(hitBox, lvlData)){
            inAir = true;
        }
    }
    private void resetAniTick() {
        aniTick = 0;
        aniIndex = 0;
    }
    public void resetDirBooleans() {
        left = false;
        right = false;
    }

    public void resetAll() {
        resetDirBooleans();
        inAir = false;
        attacking = false;
        moving = false;
        airSpeed = 0f;
        state = IDLE;
        currentHealth = maxHealth;
        powerAttackActive = false;
        powerAttackTick = 0;
        powerValue = powerMaxValue;

        hitBox.x = x;
        hitBox.y = y;
        resetAttackBox();

        if (!IsEntityOnFloor(hitBox, lvlData))
            inAir = true;
    }
    public void  setAttacking(boolean attacking){
        this.attacking = attacking;
    }
    public void setJump(boolean jump){
        this.jump = jump;
    }
    public void setLeft(boolean left){
        this.left = left;
    }
    public boolean isLeft(){
        return left;
    }
    public void setRight(boolean right){
        this.right = right;
    }
    public boolean isRight(){
        return right;
    }
    public int getCurrentHealth(){return currentHealth;}
    public void setCurrentHealth(int currentHealth){this.currentHealth = currentHealth;}

    public int getMaxHealth(){return maxHealth;}

}

