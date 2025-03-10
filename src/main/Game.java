package main;

import audio.AudioPlayer;
import gamestates.GameOptions;
import gamestates.Gamestate;
import gamestates.Playing;
import gamestates.Menu;
import ui.AudioOptions;

import java.awt.*;

// inițializează toate componentele jocului și începe bucla principală a jocului într-un thread separat
// implementează Runnable pentru a putea rula bucla principală a jocului într-un thread separat


public class Game implements Runnable{
    private GameWindow gameWindow;

    //Panoul principal în care jocul este desenat
    private GamePanel gamePanel;

    //Firul de execuție principal al jocului
    private Thread gameThread;

    //Numărul de cadre pe secundă dorite
    private final int FPS_SET = 120;

    //Numărul de actualizări pe secundă dorite
    private final int UPS_SET = 200;

    private Playing playing;
    private Menu menu;
    private GameOptions gameOptions;
    private AudioOptions audioOptions;
    private AudioPlayer audioPlayer;
    private GameDatabase gameDatabase;


    public  final static int TILES_DEFAULT_SIZE = 32;
    public  final static float SCALE = 1.3f;
    public final static int TILES_IN_WIDTH = 26;    // visibles tiles, game screen size
    public final static int TILES_IN_HEIGHT = 14;
    public final static int TILES_SIZE = (int)(TILES_DEFAULT_SIZE * SCALE); // 48
    public final static int GAME_WIDTH = TILES_IN_WIDTH * TILES_SIZE;
    public final static int GAME_HEIGHT = TILES_IN_HEIGHT * TILES_SIZE;
    public Game(){

        //LoadSave.GetAllLevels();
        initClasses();

        gamePanel = new GamePanel(this);
        gameWindow =  new GameWindow(gamePanel);
        gamePanel.setFocusable(true);
        gamePanel.requestFocus();

        //Pornirea buclei jocului într-un fir separat
        startGameLoop();
    }

    private void initClasses() {
        gameDatabase = new GameDatabase();
        audioPlayer = new AudioPlayer();
        audioOptions = new AudioOptions(this);
        menu = new Menu(this);
        playing = new Playing(this);
        gameOptions= new GameOptions(this);
    }

    private void startGameLoop(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    //Metode de actualizare și desenare
    public void update() {

        switch(Gamestate.state){
            case MENU:
                menu.update();
                break;
            case PLAYING:
               playing.update();
                break;
            case OPTIONS:
                gameOptions.update();
                break;
            case QUIT:
            default:
                System.exit(0);
                break;
        }
    }
    public void render(Graphics g){

        switch(Gamestate.state){
            case MENU:
                menu.draw(g);
                break;
            case PLAYING:
                playing.draw(g);
                break;
            case OPTIONS:
                gameOptions.draw(g);
                break;
            default:
                break;
        }
    }

    @Override
    public void run() {


        //Calcularea timpului per cadru și per actualizare
        double timePerFrame = 1000000000.0 / FPS_SET; // nano
        double timePerUpdate = 1000000000.0 / UPS_SET;

        long previousTime = System.nanoTime();

        int frames=0;
        int updates = 0;
        long lastCheck = System.currentTimeMillis();

        double deltaU = 0;
        double deltaF = 0;


        //Rulează continuu, actualizând logica jocului și desenând cadrele la intervalele corespunzătoare
        //DeltaU și DeltaF: Contorizează timpul scurs pentru actualizări și redări, asigurând că acestea se întâmplă la intervalele corecte
        while(true){

            long currentTime = System.nanoTime();

            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if(deltaU >= 1){
                update();
                updates++;
                deltaU--;
            }

            if(deltaF >= 1){
                gamePanel.repaint();
                frames++;
                deltaF--;
            }


            if(System.currentTimeMillis() - lastCheck >= 1000){
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS:" + frames + " | UPS: " + updates);
                frames = 0;
                updates = 0;
            }
        }
    }

    public void windowFocusLost(){
        if(Gamestate.state == Gamestate.PLAYING){
            playing.getPlayer().resetDirBooleans();
        }
    }

    public Menu getMenu() {
        return menu;
    }
    public Playing getPlaying(){
        return playing;
    }
    public GameOptions getGameOptions(){ return gameOptions;}
    public AudioOptions getAudioOptions(){return audioOptions;}
    public AudioPlayer getAudioPlayer(){return audioPlayer;}
    public GameDatabase getGameDatabase(){return gameDatabase;}
}
