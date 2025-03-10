package ui;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utils.Constants.UI.URMButtons.URM_SIZE;

// afișează un ecran care apare atunci când jucătorul pierde jocul, conține două butoane:
// unul pentru areveni la meniul principal și altul pentru a începe un nou joc

public class GameOverOverlay {
    private Playing playing;
    private BufferedImage img;

    //Coordonatele și dimensiunile imaginii de fundal
    private int imgX, imgY, imgW, imgH;

    // butoanele pentru revenirea la meniul principal și pentru a începe un nou joc
    private  UrmButton menu, play;
    public GameOverOverlay(Playing playing){
        this.playing = playing;
        createImg();
        createButtons();
    }

    private void createButtons() {
        int menuX = (int) (335 * Game.SCALE);
        int playX = (int) (440 * Game.SCALE);
        int y = (int) (195 * Game.SCALE);
        // tipul de buton ales din imagine
        play = new UrmButton(playX, y, URM_SIZE, URM_SIZE, 0);
        menu = new UrmButton(menuX, y, URM_SIZE, URM_SIZE, 2);
    }

    private void createImg() {
        img = LoadSave.GetSpriteAtlas(LoadSave.DEATH_SCREEN);
        imgW = (int) (img.getWidth() * Game.SCALE);
        imgH = (int) (img.getHeight() * Game.SCALE);
        imgX = Game.GAME_WIDTH / 2 - imgW / 2;  // in the middle
        imgY = (int) (100 * Game.SCALE);
    }

    public void draw(Graphics g) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        g.drawImage(img, imgX, imgY, imgW, imgH, null);

        menu.draw(g);
        play.draw(g);

        /*g.setColor(Color.white);
        g.drawString("Game Over", Game.GAME_WIDTH / 2, 150);
        g.drawString("Press enter to enter Main Menu!", Game.GAME_WIDTH / 2, 300);*/

    }

    public void update(){
        menu.update();
        play.update();
    }
    public void keyPressed(KeyEvent e) {

    }

    //Verifică dacă cursorul mouse-ului este în interiorul unui anumit buton
    private boolean isIn(UrmButton b, MouseEvent e) {
        return b.getBounds().contains(e.getX(), e.getY());
    }

    public void mouseMoved(MouseEvent e) {
        play.setMouseOver(false);
        menu.setMouseOver(false);

        if (isIn(menu, e))
            menu.setMouseOver(true);
        else if (isIn(play, e))
            play.setMouseOver(true);
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(menu, e)) {
            if (menu.isMousePressed()) {
                playing.resetAll();
                playing.setGameState(Gamestate.MENU);
                //Gamestate.state = Gamestate.MENU;
            }
        } else if (isIn(play, e)) {
            if (play.isMousePressed()){
                playing.resetAll();
                playing.getGame().getAudioPlayer().setLevelSong(playing.getLevelManager().getLvlIndex());
            }
        }

        menu.resetBools();
        play.resetBools();
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(menu, e))
            menu.setMousePressed(true);
        else if (isIn(play, e))
            play.setMousePressed(true);
    }
}
