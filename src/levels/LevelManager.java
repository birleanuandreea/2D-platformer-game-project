package levels;
import gamestates.Gamestate;
import main.Game;
import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static Objects.Chest.GetChest;

// esențială pentru gestionarea și schimbul nivelurilor în timpul jocului,
// precum și pentru desenarea și actualizarea lor pe ecran
public class LevelManager {

    private Game game;
    private ArrayList<Level> levels;
    private TileFactory tileFactory;
    private int lvlIndex = 0;
    public LevelManager(Game game){

        this.game = game;
        importOutsideSprites();
        levels = new ArrayList<>();
        buildAllLevels();
    }


    //Încarcă noul nivel și actualizează componentele jocului (inamici, jucător, obiecte)
    public void loadNextLevel(){
        lvlIndex++;
        if(lvlIndex >= levels.size()){
            lvlIndex = 0;
            System.out.println("Game completd!");
            Gamestate.state = Gamestate.MENU;
        }

        Level newLevel = levels.get(lvlIndex);
        importOutsideSprites();
        game.getPlaying().getEnemyManager().loadEnemies(newLevel);
        game.getPlaying().getPlayer().loadLvlData(newLevel.getLevelData());
        game.getPlaying().setMaxLvlOffset(newLevel.getLvlOffset());
        game.getPlaying().getObjectManager().loadObjects(newLevel);

    }

    //Încarcă toate imaginile nivelurilor din resurse
    private void buildAllLevels() {
        BufferedImage[] alllevels = LoadSave.GetAllLevels();
        for(BufferedImage img : alllevels)
            levels.add(new Level(img));
    }


    //Încarcă atlasurile de sprite-uri pentru niveluri
    public void importOutsideSprites() {


        BufferedImage img1 = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS1);
        BufferedImage img2 = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS2);
        BufferedImage img3 = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS3);
        BufferedImage img = null;


        switch (lvlIndex){
            case 0:
                img = img1;
                break;
            case 1:
                img = img2;
                break;
            case 2:
                img = img3;
                break;
        }
        //Creează un LevelTileFactory pentru a genera dale pentru nivel
        tileFactory = new LevelTileFactory(img);

    }


    //Desenează dala la poziția corectă, ajustată de offset-ul nivelului
    public void draw(Graphics g, int lvlOffset){
       for(int j=0;j<Game.TILES_IN_HEIGHT;j++){
            for(int i=0;i<levels.get(lvlIndex).getLevelData()[0].length;i++){

                //g.drawImage(levelSprite[index], Game.TILES_SIZE * i - lvlOffset, Game.TILES_SIZE * j,Game.TILES_SIZE,Game.TILES_SIZE, null);
                //g.drawImage(currentLevelSprite[index], Game.TILES_SIZE * i - lvlOffset, Game.TILES_SIZE * j,Game.TILES_SIZE,Game.TILES_SIZE, null);

                int index = levels.get(lvlIndex).getSpriteIndex(i, j);

                //Folosește tileFactory pentru a crea imaginea plăcii corespunzătoare
                BufferedImage tile = tileFactory.createTile(index);

                g.drawImage(tile, Game.TILES_SIZE * i - lvlOffset, Game.TILES_SIZE * j, Game.TILES_SIZE, Game.TILES_SIZE, null);
            }
        }
       // g.drawImage(levelSprite[46], 0, 0, null);
    }

    public void update(){

    }
    public Level getCurrentLevel(){
        return levels.get(lvlIndex);
    }

    //Seteaza nivelul curent la un anumit index. Daca indexul este invalid, arunca o exceptie IllegalArgumentException
    public void setCurrentLevel(int lvlIndex) throws InvalidLevelException{
        if(lvlIndex < 0 || lvlIndex >= levels.size()){
            //throw new IllegalArgumentException("Invalid level index");
            throw new InvalidLevelException();
        }
        this.lvlIndex = lvlIndex;
        loadCurrentLevel();
    }

    //Incarca datele nivelului curent in managerul de inamici, jucator si obiecte si seteaza offset-ul nivelului
    private void loadCurrentLevel() {
        Level newLevel = levels.get(lvlIndex);
        game.getPlaying().getEnemyManager().loadEnemies(newLevel);
        game.getPlaying().getPlayer().loadLvlData(newLevel.getLevelData());
        game.getPlaying().setMaxLvlOffset(newLevel.getLvlOffset());
        game.getPlaying().getObjectManager().loadObjects(newLevel);
    }

    public int getAmountOfLevels(){
        return levels.size();
    }
    public int getLvlIndex(){return lvlIndex;}
    public void setLvlIndex(int lvlIndex){this.lvlIndex=lvlIndex;}
}
