package utils;

import main.Game;

import javax.swing.plaf.PanelUI;

// conține clase inelare statice care conțin valori constante care vor fi utilizate în joc

public class Constants {
    public static final int ANI_SPEED = 25;
    public static final float GRAVITY = 0.04f * Game.SCALE;

    // definește tipuri de obiecte (poțiuni roșii și albastre, cufere, țepi) și
    // atributele lor (valorile poțiunilor, dimensiunile cufărului și poțiunilor, dimensiunile țepilor)
    public static class ObjectConstants {

        public static final int RED_POTION = 0;
        public static final int BLUE_POTION = 1;
        public static final int CHEST = 2;
        public static final int SPIKE = 3;

        public static final int RED_POTION_VALUE = 15;
        public static final int BLUE_POTION_VALUE = 10;

        public static final int CHEST_WIDTH_DEFAULT = 48;
        public static final int CHEST_HEIGHT_DEFAULT = 29; // 26
        public static final int CHEST_WIDTH = (int) (Game.SCALE * CHEST_WIDTH_DEFAULT);
        public static final int CHEST_HEIGHT = (int) (Game.SCALE * CHEST_HEIGHT_DEFAULT);

        public static final int POTION_WIDTH_DEFAULT = 12;
        public static final int POTION_HEIGHT_DEFAULT = 16;
        public static final int POTION_WIDTH = (int) (Game.SCALE * POTION_WIDTH_DEFAULT);
        public static final int POTION_HEIGHT = (int) (Game.SCALE * POTION_HEIGHT_DEFAULT);
        public static final int SPIKE_WIDTH_DEFAULT = 32;
        public static final int SPIKE_HEIGHT_DEFAULT = 32;
        public static final int SPIKE_WIDTH = (int) (Game.SCALE * SPIKE_WIDTH_DEFAULT);
        public static final int SPIKE_HEIGHT = (int) (Game.SCALE * SPIKE_HEIGHT_DEFAULT);

        public static int GetSpriteAmount(int object_type) {
            switch (object_type) {
                case RED_POTION, BLUE_POTION:
                    return 7;
                case CHEST:
                    return 5;
            }
            return 1;
        }
    }


    // definește tipuri de inamici (urs polar, războinic,fantome)
    // și atributele lor (dimensiunile fiecărui tip de inamic, offset-urile de desenare)
    public static class EnemyConstants {

        public static final int WARRIOR = 2;

        public static final int WARRIOR_WIDTH_DEFAULT = 94;   // 74 140
        public static final int WARRIOR_HEIGHT_DEFAULT = 60;  // 56   93
        public static final int WARRIOR_WIDTH = (int) (WARRIOR_WIDTH_DEFAULT * Game.SCALE);
        public static final int WARRIOR_HEIGHT = (int) (WARRIOR_HEIGHT_DEFAULT * Game.SCALE);

        public static final int WARRIOR_DRAWOFFSET_X = (int) (12 * Game.SCALE);   // 59
        public static final int WARRIOR_DRAWOFFSET_Y = (int) (21 * Game.SCALE);   // 21



        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int ATTACK = 2;
        public static final int HIT = 3;
        public static final int DEAD = 4;




        public static final int GHOST = 3;

        public static final int GHOST_WIDTH_DEFAULT = 44;
        public static final int GHOST_HEIGHT_DEFAULT = 44;

        public static final int GHOST_WIDTH = (int) (GHOST_WIDTH_DEFAULT * Game.SCALE);
        public static final int GHOST_HEIGHT = (int) (GHOST_HEIGHT_DEFAULT * Game.SCALE);

        public static final int GHOST_DRAWOFFSET_X = (int) (8 * Game.SCALE);
        public static final int GHOST_DRAWOFFSET_Y = (int) (5 * Game.SCALE);

        public static final int POLAR = 0;
        public static final int POLAR_WIDTH_DEFAULT = 64;
        public static final int POLAR_HEIGHT_DEFAULT = 64;

        public static final int POLAR_WIDTH = (int) (POLAR_WIDTH_DEFAULT * Game.SCALE);
        public static final int POLAR_HEIGHT = (int) (POLAR_HEIGHT_DEFAULT * Game.SCALE);

        public static final int POLAR_DRAWOFFSET_X = (int) (9 * Game.SCALE);
        public static final int POLAR_DRAWOFFSET_Y = (int) (26 * Game.SCALE);

        // returnează numărul de sprite-uri pentru fiecare stare a inamicului
        public static int GetSpriteAmount(int enemy_type, int enemy_state) {

            switch (enemy_type) {
               case WARRIOR:
                   switch (enemy_state){
                       case IDLE:
                       case ATTACK:
                       case RUNNING:
                       case HIT:
                           return 8;
                       case DEAD:
                           return 6;
                   }
                case GHOST:
                    switch (enemy_state){
                        case IDLE:
                        case RUNNING:
                            return 6;
                        case DEAD:
                            return 1;
                        case ATTACK:
                            return 2;
                        case HIT:
                            return 6;
                    }
                case POLAR:
                    switch (enemy_state){
                        case IDLE, RUNNING, ATTACK, HIT, DEAD:
                            return 4;
                    }
            }
            return 0;
        }

