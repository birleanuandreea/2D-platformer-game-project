package ui;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utils.Constants.UI.URMButtons.*;

// afișează un ecran care apare atunci când jucătorul completează un nivel
// conține  două butoane: unul pentru areveni la meniul principal și altul pentru a trece la nivelul următor

public class LevelCompletedOverlay {

    private Playing playing;

    //butoanele pentru revenirea la meniul principal și pentru a trece la nivelul următor
    private UrmButton menu, next;
    private BufferedImage img;

    // Proprietatile imaginii pentru level completed
    private int bgX, bgY, bgW, bgH;
    public LevelCompletedOverlay(Playing playing){
        this.playing = playing;
        initImg();
        initButtons();
    }

    private void initButtons() {
        int menuX = (int) (330 * Game.SCALE);
        int nextX = (int) (445 * Game.SCALE);
        int y = (int) (195 * Game.SCALE);
        // tipul de buton ales din imagine
        next = new UrmButton(nextX, y, URM_SIZE, URM_SIZE, 0);
        menu = new UrmButton(menuX, y, URM_SIZE, URM_SIZE, 2);
    }

    private void initImg() {
        img = LoadSave.GetSpriteAtlas(LoadSave.COMPLETED_IMG);
        bgW = (int) (img.getWidth() * Game.SCALE);
        bgH = (int) (img.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgW / 2;
        bgY = (int) (75 * Game.SCALE);
    }

    public void draw(Graphics g) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        g.drawImage(img, bgX, bgY, bgW, bgH, null);
        next.draw(g);
        menu.draw(g);
    }

    public void update() {
        next.update();
        menu.update();
    }

    //verificare in interiorul butonului
    private boolean isIn(UrmButton b, MouseEvent e) {
        return b.getBounds().contains(e.getX(), e.getY());
    }

    public void mouseMoved(MouseEvent e) {
        next.setMouseOver(false);
        menu.setMouseOver(false);

        if (isIn(menu, e))
            menu.setMouseOver(true);
        else if (isIn(next, e))
            next.setMouseOver(true);
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(menu, e)) {
            if (menu.isMousePressed()) {
                playing.resetAll();
                playing.setGameState(Gamestate.MENU);
            }
        } else if (isIn(next, e))
            if (next.isMousePressed()){

                //Se calculează diferența dintre sănătatea maximă a jucătorului și sănătatea curentă
                int tmp = playing.getPlayer().getMaxHealth() - playing.getPlayer().getCurrentHealth();


                //Se încarcă nivelul următor în joc
                playing.loadNextLevel();
                //Se ajustează sănătatea jucătorului la valoarea maximă minus diferența calculată anterior
                playing.getGame().getPlaying().getPlayer().setCurrentHealth(playing.getPlayer().getMaxHealth() - tmp);
                playing.getGame().getAudioPlayer().setLevelSong(playing.getLevelManager().getLvlIndex());

                // Se obține indexul nivelului următor
                //int nivelUrmator = playing.getLevelManager().getLvlIndex();


                //Se folosește metoda saveLevel a obiectului GameDatabase pentru a salva nivelul următor
                // și starea de sănătate a jucătorului în baza de date
                //playing.getGame().getGameDatabase().saveLevel(nivelUrmator, playing.getPlayer().getMaxHealth() - tmp);//playing.getPlayer().getCurrentHealth());


            }

        menu.resetBools();
        next.resetBools();
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(menu, e))
            menu.setMousePressed(true);
        else if (isIn(next, e))
            next.setMousePressed(true);
    }
}
