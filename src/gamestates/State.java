package gamestates;

import audio.AudioPlayer;
import main.Game;
import ui.MenuButton;

import java.awt.event.MouseEvent;
// servește drept bază pentru toate stările specifice ale jocului și conține metode utile pentru gestionarea jocului și a stării acestuia

public class State {

    protected Game game;
    public State(Game game){
        this.game = game;
    }

    public Game getGame(){
        return game;
    }
    public boolean isIn(MouseEvent e, MenuButton mb){
        return mb.getBounds().contains(e.getX(), e.getY());
    }
    public void setGameState(Gamestate state){
        switch (state){
            case MENU -> game.getAudioPlayer().playSong(AudioPlayer.MENU_1);
            case PLAYING -> game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLvlIndex());
        }

        Gamestate.state = state;
    }
}

