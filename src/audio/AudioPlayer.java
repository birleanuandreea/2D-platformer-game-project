package audio;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import javax.sound.sampled.*;

// se ocupă cu gestionarea și redarea fișierelor audio: muzica de fundal pentru diferite nivele
// și efectele sonore asociate cu diverse acțiuni ale jucătorului

public class AudioPlayer {

    public static int MENU_1 = 0;
    public static int LEVEL_1 = 1;
    public static int LEVEL_2 = 2;

    public static int DIE = 0;
    public static int JUMP = 1;
    public static int GAMEOVER = 2;
    public static int LVL_COMPLETED = 3;
    public static int ATTACK_ONE = 4;
    public static int ATTACK_TWO = 5;
    public static int ATTACK_THREE = 6;

    private Clip[] songs, effects;  // un fel de audio player
    private int currentSongId;
    private float volume = 1f;
    private boolean songMute, effectMute;   // indica dacă funcția de mute este activată sau dezactivată
    private Random rand = new Random(); // pentru atac

    public AudioPlayer(){
        loadSongs();
        loadEffects();
        playSong(MENU_1);
    }
    private  void loadSongs(){
        String[] names = {"menu", "level1", "level2"};
        songs = new Clip[names.length];
        for(int i=0;i<songs.length;i++){
            songs[i]=getClip(names[i]);
        }
    }

    private void loadEffects() {
        String[] effectNames = { "die", "jump", "gameover", "lvlcompleted", "attack1", "attack2", "attack3" };
        effects = new Clip[effectNames.length];
        for (int i = 0; i < effects.length; i++)
            effects[i] = getClip(effectNames[i]);

        updateEffectsVolume();
    }


    private Clip getClip(String name){
        //pentru a obține URL-ul fișierului audio
        URL url = getClass().getResource("/audio/" + name + ".wav");
        //System.out.println(url);
        AudioInputStream audio;

        try {
            assert url != null;
            //pentru a obține un obiect AudioInputStream pentru fișierul audio
            audio = AudioSystem.getAudioInputStream(url);

            //AudioSystem.getClip() este utilizat pentru a obține un obiect Clip,
            // care este o sursă de sunet reprezentând un segment de sunet pre-încărcat în memorie care poate fi redat
            Clip c = AudioSystem.getClip();
            c.open(audio);
            return c;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }


    // Pentru ca jucatorul sa porneasca efecte/sunete

    // responsabilă pentru a comuta funcția de mute pentru cântece între activ și inactiv
    public void toggleSongMute(){
        this.songMute = !songMute;
        for(Clip c: songs){
            BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
            booleanControl.setValue(songMute);
        }

    }

    // pentru a comuta funcția de mute pentru efecte între activ și inactiv
    public void toggleEffectMute(){
        this.effectMute = !effectMute;
        for (Clip c : effects) {
            BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
            booleanControl.setValue(effectMute);
        }
        if (!effectMute)
            playEffect(JUMP);
    }
    public void setVolume(float volume){
        this.volume = volume;
        updateSongVolume();
        updateEffectsVolume();
    }
    public void setLevelSong(int lvlIndex){
        if(lvlIndex % 2 == 0)
            playSong(LEVEL_1);
        else
            playSong(LEVEL_2);
    }
    public void lvlCompleted(){
        stopSong();
        playEffect(LVL_COMPLETED);
    }
    public void playAttackSound(){
        int start = 4;
        start += rand.nextInt(3);
        playEffect(start);
    }

    public void playEffect(int effect) {
        effects[effect].setMicrosecondPosition(0);
        effects[effect].start();
    }
    public void playSong(int song) {
        stopSong();

        currentSongId = song;
        updateSongVolume();
        songs[currentSongId].setMicrosecondPosition(0);
        songs[currentSongId].loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stopSong() {
        if (songs[currentSongId].isActive())
            songs[currentSongId].stop();
    }


    // Sunete in fundal
    private void updateSongVolume() {
        //Control pentru a schimba volumul, balance
        FloatControl gainControl = (FloatControl) songs[currentSongId].getControl(FloatControl.Type.MASTER_GAIN);
        float range = gainControl.getMaximum() - gainControl.getMinimum();
        float gain = (range * volume) + gainControl.getMinimum();
        gainControl.setValue(gain);
    }

    // Le actualizam pe toate pentru ca sunt scurte si nu stim sigur cand apar
    private void updateEffectsVolume() {
        for (Clip c : effects) {
            FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (range * volume) + gainControl.getMinimum();
            gainControl.setValue(gain);
        }
    }
}
