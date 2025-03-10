package main;

import inputs.KeyboardInputs;
import inputs.MouseInputs;

import javax.swing.*;
import java.awt.*;
import static main.Game.GAME_WIDTH;
import static main.Game.GAME_HEIGHT;

// reține și afișează jocul pe un Jpanel, inițializează și gestionează intrările de la tastatură și mouse

public class GamePanel extends JPanel {

    private MouseInputs mouseInputs;
    private Game game;

    public GamePanel(Game game){

        /*try {
            importImg();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/

        mouseInputs = new MouseInputs(this);
        this.game = game;
        setPanelSize();
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }


    //Setarea dimensiunii panoului
    private void setPanelSize() {
        Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);  // 40 tiles wide, 25 tiles high, 32x32 pixels
        setPreferredSize(size);
       // System.out.println("Size: " + GAME_WIDTH + " : " + GAME_HEIGHT);
    }

    public void updateGame(){
    }


    //
    public void paintComponent(Graphics g){
        //pentru a asigura o curățare corectă a panoului
        super.paintComponent(g);

        //Se apelează metoda render a jocului pentru a desena conținutul jocului pe panou
        game.render(g);
    }
    public Game getGame(){
        return game;
    }
}
