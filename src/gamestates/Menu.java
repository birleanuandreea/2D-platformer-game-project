package gamestates;

import main.Game;
import ui.MenuButton;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

// gestionează logica meniului principal: încărcarea butoanelor și a imaginii de fundal, răspunsul la evenimentele de mouse

public class Menu extends State implements StateMethods{

    // Un array de obiecte MenuButton pentru butoanele din meniul principa
    private MenuButton[] buttons = new MenuButton[3];
    private BufferedImage backgroundImg, backgroungImgPink;

    //Coordonatele și dimensiunile meniului principal
    private int menuX, menuY, menuWidth, menuHeight;

    public Menu(Game game) {
        super(game);
        loadBackground();
        loadButtons();
        backgroungImgPink = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);
    }

    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND);
        menuWidth = (int)(backgroundImg.getWidth() * Game.SCALE);
        menuHeight = (int)(backgroundImg.getHeight() * Game.SCALE);
        menuX = Game.GAME_WIDTH / 2 - menuWidth / 2;
        menuY = (int)(45 * Game.SCALE);
    }

    private void loadButtons() {
        buttons[0] =  new MenuButton(Game.GAME_WIDTH / 2, (int)(150 * Game.SCALE), 0, Gamestate.PLAYING);
        buttons[1] =  new MenuButton(Game.GAME_WIDTH / 2, (int)(220 * Game.SCALE), 1, Gamestate.OPTIONS);
        buttons[2] =  new MenuButton(Game.GAME_WIDTH / 2, (int)(290 * Game.SCALE), 2, Gamestate.QUIT);
    }

    @Override
    public void update() {
        for(MenuButton mb : buttons){
            mb.update();
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroungImgPink, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        g.drawImage(backgroundImg, menuX, menuY, menuWidth, menuHeight, null);
        for(MenuButton mb : buttons){
            mb.draw(g);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    //Gestionează evenimentul de apăsare a butonului mouse-ului pentru activarea butoanelor

    @Override
    public void mousePressed(MouseEvent e) {
        for(MenuButton mb : buttons){
            if(isIn(e, mb)){
                mb.setMousePressed(true);
               // break;
            }
        }
    }

    //Gestionează evenimentul de eliberare a butonului mouse-ului pentru navigarea către stările de joc corespunzătoare butoanelor
    @Override
    public void mouseReleased(MouseEvent e) {
        for(MenuButton mb : buttons){
            if(isIn(e, mb)){
                if(mb.isMousePressed()){
                    mb.applyGamestate();
                }
                if(mb.getState() == Gamestate.PLAYING){

                        //Se apelează metoda loadLevel() a obiectului GameDatabase pentru a obține datele nivelului din baza de date
                        //int[] levelToLoad = game.getGameDatabase().loadLevel();

                        //System.out.println(game.getPlaying().getLevelManager().getLvlIndex());

                    //În funcție de datele primite, se determină ce nivel trebuie încărcat și se ajustează starea jucătorului
                    //if(levelToLoad[0] == 1 || levelToLoad[0] == 2){
                      //  game.getPlaying().getLevelManager().setLvlIndex(levelToLoad[0]-1);
                        //game.getPlaying().loadNextLevel();
                        //game.getPlaying().getPlayer().setCurrentHealth(levelToLoad[1]);
                    //}
                      //  if(levelToLoad[0] == 0){
                        //    game.getPlaying().getPlayer().setCurrentHealth(game.getPlaying().getPlayer().getMaxHealth());
                        //}

                    game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLvlIndex());
                }
                break;
            }
        }
        resetButtons();
    }

    //Resetază starea butoanelor după ce acestea au fost apăsate
    private void resetButtons() {
        for(MenuButton mb : buttons)
            mb.resetBools();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for(MenuButton mb : buttons)
            mb.setMouseOver(false);

        for(MenuButton mb : buttons) {
            if (isIn(e, mb)) {
                mb.setMouseOver(true);
                break;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            Gamestate.state = Gamestate.PLAYING;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