        // returnează valoarea maximă a sănătății pentru fiecare tip de inamic
        public static int GetMaxHealth(int enemy_type) {
            switch (enemy_type) {
                case GHOST:
                    return 18;
                case WARRIOR:
                    return 32;
                case POLAR:
                    return 10;
                default:
                    return 1;
            }
        }

        // returnează valoarea daunelor cauzate de fiecare tip de inamic
        public static int GetEnemyDmg(int enemy_type) {
            switch (enemy_type) {
                case GHOST:
                    return 20;
                case WARRIOR:
                    return 30;
                case POLAR:
                    return 10;
                default:
                    return 0;
            }

        }
    }

    //  dimensiunile pentru diverse elemente de mediu (munți mari și nori),
    //  toate scalate cu un factor din clasa Game.
    public static class Environment{
        public static final int BIG_MOUNTAIN_WIDTH_DEFAULT = 640;//488;
        public static final int BIG_MOUNTAIN_HEIGHT_DEFAULT = 640;//277;
        public static final int BIG_MOUNTAIN_WIDTH = (int)(BIG_MOUNTAIN_WIDTH_DEFAULT * Game.SCALE);
        public static final int BIG_MOUNTAIN_HEIGHT = (int)(BIG_MOUNTAIN_HEIGHT_DEFAULT * Game.SCALE);

        /*public static final int SMALL_MOUNTAIN_WIDTH_DEFAULT = 448;
        public static final int SMALL_MOUNTAIN_HEIGHT_DEFAULT = 101;
        public static final int SMALL_MOUNTAIN_WIDTH = (int)(SMALL_MOUNTAIN_WIDTH_DEFAULT * Game.SCALE);
        public static final int SMALL_MOUNTAIN_HEIGHT = (int)(SMALL_MOUNTAIN_HEIGHT_DEFAULT * Game.SCALE);*/

        public static final int CLOUDS_WIDTH_DEFAULT = 640;
        public static final int CLOUDS_HEIGHT_DEFAULT = 124;
        public static final int CLOUDS_WIDTH = (int)(CLOUDS_WIDTH_DEFAULT * Game.SCALE);
        public static final int CLOUDS_HEIGHT = (int)(CLOUDS_HEIGHT_DEFAULT * Game.SCALE);
    }


    // definește dimensiunile butoanelor din interfața utilizatorului (butoane de pauză, butoane URM, butoane de volum),
    // toate scalate cu un factor din clasa Game.
    public static class UI{
        public static class Buttons{
            public static final int B_WIDTH_DEFAULT = 140;
            public static final int B_HEIGHT_DEFAULT = 56;
            public static final int B_WIDTH = (int)(B_WIDTH_DEFAULT * Game.SCALE);
            public static final int B_HEIGHT = (int)(B_HEIGHT_DEFAULT * Game.SCALE);
        }
        public static class PauseButtons {
            public static final int SOUND_SIZE_DEFAULT = 42;
            public static final int SOUND_SIZE = (int) (SOUND_SIZE_DEFAULT * Game.SCALE);
        }

        public static class URMButtons {
            public static final int URM_DEFAULT_SIZE = 56;
            public static final int URM_SIZE = (int) (URM_DEFAULT_SIZE * Game.SCALE);

        }

        public static class VolumeButtons {
            public static final int VOLUME_DEFAULT_WIDTH = 28;
            public static final int VOLUME_DEFAULT_HEIGHT = 44;
            public static final int SLIDER_DEFAULT_WIDTH = 215;

            public static final int VOLUME_WIDTH = (int) (VOLUME_DEFAULT_WIDTH * Game.SCALE);
            public static final int VOLUME_HEIGHT = (int) (VOLUME_DEFAULT_HEIGHT * Game.SCALE);
            public static final int SLIDER_WIDTH = (int) (SLIDER_DEFAULT_WIDTH * Game.SCALE);
        }
    }

    // Definește direcțiile în joc (stânga, sus, dreapta, jos).
   public static class Directions{
        public static final int LEFT = 0;
        public static final int UP = 1;
        public static final int RIGHT = 2;
        public static final int DOWN = 3;
    }

    // Definește acțiunile jucătorului
    public static class PlayerConstants{
        public static final int IDLE = 0;
        public static final int ATTACK = 1;
        public static final int RUNNING = 2;
        public static final int JUMP = 3;
        public static final int FALLING = 4;
        public static final int HIT = 5;
        public static final int DEAD = 6;
        public static final int LIGHT = 8;
        public static final int LOW = 9;
        public static final int DEFEND = 10;

        public static int GetSpriteAmount(int player_action){

            switch (player_action){
                case IDLE:
                    return 6;
                case HIT:
                    return 4;
                case JUMP:
                case DEAD:
                case ATTACK:
                case LIGHT:
                case RUNNING:
                case FALLING:
                    return 8;
                case LOW:
                case DEFEND:
                    return 3;
                default:
                    return 1;
            }
        }
    }
}
