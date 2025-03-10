package levels;

import Objects.Potion;
import Objects.Spike;
import entities.*;
import main.Game;
import utils.HelpMethods;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utils.HelpMethods.*;

// responsabilă de gestionarea și manipularea datelor asociate unui nivel din joc

public class Level {

    private BufferedImage img;
    private int[][] lvlData;
    private ArrayList<Polar> polars;
    private ArrayList<Warrior> warriors;
    private ArrayList<Ghost> ghosts;
    private ArrayList<Potion> potions;
    private ArrayList<Spike> spikes;
    private int lvlTilesWide; // = LoadSave.GetLevelData()[0].length;   // 50
    private int maxTilesOffset; // = lvlTilesWide - Game.TILES_IN_WIDTH;    // 50 - 26 = 14
    private int maxLvlOffsetX; // = maxTilesOffset * Game.TILES_SIZE;

    public Level(BufferedImage img){
        this.img = img;
        createLevelData();
        createEnemies();
        createPotions();
        createSpikes();
        calcLvlOffsets();
    }

    private void createSpikes() {
        spikes = HelpMethods.GetSpikes(img);
    }

    private void createPotions() {
        potions = HelpMethods.GetPotions(img);
    }


    private void createEnemies() {
        warriors = GetWarrior(img);
        ghosts = GetGhost(img);
        polars=GetPolar(img);
    }

    private void calcLvlOffsets() {
        //lățimea nivelului în tiles
        lvlTilesWide = img.getWidth();

        // numărul maxim de tiles pe care nivelul se poate deplasa înainte de a ajunge la margine
        maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;

        //offset-ul maxim în pixeli pe care nivelul se poate deplasa pe axa X
        maxLvlOffsetX = Game.TILES_SIZE * maxTilesOffset;
    }

    private void createLevelData() {
        lvlData = GetLevelData(img);
    }

    public int getSpriteIndex(int x, int y){
        return lvlData[y][x];
    }
    public int[][] getLevelData(){
        return lvlData;
    }
    public int getLvlOffset(){
        return maxLvlOffsetX;
    }

   public ArrayList<Warrior> getWarriors(){
        return warriors;
    }
    public ArrayList<Ghost> getGhosts(){
        return ghosts;
    }
    public ArrayList<Potion> getPotions() {
        return potions;
    }
    public ArrayList<Spike> getSpikes(){
        return spikes;
    }

    public ArrayList<Polar> getPolars() {
        return polars;
    }
}
