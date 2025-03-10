package utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

// folosită pentru a încărca sprite-uri, background-uri, butoane și niveluri din fișiere

public class LoadSave {

    public static final String PLAYER_ATLAS_1 = "char_blue_1.png";
    public static final String LEVEL_ATLAS1 = "tiles1.png";
    public static final String LEVEL_ATLAS2 = "tile2.png";
    public static final String LEVEL_ATLAS3 = "tiles3.png";
    public static final String MENU_BUTTONS = "button_atlas.png";
    public static final String MENU_BACKGROUND = "menu_background.png";
    public static final String MENU_BACKGROUND_IMG = "background.jpg";
    public static final String PAUSE_BACKGROUND = "pause_menu.png";
    public static final String SOUND_BUTTONS = "sound_button.png";
    public static final String URM_BUTTONS = "urm_buttons.png";
    public static final String VOLUME_BUTTONS = "volume_buttons.png";
    public static final String PLAYING_BACKGROUND_IMG = "Above_sky.png";
    public static final String BIG_MOUNTAIN = "mountains.png";
    public static final String CLOUDS = "Above_clouds.png";
    public static final String GHOST_SPRITE = "ghost.png";
    public static final String WARRIOR_SPRITE = "warrior.png";
    public static final String BEAR_SPRITE = "polar_bear.png";
    public static final String STATUS_BAR = "health_power_bar.png";
    public static final String CHEST_ATLAS = "chests2.png";
    public static final String POTION_ATLAS = "potions_sprites.png";
    public static final String TRAP_ATLAS = "trap_atlas.png";
    public static final String COMPLETED_IMG = "completed_sprite.png";
    public static final String DEATH_SCREEN = "death_screen.png";
    public static final String OPTIONS_MENU = "options_background.png";



    //încarcă și returnează o imagine (BufferedImage) din resursele jocului, folosind numele fișierului specificat
    public static BufferedImage GetSpriteAtlas(String fileName) {
        BufferedImage image = null;
        InputStream is = null;

        try {
            // Încarcă imaginea ca stream de resurse
            is = LoadSave.class.getResourceAsStream("/" + fileName);

            // Asigurare că InputStream nu este null înainte de a citi imaginea
            if (is != null) {
                image = ImageIO.read(is);
            } else {
                throw new IOException("Resursa nu a fost gasita: " + fileName);
            }

           // assert is != null;
            //image = ImageIO.read(is);

        } catch (IOException e) {
            e.printStackTrace();    // Afisare stack trace pentru depanare
        } finally {
            // Asigurare că InputStream este închis pentru a elibera resursele
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();    // Tratează orice excepții apărute la închiderea InputStream
            }
        }

        return image;
    }

    // încarcă și returnează toate nivelurile disponibile sub forma unui array de imagini (BufferedImage[])
    public static BufferedImage[] GetAllLevels(){
        //Obține URL-ul directorului care conține fișierele nivelurilor (/lvls)
        URL url = LoadSave.class.getResource("/lvls");
        File file = null;

        // url = location, toURI = resources(folder)
        try{
            //  Asigurare că URL-ul nu este null
            assert url != null;
            // Transformă URL-ul într-un obiect File
            file = new File(url.toURI());
        }catch(URISyntaxException e){
            e.printStackTrace();
        }
        File[] files = file.listFiles();
        File[] filesSorted = new File[files.length];

       // sortează fișierele în ordine numerică (numele fișierelor sunt de forma 1.png, 2.png, etc.).
        for(int i=0;i< filesSorted.length;i++)
            for(int j=0;j< files.length;j++)
                if (files[j].getName().equals((i + 1) + ".png")) {
                    filesSorted[i] = files[j];
                }
        BufferedImage[] imgs = new BufferedImage[filesSorted.length];

        for(int i=0;i<imgs.length;i++){
            try {
                imgs[i] = ImageIO.read(filesSorted[i]);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return imgs;
    }
}
