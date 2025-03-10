package utils;
import Objects.Potion;
import Objects.Spike;
import entities.*;
import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utils.Constants.EnemyConstants.*;
import static utils.Constants.ObjectConstants.*;

// oferă diverse metode de utilitate pentru manipularea datelor nivelului,
// verificarea coliziunilor și gestionarea entităților în joc

public class HelpMethods {

    //-----------------Metode pentru verificarea coliziunilor și mișcării------------------------

    // Verifică dacă o entitate poate să se miște într-o anumită poziție,
    // verificând coliziunile la toate cele patru colțuri ale entității
    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData){

        if(!IsSolid(x,y,lvlData)){
            if(!IsSolid(x + width, y + height, lvlData)){
                if(!IsSolid(x + width,y, lvlData)){
                    if(!IsSolid(x,y + height, lvlData)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // Verifică dacă un punct specificat de coordonatele (x, y) este solid (ocupat) în cadrul datelor nivelului
   private static boolean IsSolid(float x, float y, int[][] lvlData) {
        int maxWidth = lvlData[0].length * Game.TILES_SIZE;
       // System.out.println(maxWidth);
       // Determină dacă coordonatele sunt în afara limitelor nivelului sau dacă tile-ul respectiv este solid
        if (x < 0 || x >= maxWidth)
            return true;
        if (y < 0 || y >= Game.GAME_HEIGHT)
            return true;

        float xIndex = x / Game.TILES_SIZE;
        float yIndex = y / Game.TILES_SIZE;


       return IsTileSolid((int) xIndex, (int) yIndex, lvlData);
    }

    // Verifică dacă un tile specificat de coordonatele xTile și yTile este solid
    public static boolean IsTileSolid(int xTile, int yTile, int[][] lvlData) {

        int value = lvlData[yTile][xTile];

        switch (value){
            case 47, 41, 46, 42, 28, 29, 30, 40, 43, 44, 36:
                return false;
            default:
                return true;
        }

        /*if (value >= 48 || value < 0 || (value != 47 && value != 41 && value != 46 && value != 42 && value != 28 && value != 29 && value != 30 && value != 40 && value != 43 && value != 44 && value != 36))
            return true;

        return false;*/
    }


//--------------------Metode pentru determinarea poziției entităților----------------------

// Returnează poziția X(la care entitatea ar trebui să se oprească lângă un perete)
// a unei entități lângă un perete, luând în considerare direcția de mișcare
  public static float GetEntityXPosNextToWall(Rectangle2D.Float hitBox, float xSpeed){
        // tile-ul în care se află entitatea
        int currentTile = (int)(hitBox.x / Game.TILES_SIZE);
        if(xSpeed > 0){
            // RIGHT
            int tileXPos = currentTile * Game.TILES_SIZE;

            //distanța dintre marginea dreaptă a tile-ului și marginea dreaptă a hitbox-ului entității
            int xOffset = (int)(Game.TILES_SIZE - hitBox.width);

            //Poziția finală este calculată ca poziția de început a tile-ului curent plus offset-ul și minus 1 pentru a evita intrarea în perete
            return tileXPos + xOffset - 1;  // 1 not inside
        }else{
            // LEFT
            return currentTile * Game.TILES_SIZE;
        }
    }

    // Returnează poziția Y a unei entități sub un acoperiș sau deasupra podelei, luând în considerare direcția de mișcare verticală
    public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitBox, float airSpeed){

        int currentTile = (int)(hitBox.y / Game.TILES_SIZE);
        if(airSpeed > 0){
            // FALLING - touching floor
            int tileYPos = currentTile * Game.TILES_SIZE;
            int yOffset = (int)(Game.TILES_SIZE - hitBox.height);
            return tileYPos + yOffset - 1;  // 1 not inside the tile
        }else{
            // JUMPING
            return currentTile * Game.TILES_SIZE;
        }
    }

    //--------------------------Metode pentru verificarea poziției entităților----------------------------

    //Verifică dacă o entitate se află pe podea
    public static boolean IsEntityOnFloor(Rectangle2D.Float hitBox, int[][] lvlData){
        // Check the pixel below bottomleft and bottomright
        if(!IsSolid(hitBox.x, hitBox.y + hitBox.height + 1,lvlData)){
            if(!IsSolid(hitBox.x + hitBox.width, hitBox.y + hitBox.height + 1,lvlData )){
                return false;
            }
        }
        return true;
    }


    // Verifică dacă există podea sub hitbox-ul entității, cu sau fără considerarea vitezei pe axa X
    public static boolean IsFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
        if (!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData))
            if (!IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData))
                return false;
        return true;
    }
    public static boolean IsFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData) {
        if (xSpeed > 0)
            return IsSolid(hitbox.x + hitbox.width + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
        else
            return IsSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
    }


    //-------------------Metode pentru verificarea tile-urilor-------------------

    //Verifică dacă toate tile-urile dintre două coordonate X sunt walkable (navigabile)
    public static boolean IsAllTilesWalkable(int xStart, int xEnd, int y, int[][] lvlData) {
        /*if (IsAllTilesClear(xStart, xEnd, y, lvlData))
            for (int i = 0; i < xEnd - xStart; i++) {
                if (!IsTileSolid(xStart + i, y + 1, lvlData))
                    return false;
            }
        return true;*/
        for (int i = 0; i < xEnd - xStart; i++) {
            if (IsTileSolid(xStart + i, y, lvlData))
                return false;
            if (!IsTileSolid(xStart + i, y + 1, lvlData))
                return false;
        }

        return true;
    }

    //Verifică dacă linia de vedere dintre două hitbox-uri este clară, adică toate tile-urile dintre cele două hitbox-uri sunt navigabile
    public static boolean IsSightClear(int[][] lvlData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox, int yTile) {
        int firstXTile = (int) (firstHitbox.x / Game.TILES_SIZE);
        int secondXTile = (int) (secondHitbox.x / Game.TILES_SIZE);

        if (firstXTile > secondXTile)
            return IsAllTilesWalkable(secondXTile, firstXTile, yTile, lvlData);
        else
            return IsAllTilesWalkable(firstXTile, secondXTile, yTile, lvlData);
    }


    //--------------------Metode pentru obținerea datelor nivelului și entităților--------------

    //Convertește o imagine a nivelului într-o matrice de date a nivelului
    public static int[][] GetLevelData(BufferedImage img){
        int[][] lvlData =  new int [img.getHeight()][img.getWidth()];
        for(int j=0;j<img.getHeight();j++){
            for(int i=0;i<img.getWidth();i++){
                //Folosește culoarea roșie a pixelilor pentru a determina valorile tile-urilor
                Color color = new Color(img.getRGB(i,j));
                int value = color.getRed();
                if(value >= 48){
                    value = 0;
                }
                lvlData[j][i] = value;
            }
        }
        return lvlData;
    }
    public static ArrayList<Polar> GetPolar(BufferedImage img) {
        ArrayList<Polar> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                if (value == POLAR)
                    list.add(new Polar(i * Game.TILES_SIZE, j * Game.TILES_SIZE));
            }
        return list;
    }

    //Returnează o listă de entități specifice (Polar, Warrior, Ghost, Potion, Spike) dintr-o imagine a nivelului
    public static ArrayList<Warrior> GetWarrior(BufferedImage img) {
        ArrayList<Warrior> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                if (value == WARRIOR)
                    list.add(new Warrior(i * Game.TILES_SIZE, j * Game.TILES_SIZE));
            }
        return list;
    }
    public static ArrayList<Ghost> GetGhost(BufferedImage img) {
        ArrayList<Ghost> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                if (value == GHOST)
                    list.add(new Ghost(i * Game.TILES_SIZE, j * Game.TILES_SIZE));
            }
        return list;
    }
    public static ArrayList<Potion> GetPotions(BufferedImage img) {
        ArrayList<Potion> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getBlue();
                if (value == RED_POTION || value == BLUE_POTION)
                    list.add(new Potion(i * Game.TILES_SIZE, j * Game.TILES_SIZE, value));
            }
        return list;
    }
    public static ArrayList<Spike> GetSpikes(BufferedImage img) {
        ArrayList<Spike> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getBlue();
                if (value == SPIKE)
                    list.add(new Spike(i * Game.TILES_SIZE, j * Game.TILES_SIZE, SPIKE));
            }
        return list;
    }
}
