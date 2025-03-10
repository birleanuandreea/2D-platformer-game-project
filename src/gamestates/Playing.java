package gamestates;

import Objects.ObjectManager;
import entities.EnemyManager;
import entities.Player;
import levels.InvalidLevelException;
import levels.LevelManager;
import main.Game;
import ui.GameOverOverlay;
import ui.LevelCompletedOverlay;
import ui.PauseOverlay;
import utils.LoadSave;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import static utils.Constants.Environment.*;

// integrează diferite aspecte ale jocului și coordonează funcționalitățile acestuia în timpul jocului

public class Playing extends State implements StateMethods {

    private Player player;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private ObjectManager objectManager;

    //suprapunerile care sunt afișate în diverse situații
    private GameOverOverlay gameOverOverlay;
    private LevelCompletedOverlay levelCompletedOverlay;
    private PauseOverlay pauseOverlay;
    private boolean paused = false;


    //folosită pentru a urmări offsetul orizontal al nivelului în raport cu ecranul
    private int xLvlOffset;
    private int leftBorder = (int) (0.2 * Game.GAME_WIDTH);
    private int rightBorder = (int) (0.2 * Game.GAME_WIDTH);

    /*private int lvlTilesWide = LoadSave.GetLevelData()[0].length;   // 50
    private int maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;    // 50 - 26 = 14*/
    private int maxLvlOffsetX;


    private BufferedImage backgroundImg, bigmountain, smallmountain, clouds;

    private boolean gameOver;// = false;
    private boolean lvlCompleted;
    private boolean playerDying;

    public Playing(Game game) {
        super(game);
        initClasses();

        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BACKGROUND_IMG);
        bigmountain = LoadSave.GetSpriteAtlas(LoadSave.BIG_MOUNTAIN);
        clouds = LoadSave.GetSpriteAtlas(LoadSave.CLOUDS);

