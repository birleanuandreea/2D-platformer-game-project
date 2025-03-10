package gamestates;

// definește stările posibile ale jocului: PLAYING, MENU, OPTIONS, QUIT

public enum Gamestate {
    PLAYING, MENU, OPTIONS, QUIT;

    public static Gamestate state = MENU;
}