        calcLvlOffset();
        loadStartLevel();
    }
    public void  loadNextLevel(){
        levelManager.loadNextLevel();
        resetAll();
    }

    private void loadStartLevel() {
        enemyManager.loadEnemies(levelManager.getCurrentLevel());
        objectManager.loadObjects(levelManager.getCurrentLevel());
    }

    private void calcLvlOffset() {
        maxLvlOffsetX = levelManager.getCurrentLevel().getLvlOffset();
    }

    private void initClasses() {

        levelManager = new LevelManager(game);
        enemyManager = new EnemyManager(this);
        objectManager = new ObjectManager(this);
        player = new Player(200, 200, (int) (56 * Game.SCALE), (int) (56 * Game.SCALE), this);  // constructor privat, apelat prin get, worldX =100, worldY=100
        player.loadLvlData(levelManager.getCurrentLevel().getLevelData());

        pauseOverlay = new PauseOverlay(this);
        gameOverOverlay = new GameOverOverlay(this);
        levelCompletedOverlay = new LevelCompletedOverlay(this);
    }

    //apelată în fiecare cadru și este responsabilă pentru actualizarea stării jocului
    @Override
    public void update() {
        if(paused){
            pauseOverlay.update();
        }else if(lvlCompleted){
            levelCompletedOverlay.update();
        }else if(gameOver){
            gameOverOverlay.update();
        }else if(playerDying){
            player.update();
        }else{
            levelManager.update();
            objectManager.update();
            player.update();
            enemyManager.update(levelManager.getCurrentLevel().getLevelData(), player);
            checkCloseToBorder();
        }
    }

    // asigură că jucătorul rămâne întotdeauna vizibil pe ecran și că nu poate ieși în afara marginilor nivelului
    private void checkCloseToBorder() {

        //poziția actuală a acestuia pe ecran
        int playerX = (int) player.getHitBox().x;
       //diferență reprezintă distanța dintre marginea stângă a ecranului și marginea stângă a hitbox-ului jucătorului
        int diff = playerX - xLvlOffset;

        //xLvlOffset este crescut pentru a deplasa nivelul spre stânga și pentru a menține jucătorul în centrul ecranului
        if (diff > rightBorder) {
            xLvlOffset += diff - rightBorder;
        }
        //xLvlOffset este scăzut pentru a deplasa nivelul spre dreapta și pentru a menține jucătorul în centrul ecranului
        else if (diff < leftBorder) {
            xLvlOffset += diff - leftBorder;
        }

        //xLvlOffset este setat la valoarea maximă permisă
        if (xLvlOffset > maxLvlOffsetX) {
            xLvlOffset = maxLvlOffsetX;
        } else if (xLvlOffset < 0) {
            xLvlOffset = 0;
        }
    }


    @Override
    public void draw(Graphics g) {

        g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        //drawSmallMountain(g);
        drawBigMountain(g);
        drawClouds(g);

        levelManager.draw(g, xLvlOffset);
        player.render(g, xLvlOffset);
        enemyManager.draw(g, xLvlOffset);
        objectManager.draw(g, xLvlOffset);

        if (paused) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
            pauseOverlay.draw(g);
        } else if (gameOver)
            gameOverOverlay.draw(g);
        else if (lvlCompleted){
            levelCompletedOverlay.draw(g);
        }

    }

    private void drawClouds(Graphics g) {
        for (int i = 0; i < 2; i++)
            g.drawImage(clouds, i * CLOUDS_WIDTH, (int) (100 * Game.SCALE), CLOUDS_WIDTH, CLOUDS_HEIGHT, null);
    }

    private void drawBigMountain(Graphics g) {

        for (int i = 0; i < 2; i++)
            g.drawImage(bigmountain, i * BIG_MOUNTAIN_WIDTH, (int) (100 * Game.SCALE), BIG_MOUNTAIN_WIDTH, BIG_MOUNTAIN_HEIGHT, null);
    }

    /*private void drawSmallMountain(Graphics g) {

        for (int i = 0; i < 3; i++)
            g.drawImage(smallmountain, i * SMALL_MOUNTAIN_WIDTH, (int) (220 * Game.SCALE), SMALL_MOUNTAIN_WIDTH, SMALL_MOUNTAIN_HEIGHT, null);
    }*/

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!gameOver) {
            if(e.getButton() == MouseEvent.BUTTON3)
                player.powerAttack();
            }
        }
    public void mouseDragged(MouseEvent e) {
        if (!gameOver)
            if (paused)
                pauseOverlay.mouseDragged(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!gameOver) {
            if (paused)
                pauseOverlay.mousePressed(e);
            else if (lvlCompleted)
                levelCompletedOverlay.mousePressed(e);
        }else
            gameOverOverlay.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!gameOver) {
            if (paused)
                pauseOverlay.mouseReleased(e);
            else if (lvlCompleted)
                levelCompletedOverlay.mouseReleased(e);
        }else
            gameOverOverlay.mouseReleased(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!gameOver) {
            if (paused)
                pauseOverlay.mouseMoved(e);
            else if (lvlCompleted)
                levelCompletedOverlay.mouseMoved(e);
        }else
            gameOverOverlay.mouseMoved(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver)
            gameOverOverlay.keyPressed(e);
        else {

            switch (e.getKeyCode()) {
                case KeyEvent.VK_A:
                    player.setLeft(true);
                    break;
                case KeyEvent.VK_D:
                    player.setRight(true);
                    break;
                case KeyEvent.VK_ENTER:
                    player.setAttacking(true);
                    break;
                case KeyEvent.VK_SPACE:
                    player.setJump(true);
                    break;
               /* case KeyEvent.VK_BACK_SPACE:
                    Gamestate.state = Gamestate.MENU;
                    break;*/
                case KeyEvent.VK_BACK_SPACE:
                    paused = !paused;
                    break;
                case KeyEvent.VK_S:
                    //salvare joc

                    int tmp = player.getMaxHealth() - player.getCurrentHealth();
                    int currentHealth = player.getMaxHealth() - tmp;
                    player.setCurrentHealth(currentHealth);

                    game.getGameDatabase().saveGameData(levelManager.getLvlIndex(), (int)player.getHitBox().x, (int)player.getHitBox().y, currentHealth);
                case KeyEvent.VK_L:
                    //Incarcare joc
                    int[] data = game.getGameDatabase().loadGameData();
                    int loadLvlIndex = data[0];
                    int playerX = data[1];
                    int playerY = data[2];
                    int health = data[3];

                    //Setare nivel si pozitia jucatorului
                    try {
                        levelManager.setCurrentLevel(loadLvlIndex);
                    } catch (InvalidLevelException ex) {
                        System.out.println("Invalid level index");
                        //throw new RuntimeException(ex);
                    }
                    levelManager.importOutsideSprites();
                    player.getHitBox().x = playerX;
                    player.getHitBox().y = playerY;
                    player.setCurrentHealth(health);
                    player.loadLvlData(levelManager.getCurrentLevel().getLevelData());
                    calcLvlOffset();
                    System.out.println("Game loaded!");
                    break;
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!gameOver) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A:
                    player.setLeft(false);
                    break;
                case KeyEvent.VK_D:
                    player.setRight(false);
                    break;
                case KeyEvent.VK_ENTER:
                    player.setAttacking(false);
                    break;
                case KeyEvent.VK_SPACE:
                    player.setJump(false);
                    break;
            }
        }
    }

    public void setMaxLvlOffset(int lvlOffset){
        this.maxLvlOffsetX = lvlOffset;
    }

    public void unpauseGame() {
        paused = false;
    }

    public void windowFocusLost() {
        player.resetDirBooleans();
    }

    public Player getPlayer() {
        return player;
    }


    public void resetAll() {
        gameOver = false;
        paused = false;
        lvlCompleted = false;
        playerDying = false;
        player.resetAll();
        enemyManager.resetAllEnemies();
        objectManager.resetAllObjects();

    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        enemyManager.checkEnemyHit(attackBox);
    }

   public void checkPotionTouched(Rectangle2D.Float hitBox) {
        objectManager.checkObjectTouched(hitBox);
    }

    public void checkObjectHit(Rectangle2D.Float attackBox) {
        objectManager.checkObjectHit(attackBox);
    }
    public void checkSpikesTouched(Player player) {
        objectManager.checkSpikesTouched(player);
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
    public EnemyManager getEnemyManager(){
        return enemyManager;
    }
    public ObjectManager getObjectManager(){
        return objectManager;
    }
    public void setLevelCompleted(boolean levelCompleted){
        this.lvlCompleted = levelCompleted;
        if(levelCompleted)
            game.getAudioPlayer().lvlCompleted();
    }
    public LevelManager getLevelManager(){return levelManager;}


    public void setPlayerDying(boolean playerDying) {
        this.playerDying = playerDying;
    }
}
